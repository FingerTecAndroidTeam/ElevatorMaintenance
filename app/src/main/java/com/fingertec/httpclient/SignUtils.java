package com.fingertec.httpclient;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jqr on 2016/12/27.
 * 1.key,服务器与前端使用同一个名字来匹配。
 * 2.KEY_TIMESTAMP 时间戳,目的是为了控制这个请求在一个时间范围内有效。例如：两分钟。如果超过了两分钟，还在用这个请求请求服务器那就不会成功。
 * 3.KEY_VERSION 接口版本号,升级专用。前端app升级后为了兼容后台接口而定制的。
 *
 */
public class SignUtils {

    public static final String KEY_PRIVATE = "key";

    public static final String KEY_TIMESTAMP = "timestamp";

    public static final String KEY_VERSION = "version";

    public static HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<>();
        params.put(KEY_PRIVATE, "alsfoxShop_plat");
        params.put(KEY_VERSION, "1.01");
        params.put(KEY_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        return params;
    }
}
