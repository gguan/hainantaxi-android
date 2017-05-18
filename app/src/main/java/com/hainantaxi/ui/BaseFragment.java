package com.hainantaxi.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.hainantaxi.net.TokenThrowable;
import com.facebook.drawee.backends.pipeline.Fresco;

import butterknife.Unbinder;

/**
 * Created by develop on 2017/5/17.
 */

public abstract class BaseFragment extends Fragment {

    protected String TAG = this.getClass().getSimpleName();

    protected Unbinder mUnbinder;

    public void handleError(Throwable throwable) {


        if (throwable instanceof TokenThrowable) {
            jump2Login();
            return;
        }

    }

    protected void pauseLoadImage() {
        Fresco.getImagePipeline().pause();
    }

    protected void resumeLoadImage() {
        Fresco.getImagePipeline().resume();
    }

    private void jump2Login() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Fresco.getImagePipeline().isPaused()) {
            Fresco.getImagePipeline().resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
