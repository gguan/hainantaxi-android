package com.hainantaxi.map;


import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.hainantaxi.MyApplication;
import com.hainantaxi.utils.SubscriptionManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by develop on 2017/5/25.
 */

public class PoiSearchTask extends SubscriptionManager {


    public static final String Input_tips = "input_tips";
    public static final int SUCCESS_CODE = 1000;

    public Observable<PoiResult> loadSearchData(int pageSize, int pageNum, String keyWord, String searchType, String cityCode) {
        return Observable.create(subscriber -> searchPoi(subscriber, pageSize, pageNum, keyWord, searchType, cityCode));
    }


    public Observable<List<Tip>> loadImputtips(String newText, String city) {
        return Observable.create(subscriber -> searchInputTips(subscriber, newText, city));
    }


    private void searchPoi(final Subscriber<? super PoiResult> subscriber, int pageSize, int pageNum, String keyWord, String searchType, String cityCode) {
        PoiSearch.Query query = new PoiSearch.Query(keyWord, searchType, cityCode);
        query.setPageSize(pageSize);
        query.setPageNum(pageNum);

        PoiSearch search = new PoiSearch(MyApplication.getContext(), query);
        search.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                if (SUCCESS_CODE == i) {
                    ArrayList<PoiItem> pois = poiResult.getPois();
                    if (pois == null || pois.size() < 1) {
                        List<SuggestionCity> searchSuggestionCitys = poiResult.getSearchSuggestionCitys();
                        if (searchSuggestionCitys == null || searchSuggestionCitys.size() < 1) {
                            List<String> searchSuggestionKeywords = poiResult.getSearchSuggestionKeywords();
                        } else {

                        }
                    } else {

                    }
                    subscriber.onNext(poiResult);
                    subscriber.onCompleted();
                }

            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        search.searchPOIAsyn();
    }


    private void searchInputTips(final Subscriber<? super List<Tip>> subscriber, String newText, String city) {
        InputtipsQuery inputtipsQuery = new InputtipsQuery(newText, city);
        inputtipsQuery.setCityLimit(true);//限制为当前城市

        Inputtips inputtips = new Inputtips(MyApplication.getContext(), inputtipsQuery);

        inputtips.setInputtipsListener((list, i) -> {
            if (SUCCESS_CODE == i) {
                for (Tip t : list) {
                    String address = t.getAddress();
                    String adcode = t.getAdcode();
                    String district = t.getDistrict();
                    String name = t.getName();
                    String poiID = t.getPoiID();
                    String typeCode = t.getTypeCode();
                    LatLonPoint point = t.getPoint();
                }
                subscriber.onNext(list);
                subscriber.onCompleted();
            } else {
                subscriber.onError(new Throwable("" + i));
            }
        });

        inputtips.requestInputtipsAsyn();
    }




}
