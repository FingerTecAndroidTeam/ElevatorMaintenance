package com.fingertec.httpclient;

public interface IHttpReqListener {
    /**
     * http 信息返回接口
     */
	public <T> void OnComplete(int action, T bean);

}
