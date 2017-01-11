package com.fingertec.httpclient.request;

import com.fingertec.httpclient.SignUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jqr on 2016/12/27.
 * 外界将请求的地址，请求结果的解析参照对象以及请求参数都保存到这个类中
 */
 public class RequestContent <T>{

    /**
     * 发送请求的路径
     */
    private String requestUrl;

    /**
     * 请求参数，new出一个map对象，将默认的先添加进去
     */
    private HashMap<String, String> parameters = SignUtils.getParameters();

    /**
     * 外界传入参数的构造方法，没有返回结果的请求
     */
    public RequestContent(String requestUrl) {
        this.requestUrl = requestUrl;
    }


    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

}
