package com.fingertec.elevatormaintenance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fingertec.bean.Bean;
import com.fingertec.bean.MissionBean;
import com.fingertec.bean.User;
import com.fingertec.bean.UserBean;
import com.fingertec.common.Constants;
import com.fingertec.common.EMApplication;
import com.fingertec.httpclient.Action;
import com.fingertec.httpclient.UrlConfig;
import com.fingertec.httpclient.request.RequestContent;
import com.fingertec.httpclient.request.Requester;
import com.fingertec.utils.DeviceUtils;
import com.fingertec.utils.MD5Util;
import com.fingertec.utils.PrefUtils;
import com.fingertec.utils.UserInfomationUtils;
import com.fingertec.widget.LoadingBar;
import com.fingertec.widget.TitleBuilder;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    @BindView(R.id.login_activity_et_username)
    EditText etUsername;
    @BindView(R.id.login_activity_et_password)
    EditText etPassword;
    @BindView(R.id.login_activity_btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    void initWidget() {
        new TitleBuilder(this).setTitleText(getResources().getString(R.string.activity_login));
        etUsername.setOnEditorActionListener((TextView.OnEditorActionListener) this);
        etPassword.setOnEditorActionListener((TextView.OnEditorActionListener) this);
        EMApplication.doFont(etUsername);
        EMApplication.doFont(etPassword);
        EMApplication.doFont(btnLogin);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_activity_btn_login:
                if (TextUtils.isEmpty(etUsername.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "请输入账户", Toast.LENGTH_SHORT).show();
                    return;
                }
               /* if (!DeviceUtils.isMobileNum(etUsername.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                showWaitDialog("请稍候...");
                httpPost();

                break;
        }
    }

    //0123456 123456
    private void httpPost() {
        btnLogin.setEnabled(false);
        mHttpReq = new Requester(this);
        requestContent = new RequestContent(UrlConfig.getLoginUrl());
        hashMap = requestContent.getParameters();
        hashMap.put(Constants.ACCOUNT_KEY, etUsername.getText().toString());
        hashMap.put(Constants.PASSWORD_KEY, etPassword.getText().toString());
        mHttpReq.sendPostRequest(Action.LOGIN, requestContent, UserBean.class);
    }

    @Override
    public <T> void OnComplete(int action, T bean) {
        UserBean mBean = (UserBean) bean;
        btnLogin.setEnabled(true);
        switch (action) {
            case Action.NETWORK:
                Toast.makeText(LoginActivity.this, mBean.msg, Toast.LENGTH_SHORT).show();
                Log.v("NETWORK ", "Error");
                break;
            case Action.ERROR:
                Toast.makeText(LoginActivity.this, mBean.msg, Toast.LENGTH_SHORT).show();
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

                    Log.d("LoginActivity", PrefUtils.ReadSharedPreferencesString("UserInformation", "name"));
                    Log.d("LoginActivity", PrefUtils.ReadSharedPreferencesString("UserInformation", "account"));
                    Log.d("LoginActivity", PrefUtils.ReadSharedPreferencesString("UserInformation", "pwd"));
                    Log.d("LoginActivity", PrefUtils.ReadSharedPreferencesString("UserInformation", "phone"));

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    this.finish();
                    //Toast.makeText(LoginActivity.this, mBean.msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, mBean.msg, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        dismissWaitDialog();

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        switch (i) {

            case EditorInfo.IME_ACTION_SEND:
                httpPost();
                break;

            default:
                break;
        }

        return false;
    }
}
