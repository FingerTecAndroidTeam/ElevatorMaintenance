package com.fingertec.elevatormaintenance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fingertec.bean.Bean;
import com.fingertec.bean.MissionBean;
import com.fingertec.common.Constants;
import com.fingertec.httpclient.Action;
import com.fingertec.httpclient.UrlConfig;
import com.fingertec.httpclient.request.RequestContent;
import com.fingertec.httpclient.request.Requester;
import com.fingertec.utils.statusbar.StatusBarUtil;
import com.fingertec.widget.LoadingBar;
import com.fingertec.widget.TitleBuilder;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MissionDetailActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.mission_tv_mission_no)
    TextView tvMissionNo;
    @BindView(R.id.mission_tv_lift_no)
    TextView tvLiftNo;
    @BindView(R.id.mission_tv_property)
    TextView tvProperty;
    @BindView(R.id.mission_tv_position)
    TextView tvPosition;
    @BindView(R.id.mission_tv_date)
    TextView tvDate;
    @BindView(R.id.mission_btn_begin)
    Button btnBegin;
    private String missionNum, elevatorNum, regulatoryName, location, maintenanceDate, maintenanceTimes, elevatorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_detail);
        initData();
    }

    private void initData() {
        missionNum = getIntent().getStringExtra(Constants.MISSION_NUMBER_KEY);//任务编号
        elevatorNum = getIntent().getStringExtra(Constants.ELEVATOR_NUMBER_KEY);//电梯编号
        regulatoryName = getIntent().getStringExtra(Constants.REGULATORY_NAME_KEY);//物业名称
        location = getIntent().getStringExtra(Constants.LOCATION_KEY);//电梯位置
        maintenanceDate = getIntent().getStringExtra(Constants.MAINTENANCE_DATE_KEY);//规定维保日期
        maintenanceTimes = getIntent().getStringExtra(Constants.MAINTENANCE_TIMES_KEY);//维保次数
        elevatorId = getIntent().getStringExtra(Constants.ELEVATOR_ID_KEY);//电梯Id
        tvMissionNo.setText(missionNum);
        tvLiftNo.setText(elevatorNum);
        tvProperty.setText(regulatoryName);
        tvPosition.setText(location);
        tvDate.setText(maintenanceDate);
    }


    @Override
    void initWidget() {
        new TitleBuilder(this).setTitleText(getResources().getString(R.string.activity_title_mission_detail)).setLeftImage(R.mipmap.back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        btnBegin.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.mission_btn_begin:
                mHttpReq = new Requester(this);
                requestContent = new RequestContent(UrlConfig.getTakeMissionUrl());
                hashMap = requestContent.getParameters();
                hashMap.put(Constants.MAINTENANCE_TIMES_KEY, maintenanceTimes);
                hashMap.put(Constants.ELEVATOR_ID_KEY, elevatorId);
                hashMap.put(Constants.USER_ID_KEY, Constants.USER_ID_NAME);
                mHttpReq.sendPostRequest(Action.TAKEMISSION, requestContent, Bean.class);
                btnBegin.setEnabled(false);
                break;
            default:
                break;
        }
    }

    @Override
    public <T> void OnComplete(int action, T bean) {
        Bean mBean = (Bean) bean;
        switch (action) {
            case Action.NETWORK:
                Toast.makeText(MissionDetailActivity.this, mBean.msg, Toast.LENGTH_SHORT);
                break;
            case Action.ERROR:
                Toast.makeText(MissionDetailActivity.this, mBean.msg, Toast.LENGTH_SHORT);
                break;
            case Action.TAKEMISSION:

                if (mBean.errorcode == Action.FAIL) {
                    Toast.makeText(MissionDetailActivity.this, mBean.msg, Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(MissionDetailActivity.this, mBean.msg, Toast.LENGTH_SHORT);
                    Intent intent = new Intent(MissionDetailActivity.this, FirstStepActivity.class);
                    startActivity(intent);
                }
                break;
        }
        btnBegin.setEnabled(true);

    }
}
