package com.fingertec.utils;


import com.fingertec.common.Constants;

/**
 *
 */
public class UserInfomationUtils {
    public static void setOpendId(String userInfoID) {
        PrefUtils.WriteSharedPreferencesString(Constants.USER_ID_NAME, Constants.USER_ID_KEY, userInfoID);
    }

    public static String getOpendId() {
        if (PrefUtils.ReadSharedPreferencesString(Constants.USER_ID_NAME, Constants.USER_ID_KEY) == null) {
            return "null";
        }
        return PrefUtils.ReadSharedPreferencesString(Constants.USER_ID_NAME, Constants.USER_ID_KEY);
    }

    public static String getUserName() {
        return PrefUtils.ReadSharedPreferencesString(Constants.USER_NAME_NAME, Constants.USER_NAME_KEY);
    }

    public static void setUserName(String name) {
        PrefUtils.WriteSharedPreferencesString(Constants.USER_NAME_NAME, Constants.USER_NAME_KEY, name);
    }

}
