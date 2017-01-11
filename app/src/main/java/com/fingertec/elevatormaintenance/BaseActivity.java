package com.fingertec.elevatormaintenance;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fingertec.httpclient.IHttpReqListener;
import com.fingertec.httpclient.request.RequestContent;
import com.fingertec.httpclient.request.Requester;
import com.fingertec.utils.statusbar.StatusBarUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by jqr on 2016/12/26.
 */
public abstract class BaseActivity extends AppCompatActivity implements IHttpReqListener {
    protected Requester mHttpReq;
    protected RequestContent requestContent;
    protected HashMap<String, String> hashMap;
    private boolean isSleep = true;
    protected Context mContext;
    private ProgressDialog waitDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        Log.v("BaseActivity","onCreate");
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
        ButterKnife.bind(this);
        initWidget();
    }

    abstract void initWidget();

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClassName());//友盟统计 ，我没测试
        MobclickAgent.onResume(mContext);
//        SexyCatInterface.init(this);
//        if (!getClassName().equals("MainActivity")) {
//            LogUtils.d(LogUtils.TAG, "onResume : " + getClassName());
//            SexyCatInterface.sendActionDelay("begin", getClassName(), "-", "-", null);
//        }
//        if (!isSleep) {
//            SexyCatInterface.sendEventDelay("resume");
//        }
        isSleep = isAppOnForeground();
    }

    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    protected String getClassName() {
        return getClass().getSimpleName();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClassName());
        MobclickAgent.onPause(mContext);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isSleep = isAppOnForeground();
//        if (!isSleep) {
//            SexyCatInterface.sendEventDelay("sleep");
//        }
//        if (!getClassName().equals("MainActivity")) {
//            LogUtils.d(LogUtils.TAG, "onStop : " + getClassName());
//            SexyCatInterface.sendActionDelay("end", getClassName(), "-", "-", null);
//        }
    }
    public void showWaitDialog(String msg) {
        if (waitDialog == null) {
            waitDialog = new ProgressDialog(this);
            waitDialog.setMessage(msg);
            waitDialog.setIndeterminate(true);
            waitDialog.setCancelable(false);
        }
        waitDialog.show();
    }

    public void dismissWaitDialog() {
        if (waitDialog != null) {
            waitDialog.dismiss();
            waitDialog = null;
        }
    }

    @Override
    public <T> void OnComplete(int action, T bean) {

    }
}
