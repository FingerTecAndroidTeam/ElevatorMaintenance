package com.fingertec.utils;

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.fingertec.common.EMApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;


public class PrefUtils {
    /**
     * 在SharedPreferences中写入指定的内容
     *
     * @param name
     * @param key
     * @return
     */
    public static void WriteSharedPreferencesInt(String name, String key,
                                                 int value) {
        SharedPreferences.Editor userInfoEditor = EMApplication.getInstance()
                .getSharedPreferences(name, EMApplication.getInstance().MODE_PRIVATE).edit();
        userInfoEditor.putInt(key, value);
        userInfoEditor.commit();
    }

    /**
     * 在SharedPreferences中读取指定的内容
     *
     * @param name
     * @param key
     * @return
     */
    public static int ReadSharedPreferencesInt(String name, String key,
                                               int defValue) {
        try {
            SharedPreferences userInfo = EMApplication.getInstance()
                    .getSharedPreferences(name, EMApplication.getInstance().MODE_PRIVATE);
            return userInfo.getInt(key, defValue);
        } catch (NullPointerException e) {
            return defValue;
        }
    }

    /**
     * 在SharedPreferences中读取指定的内容
     *
     * @param name
     * @param key
     * @return 默认为true
     */
    public static boolean ReadSharedPreferencesBoolean(String name, String key) {
        try {
            SharedPreferences userInfo = EMApplication.getInstance()
                    .getSharedPreferences(name, EMApplication.getInstance().MODE_PRIVATE);
            return userInfo.getBoolean(key, false);
        } catch (NullPointerException e) {
            return true;
        }
    }

    /**
     * 向SharedPreferences中写入指定的内容
     *
     * @param name
     * @param key
     * @param value
     */
    public static void WriteSharedPreferencesBoolean(String name, String key,
                                                     boolean value) {
        SharedPreferences.Editor userInfoEditor = EMApplication.getInstance()
                .getSharedPreferences(name, EMApplication.getInstance().MODE_PRIVATE).edit();
        userInfoEditor.putBoolean(key, value);
        userInfoEditor.commit();
    }

    /**
     * 在SharedPreferences中读取指定的内容
     *
     * @param name
     * @param key
     * @return 默认为空“”
     */
    public static String ReadSharedPreferencesString(String name, String key) {
        try {
            SharedPreferences userInfo = EMApplication.getInstance()
                    .getSharedPreferences(name, EMApplication.getInstance().MODE_PRIVATE);
            return userInfo.getString(key, null);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * @param name
     * @param key
     * @param value
     */

    public static void WriteSharedPreferencesString(String name, String key,
                                                    String value) {
        SharedPreferences.Editor userInfoEditor = EMApplication.getInstance()
                .getSharedPreferences(name, EMApplication.getInstance().MODE_PRIVATE).edit();
        userInfoEditor.putString(key, value);
        userInfoEditor.commit();
    }

    public static long ReadSharedPreferencesLong(String name, String key,
                                                 long defValue) {
        try {
            SharedPreferences userInfo = EMApplication.getInstance()
                    .getSharedPreferences(name, EMApplication.getInstance().MODE_PRIVATE);
            return userInfo.getLong(key, defValue);
        } catch (NullPointerException e) {
            return defValue;
        }
    }

    public static void WriteSharedPreferencesLong(String name, String key,
                                                  long value) {
        SharedPreferences.Editor userInfoEditor = EMApplication.getInstance()
                .getSharedPreferences(name, EMApplication.getInstance().MODE_PRIVATE).edit();
        userInfoEditor.putLong(key, value);
        userInfoEditor.commit();
    }

    public static <T> void WriteSharedPreferencesObject(String name, String key,
                                                 T object) {
        SharedPreferences preferences = EMApplication.getInstance().getSharedPreferences(name,
                EMApplication.getInstance().MODE_PRIVATE);
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(object);
            // 将字节流编码成base64的字符窜
            String object_Base64 = new String(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, object_Base64);
            editor.commit();
        } catch (IOException e) {
            // TODO Auto-generated
        }
        Log.i("ok", "存储成功");
    }

    public static <T> ArrayList<T> ReadSharedPreferencesObject(String name, String key, Class<T> cls) {
        ArrayList<T> object = null;
        SharedPreferences preferences = EMApplication.getInstance().getSharedPreferences(name,
                EMApplication.getInstance().MODE_PRIVATE);
        String productBase64 = preferences.getString(key, "");

        //读取字节
        byte[] base64 = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);

        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                object = (ArrayList<T>) bis.readObject();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return object;
    }
}
