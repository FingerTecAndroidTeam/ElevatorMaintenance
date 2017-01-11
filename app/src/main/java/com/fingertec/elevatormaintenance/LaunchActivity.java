package com.fingertec.elevatormaintenance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.fingertec.bean.Bean;
import com.fingertec.bean.UserBean;
import com.fingertec.common.Constants;
import com.fingertec.httpclient.Action;
import com.fingertec.httpclient.UrlConfig;
import com.fingertec.httpclient.request.RequestContent;
import com.fingertec.httpclient.request.Requester;
import com.fingertec.utils.ActivityToActivity;
import com.fingertec.utils.MD5Util;
import com.fingertec.utils.PrefUtils;
import com.fingertec.utils.UserInfomationUtils;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        httpPost();
    }

    private void httpPost() {
        if (TextUtils.isEmpty(UserInfomationUtils.getOpendId())) {
            ActivityToActivity.goLoginActivity(this);
            finish();
        } else {
            String account = PrefUtils.ReadSharedPreferencesString("UserInformation", "account");
            String pwd = PrefUtils.ReadSharedPreferencesString("UserInformation", "pwd");
            if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd)) {
                mHttpReq = new Requester(this);
                requestContent = new RequestContent(UrlConfig.getLoginUrl());
                hashMap = requestContent.getParameters();
                hashMap.put(Constants.ACCOUNT_KEY, account);
                hashMap.put(Constants.PASSWORD_KEY, pwd);
                mHttpReq.sendPostRequest(Action.LOGIN, requestContent, UserBean.class);
            } else {
                ActivityToActivity.goLoginActivity(this);
                finish();
            }

        }

    }


    @Override
    public <T> void OnComplete(int action, T bean) {
        UserBean mBean = (UserBean) bean;
        switch (action) {
            case Action.NETWORK:
                // Toast.makeText(LaunchActivity.this, mBean.msg, Toast.LENGTH_SHORT).show();
                ActivityToActivity.goLoginActivity(this);
                Log.v("NETWORK ", "Error");
                break;
            case Action.ERROR:
                //Toast.makeText(LaunchActivity.this, mBean.msg, Toast.LENGTH_SHORT).show();
                ActivityToActivity.goLoginActivity(this);
                Log.v("Error ", "Error");
                break;
            case Action.LOGIN:
                //解析数据
                if (((UserBean) bean).ret == 1) {
                    UserInfomationUtils.setOpendId(mBean.retdata.getUser_id());//写入user_id
                    PrefUtils.WriteSharedPreferencesString("UserInformation", "name", mBean.retdata.getUser_name());
                    PrefUtils.WriteSharedPreferencesString("UserInformation", "account", mBean.retdata.getUser_password());
                    PrefUtils.WriteSharedPreferencesString("UserInformation", "pwd", mBean.retdata.getUser_password());
                    PrefUtils.WriteSharedPreferencesString("UserInformation", "phone", mBean.retdata.getUser_contact());
                    ActivityToActivity.goMainActivity(this);
                } else {
                    ActivityToActivity.goLoginActivity(this);
                }
                break;
        }
        finish();
    }

    @Override
    void initWidget() {

    }
}
