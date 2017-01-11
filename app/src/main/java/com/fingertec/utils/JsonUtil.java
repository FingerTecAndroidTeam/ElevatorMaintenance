package com.fingertec.utils;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by liukai on 2015/2/3.
 */
public class JsonUtil {
    public static <T> T fromJson(String jsonStr, Class<T> arg0) {
        Gson gson = new Gson();
        T mEntity = gson.fromJson(jsonStr, arg0);
        return mEntity;
    }

    public static <T> T fromJson(InputStream inputStream, Class<T> arg0) {
        Gson gson = new Gson();
        T mEntity = gson.fromJson(new BufferedReader(new InputStreamReader(inputStream)), arg0);
        return mEntity;
    }

}
