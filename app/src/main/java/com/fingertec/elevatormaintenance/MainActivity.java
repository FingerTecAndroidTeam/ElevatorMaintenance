package com.fingertec.elevatormaintenance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fingertec.adapter.MissionAdapter;
import com.fingertec.bean.Bean;
import com.fingertec.bean.MissionBean;
import com.fingertec.common.Constants;
import com.fingertec.httpclient.Action;
import com.fingertec.httpclient.IHttpGetResultListener;
import com.fingertec.httpclient.UrlConfig;
import com.fingertec.httpclient.request.RequestContent;
import com.fingertec.httpclient.request.Requester;
import com.fingertec.provider.DataServer;
import com.fingertec.utils.PrefUtils;
import com.fingertec.utils.RecycleViewDivider;
import com.fingertec.utils.UserInfomationUtils;
import com.fingertec.utils.statusbar.StatusBarUtil;
import com.fingertec.widget.LoadingBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar_iv_drawer)
    ImageView ivDrawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar_tv_count)
    TextView tvCount;
    @BindView(R.id.main_rv_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.main_sr_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.pb)
    LoadingBar mLoadingBar;
    @BindView(R.id.nav_linear_finished_task)
    LinearLayout linearFinishedTask;
    @BindView(R.id.nav_linear_setting)
    LinearLayout linearSetting;
    @BindView(R.id.nav_tv_name)
    TextView tvName;
    @BindView(R.id.nav_tv_num)
    TextView tvNum;
    private MissionAdapter missionAdapter;
    private ArrayList<MissionBean.MissionItem> missionList = new ArrayList<>();
    private MissionBean missionBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    void initWidget() {
        ivDrawer.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        tvName.setText(PrefUtils.ReadSharedPreferencesString("UserInformation", "name"));
        tvNum.setText(PrefUtils.ReadSharedPreferencesString("UserInformation", "account"));
        linearSetting.setOnClickListener(this);
        linearFinishedTask.setOnClickListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLoadingBar.setReloadListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                httpPost();
            }
        });
        httpPost();
        initAdapter();

    }

    public void httpPost() {
        mHttpReq = new Requester(this);
        requestContent = new RequestContent(UrlConfig.getTLUrl());
        hashMap = requestContent.getParameters();
        hashMap.put(Constants.USER_ID_KEY, UserInfomationUtils.getOpendId());
        mHttpReq.sendPostRequest(Action.LOADMISSIONLIST, requestContent, MissionBean.class);

    }

    private void initAdapter() {
        missionAdapter = new MissionAdapter(missionList);
        missionAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(missionAdapter);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MainActivity.this, MissionDetailActivity.class);
                intent.putExtra(Constants.MISSION_NUMBER_KEY, missionList.get(position).getElevatorID());//任务编号
                intent.putExtra(Constants.ELEVATOR_NUMBER_KEY, missionList.get(position).getElevatorNum());//电梯编号
                intent.putExtra(Constants.REGULATORY_NAME_KEY, missionList.get(position).getRegulatoryName());//物业名称
                intent.putExtra(Constants.LOCATION_KEY, missionList.get(position).getElevatorLocation());//电梯位置
                intent.putExtra(Constants.MAINTENANCE_TIMES_KEY, missionList.get(position).getDate());//维保次数-----------还没有名字
                intent.putExtra(Constants.ELEVATOR_ID_KEY, missionList.get(position).getDate());//电梯Id
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        //missionAdapter.setEnableLoadMore(false);
        httpPost();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public <T> void OnComplete(int action, T bean) {
        switch (action) {
            case Action.NETWORK:
                mLoadingBar.setStatus(LoadingBar.Status.NETWORK_ERR);
                Log.v("NETWORK ", "Error");
                break;
            case Action.ERROR:
                mLoadingBar.setStatus(LoadingBar.Status.FAIL);
                Log.v("Error ", "Error");
                break;
            case Action.LOADMISSIONLIST:
                missionBean = ((MissionBean) bean);
                if (missionBean.ret == 1) {
                    if (missionBean.data.size() == 0) {
                        mLoadingBar.setStatus(LoadingBar.Status.EMPTY);
                        tvCount.setText("0");
                    } else {
                        mLoadingBar.setStatus(LoadingBar.Status.SUCCESS);
                        missionList.clear();
                        missionList.addAll(missionBean.data);
                        tvCount.setText(missionBean.data.size());
                        missionAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(MainActivity.this, missionBean.msg, Toast.LENGTH_SHORT).show();
                }

                break;
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_iv_drawer:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.nav_linear_finished_task:
                break;
            case R.id.nav_linear_setting:
                //startActivity(new Intent(MainActivity.this,LoginActivity.class));
                //drawer.closeDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
