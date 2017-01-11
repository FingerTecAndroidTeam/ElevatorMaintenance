package com.fingertec.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fingertec.bean.MissionBean;
import com.fingertec.bean.Status;
import com.fingertec.elevatormaintenance.R;
import com.fingertec.provider.DataServer;
import com.fingertec.transform.GlideCircleTransform;

import java.util.List;

public class MissionAdapter extends BaseQuickAdapter<MissionBean.MissionItem, BaseViewHolder> {
    public MissionAdapter(List<MissionBean.MissionItem> data) {
        super(R.layout.list_item_tweet, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MissionBean.MissionItem item) {
        helper.setText(R.id.main_tv_time, item.getDate())
                .setText(R.id.main_tv_from, item.getRegulatoryName())
                .setText(R.id.main_tv_work, item.getMaintenanceTimes());
        // Glide.with(mContext).load(item.getUserAvatar()).crossFade().placeholder(R.mipmap.def_head).transform(new GlideCircleTransform(mContext)).into((ImageView) helper.getView(R.id.tweetAvatar));

    }


}
