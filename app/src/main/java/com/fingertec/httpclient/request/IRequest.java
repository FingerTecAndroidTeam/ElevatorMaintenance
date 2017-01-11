package com.fingertec.httpclient.request;


/**
 * 该接口规定所有请求所需的常量和方法
 */
public interface IRequest {
    //状态码
    String TAG_STATUS_CODE = "statusCode";

    //请求失败后的失败内容
    String TAG_MESSAGE = "message";

    //json对象
    String TAG_OBJECT = "object";

    //json集合
    String TAG_OBJECTS = "objects";

    //默认超时时间
    int VALUE_DEFAULT_TIME_OUT = 20 * 1000;


    /**
     * 发送get请求
     *
     * @param action
     */
    void sendGetRequest(RequestContent action);


    public <T> void sendPostRequest(int action, RequestContent requestContent,  Class<T> cls);

    public <T> void sendPostRequestList(int action, RequestContent requestContent, Class<T> cls);

    /**
     * 取消所有请求，可能中断请求
     */
    void cancelAllRequests(boolean mayInterruptIfRunning);

    /**
     * 重新设置请求超时时间
     */
    void setTimeOut(int value);

    /**
     * 下载文件
     */
    void downloadFile(String url);
}
