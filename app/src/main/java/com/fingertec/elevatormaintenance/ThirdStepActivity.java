package com.fingertec.elevatormaintenance;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fingertec.adapter.StepThreeAdapter;
import com.fingertec.provider.DataServer;
import com.fingertec.utils.RecycleViewDivider;
import com.fingertec.widget.TitleBuilder;

import butterknife.BindView;

public class ThirdStepActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.tstep_sr_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tstep_rv_list)
    RecyclerView mRecyclerView;

    private StepThreeAdapter mStepThreeAdapter;
    private static final int TOTAL_COUNTER = 18;
    private static final int PAGE_SIZE = 6;
    private int delayMillis = 1000;
    private int mCurrentCounter = 0;
    private boolean isErr;
    private boolean mLoadMoreEndGone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_step);
    }

    @Override
    void initWidget() {
        new TitleBuilder(this).setTitleText(getResources().getString(R.string.activity_title_tstep))
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).setBackgroundColor(R.color.transparent);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
    }

    private void initAdapter() {
        mStepThreeAdapter = new StepThreeAdapter(PAGE_SIZE);
        mStepThreeAdapter.setOnLoadMoreListener(this);
        mStepThreeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
//        mQuickAdapter.setAutoLoadMoreSize(3);
        mRecyclerView.setAdapter(mStepThreeAdapter);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));
        mCurrentCounter = mStepThreeAdapter.getData().size();

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(ThirdStepActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        mStepThreeAdapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载第一页数据的时候一定要用setNewData，否则不会调用onLoadMoreRequested
                mStepThreeAdapter.setNewData(DataServer.getSampleData(PAGE_SIZE));
                isErr = false;
                mCurrentCounter = PAGE_SIZE;
                mSwipeRefreshLayout.setRefreshing(false);
                mStepThreeAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);
    }

    /**
     * 在onLoadMoreRequested中添加数据要在mRecyclerView.post的Runnable中执行
     */
    @Override
    public void onLoadMoreRequested() {
        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
//                    mQuickAdapter.loadMoreEnd();//default visible
                    mStepThreeAdapter.loadMoreEnd(mLoadMoreEndGone);//true is gone,false is visible没有更多数据
                } else {
                    if (isErr) {
                        mStepThreeAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
                        mCurrentCounter = mStepThreeAdapter.getData().size();
                        mStepThreeAdapter.loadMoreComplete();//加载完成
                    } else {
                        isErr = true;
                        Toast.makeText(ThirdStepActivity.this, "网络错误!", Toast.LENGTH_LONG).show();
                        mStepThreeAdapter.loadMoreFail();//加载失败
                    }
                }
                mSwipeRefreshLayout.setEnabled(true);
            }

        }, delayMillis);
    }
}
