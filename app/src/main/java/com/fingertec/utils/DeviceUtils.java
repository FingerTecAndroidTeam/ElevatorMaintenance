/*****************************
 * Copyright (c) 2014 by Hisunflytone Co. Ltd.  All rights reserved.
 ****************************/
package com.fingertec.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Display;

import com.fingertec.common.EMApplication;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author linwd
 * @version v3.0.0
 * @{# DeviceUtils.java Create on 2014-2-28 下午4:02:01
 * <p>
 * <p/>
 * </p>
 */
public class DeviceUtils {

    public static String getUUID() {
        String onlyCode = "";
        final TelephonyManager tm = (TelephonyManager) EMApplication.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, androidId;
        if (null != tm.getDeviceId()) {
            tmDevice = MD5Util.getMD5(tm.getDeviceId());//加MD5 加密
        } else {
            tmDevice = null;
        }
        if (null != android.provider.Settings.Secure.getString(EMApplication.getInstance().getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID)) {
            androidId = MD5Util.getMD5(android.provider.Settings.Secure.getString(EMApplication.getInstance().getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));//加MD5 加密
        } else {
            androidId = null;
        }

        if (null != tmDevice && !"".equals(tmDevice)) {
            onlyCode = tmDevice;//机器号
            onlyCode = "D" + onlyCode;
        } else if (null != androidId && !"".equals(androidId)) {
            onlyCode = androidId;//Android ID
            onlyCode = "A" + onlyCode;
        } else {
            onlyCode = PrefUtils.ReadSharedPreferencesString("RandomUUID", "UUID");
            if (onlyCode == null) {
                onlyCode = UUID.randomUUID().toString();//随机一个UUID String
                onlyCode = "R" + onlyCode;
                PrefUtils.WriteSharedPreferencesString("RandomUUID", "UUID", onlyCode);
            }
        }
        return onlyCode;
    }

    public static String getOldUUID() {
        final TelephonyManager tm = (TelephonyManager) EMApplication.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(EMApplication.getInstance().getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();

        return uniqueId;
    }

    public static int getScreenWidth() {
        return EMApplication.getInstance().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return EMApplication.getInstance().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = EMApplication.getInstance().getResources()
                .getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = EMApplication.getInstance().getResources()
                .getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //获取手机状态栏高度
    public static int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = EMApplication.getInstance().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public static String getVersionName() {
        PackageInfo pinfo;
        try {
            pinfo = EMApplication
                    .getInstance()
                    .getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo("com.yousheng.youshengapp",
                            PackageManager.GET_CONFIGURATIONS);
            return pinfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getVersionCode() {
        PackageInfo pinfo;
        try {
            pinfo = EMApplication
                    .getInstance()
                    .getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo("com.fingertec.elevatormaintenance",
                            PackageManager.GET_CONFIGURATIONS);
            return pinfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
// 判断版本是否需要引导图使用
//     public static int getLastVersionCode(){
//         return PrefUtils.ReadSharedPreferencesInt(Constants.INTRODUCE_VERSION_NAME, Constants.INTRODUCE_VERSION_KEY, 0);
//     }
//
//     public static void setLastVersionCode(int ver){
//         PrefUtils.WriteSharedPreferencesInt(Constants.INTRODUCE_VERSION_NAME, Constants.INTRODUCE_VERSION_KEY, ver);
//     }

    public static String getIPAddr() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getMobileNetWorkType() {
        TelephonyManager telephonyManager = (TelephonyManager) EMApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2g";

            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3g";

            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4g";

            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "unknown";

            default:
                return "unknown";
        }
    }

    /**
     * 获取网络状态，wifi,wap,2g,3g,4g
     */
    public static String getNetWorkType() {
        ConnectivityManager manager = (ConnectivityManager) EMApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                return "wifi";
            } else if (type.equalsIgnoreCase("MOBILE")) {
                return getMobileNetWorkType();
            }
        }

        return "unknown";
    }

    public static String getTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date now = new Date(System.currentTimeMillis());
            Date commentTime = (Date) sdf.parse(time);
            // 如果是一年前（因为闰年366天，不好统一用365来算，特殊处理）
            if (commentTime.getYear() < now.getYear()) {
                return time.substring(0, 16);
            } else {
                // 算得两者时间之差为day天hour小时min分钟
                long diff = now.getTime() - commentTime.getTime();
                long day = diff / (24 * 60 * 60 * 1000);
                long hour = (diff / (60 * 60 * 1000) - day * 24);
                long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
                long second = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                if (day == 0 && hour == 0 && min == 0) {
                    return "刚刚";
                } else if (day == 0 && hour == 0) {
                    return min + "分钟前";
                } else if (day == 0) {
                    return hour + "小时前";
                } else {
                    return time.substring(5, 16);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }


    public static String getNameFromUrl(String url) {
        if (url != null && !url.equals("")) {
            int index = url.lastIndexOf("/");
            if (index != -1) {
                return url.substring(index + 1);
            }
        }
        return "SexyCat.apk";
    }

    public static boolean isEmail(String email) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    public static boolean isMobile(String mobile) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(170))\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();

    }

    private static String getMetaData(String key) {
        try {
            ApplicationInfo ai = EMApplication.getInstance().getPackageManager()
                    .getApplicationInfo(EMApplication.getInstance().getPackageName(),
                            PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getChannelId() {
        return getMetaData("channel");
    }

    public static String getTime(long time) {
        int day = (int) (time / (24 * 60 * 60 * 1000));
        if (day > 0) {
            return day + "天";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            return sdf.format(time);
        }
    }

    public static boolean isContainTopPackageName() {
        String[] packageNames = new String[]{"com.android.sexycat"};
        boolean flag = false;
        if (packageNames != null && packageNames.length > 0) {
            String topStackPackageName = getTopStackPackageName();
            if (topStackPackageName == null) {
                flag = false;
            } else {
                for (int i = 0; i < packageNames.length; i++) {
                    String name = packageNames[i];
                    if (name != null && topStackPackageName.equals(name)) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 获取系统最顶层包名
     *
     * @return
     */
    public static String getTopStackPackageName() {
        List<RunningTaskInfo> tasksInfo = EMApplication.activityManager.getRunningTasks(1);
        if (tasksInfo != null && tasksInfo.size() > 0) {
            return tasksInfo.get(0).topActivity.getPackageName();
        } else {
            return null;
        }
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) EMApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getBrand() {
        return Build.BRAND;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    @SuppressLint("NewApi")
    public static int getRealWidth(Activity activity) {
        if (Build.VERSION.SDK_INT >= 14) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            return point.x;
        } else {
            return DeviceUtils.getScreenWidth();
        }
    }

    @SuppressLint("NewApi")
    public static int getRealHeight(Activity activity) {
        if (Build.VERSION.SDK_INT >= 14) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            return point.y;
        } else {
            return DeviceUtils.getScreenHeight();
        }
    }

    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|^(14[7])|(15[^4,\\D])|(18[0,2,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
}
