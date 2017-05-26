package com.hainantaxi.net.remote;

import android.content.Context;
import android.util.Log;

import com.hainantaxi.BuildConfig;
import com.hainantaxi.Config;
import com.hainantaxi.modle.entity.Region;
import com.hainantaxi.net.BaseDataSource;
import com.hainantaxi.net.ErrorListener;
import com.hainantaxi.net.HTTPResponse;
import com.hainantaxi.net.service.MyService;
import com.hainantaxi.net.service.PreferenceService;
import com.hainantaxi.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by develop on 2017/5/17.
 */

@Singleton
public class BaseRemoteDataSource implements BaseDataSource {

    private Retrofit mRetrofit;
    private MyService mPlayService;
    private ErrorListener mErrorListener;
    private OkHttpClient mOkHttpClient;

    public BaseRemoteDataSource(Context context, PreferenceService preferenceService) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //.registerTypeAdapterFactory(new ResponseTypeAdapterFactory())
        gsonBuilder = buildGsonBuilder(gsonBuilder);
        Gson gson = gsonBuilder.create();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG)
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        else
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

        String userAgent = Config.USER_AGENT_HEAD + AppUtils.getVersion(context) + AppUtils.getUserAgent();
        String version = Config.APP_VERSION_HEAD + AppUtils.getVersion(context);
        Interceptor mTokenInterceptor = chain -> {
            Request originRequest = chain.request();

            long time = System.currentTimeMillis();
            String ts = String.valueOf(time / Config.NET_TIME_OUT) + "." + String.valueOf(time % Config.NET_TIME_OUT);

            Request authorisedRequest = originRequest.newBuilder()
                    .header(Config.KEY_TOKEN_HEAD, preferenceService.getToken())
                    .header(Config.KEY_AGENT, userAgent)
                    .addHeader(Config.KEY_PLAY_VERSION, version)
                    .addHeader(Config.KEY_TIME_OUT, ts)
                    .build();

            Response response = chain.proceed(authorisedRequest);


            if (mErrorListener != null) {
                mErrorListener.onRemoteErrorHappened(response.code());
            }
            return response;
        };


        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(mTokenInterceptor);


        try {

            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, null);
            X509TrustManager trustManager = (X509TrustManager) trustAllCerts[0];
            builder.sslSocketFactory(sslContext.getSocketFactory(), trustManager);
            builder.hostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            Log.e("===", "SSL错误" + e.toString());
        }

        mOkHttpClient = builder.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mPlayService = mRetrofit.create(MyService.class);
    }

    private GsonBuilder buildGsonBuilder(GsonBuilder gsonBuilder) {
        return gsonBuilder;
    }


    public void setErrorListener(ErrorListener mErrorListener) {
        this.mErrorListener = mErrorListener;
    }

    @Override
    public Observable<HTTPResponse<Region>> fetchRegion(double lat, double lng, int zoomDepth) {
        return mPlayService.fetchRegion(lat, lng, zoomDepth);
    }
}
