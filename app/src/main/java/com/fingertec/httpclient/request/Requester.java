package com.fingertec.httpclient.request;

import android.os.Looper;
import android.util.Log;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.fingertec.bean.Bean;
import com.fingertec.bean.User;
import com.fingertec.common.Constants;
import com.fingertec.httpclient.Action;
import com.fingertec.httpclient.IHttpGetResultListener;
import com.fingertec.httpclient.IHttpReqListener;
import com.fingertec.httpclient.Msg;
import com.fingertec.httpclient.Ret;
import com.fingertec.utils.DeviceUtils;
import com.fingertec.utils.JsonUtil;
import com.fingertec.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rxandroidnetworking.RxANRequest;
import com.rxandroidnetworking.RxAndroidNetworking;

import org.json.JSONObject;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jqr on 2016/12/27.
 */
public class Requester implements IRequest {

    private static final String TAG = Requester.class.getSimpleName();
    private IHttpReqListener mListener;
    private IHttpGetResultListener mGetResultListener;

    public Requester(IHttpReqListener listener) {
        mListener = listener;
    }

    public void setResultListener(IHttpGetResultListener getResultListener) {
        this.mGetResultListener = getResultListener;
    }

    @Override
    public void sendGetRequest(RequestContent action) {

    }


    @Override
    public <T> void sendPostRequest(final int action, final RequestContent requestContent, final Class<T> cls) {
        //判断联网
        if (!DeviceUtils.isNetworkAvailable()) {
            Bean bean = new Bean(Ret.FAIL, Msg.NETWORK_ERR);//服务器返回的状态值
            bean.errorcode = -99;
            mListener.OnComplete(Action.NETWORK, bean);
            return;
        }
        RxANRequest.GetRequestBuilder getRequestBuilder = new RxANRequest.GetRequestBuilder(requestContent.getRequestUrl());
        getRequestBuilder.addQueryParameter(requestContent.getParameters())
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis + " bytesSent : " + bytesSent + " bytesReceived : " + bytesReceived + " isFromCache : " + isFromCache);
                    }
                })
                .getJSONObjectObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onComplete Detail : checkForHeaderGet completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ANError) {
                            ANError anError = (ANError) e;
                            if (anError.getErrorCode() != 0) {
                                Log.d(TAG, "onError errorCode : " + anError.getErrorCode() + " onError errorBody : " + anError.getErrorBody() + " onError errorDetail : " + anError.getErrorDetail());
                                Bean bean = new Bean(Ret.FAIL, anError.getErrorDetail());
                                bean.errorcode = -99;
                                mListener.OnComplete(Action.ERROR, bean);
                            } else {
                                // error.getErrorDetail() : connectionError连接错误, parseError解析错误, requestCancelledError请求取消错误
                                Log.d(TAG, "onError errorDetail : " + anError.getErrorDetail());
                                Bean bean = new Bean(Ret.FAIL, anError.getErrorDetail());
                                bean.errorcode = -99;
                                mListener.OnComplete(Action.ERROR, bean);
                            }
                        } else {
                            Log.d(TAG, "onError errorMessage : " + e.getMessage());
                            Bean bean = new Bean(Ret.FAIL, e.getMessage());
                            bean.errorcode = -99;
                            mListener.OnComplete(Action.ERROR, bean);

                        }
                    }

                    @Override
                    public void onNext(JSONObject response) {
                        Log.d(TAG, "onResponse object : " + response.toString());
                        Log.d(TAG, "onResponse isMainThread : " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));
                        //根据参数Class Bean解析数据
                        T bean = JsonUtil.fromJson(response.toString(), cls);
                        mListener.OnComplete(action, bean);
                    }
                });

    }

    @Override
    public <T> void sendPostRequestList(final int action, RequestContent requestContent, final Class<T> cls) {

    }


    @Override
    public void cancelAllRequests(boolean mayInterruptIfRunning) {

    }

    @Override
    public void setTimeOut(int value) {

    }

    @Override
    public void downloadFile(String url) {

    }
}
