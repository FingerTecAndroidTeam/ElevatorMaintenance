package com.fingertec.httpclient;

/**
 * Created by jqr on 2017/1/9.
 */
public class UrlConfig {
    private final static String BASE_URL = "http://192.168.0.142/ElevatorMaintenanceCloudPlatform/index.php/Maintenance/";
    //广告
    private final static String TASKLIST = "Mobile/MissionList";
    private final static String LOGIN = "Mobile/Login";
    private final static String TAKEMISSION = "Mobile/takeMission";
    //
    public static String getTLUrl() {
        StringBuilder str = new StringBuilder(BASE_URL);
        str.append(TASKLIST);
        return str.toString();
    }
    public static String getTakeMissionUrl() {
        StringBuilder str = new StringBuilder(BASE_URL);
        str.append(TAKEMISSION);
        return str.toString();
    }

    public static String getLoginUrl() {
        StringBuilder str = new StringBuilder(BASE_URL);
        str.append(LOGIN);
        return str.toString();
    }
}
