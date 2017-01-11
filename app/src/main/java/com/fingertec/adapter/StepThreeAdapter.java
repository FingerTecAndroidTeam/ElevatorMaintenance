package com.fingertec.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fingertec.bean.Status;
import com.fingertec.elevatormaintenance.R;
import com.fingertec.provider.DataServer;

public class StepThreeAdapter extends BaseQuickAdapter<Status, BaseViewHolder> {


    public StepThreeAdapter(int dataSize) {
        super(R.layout.tstep_item, DataServer.getSampleData(dataSize));
    }

    @Override
    protected void convert(BaseViewHolder helper, Status item) {
    /*    helper.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setVisible(R.id.tweetRT, item.isRetweet())
                .addOnClickListener(R.id.tweetAvatar)
                .addOnClickListener(R.id.tweetName)
                .linkify(R.id.tweetText);

        Glide.with(mContext).load(item.getUserAvatar()).crossFade().placeholder(R.mipmap.def_head).transform(new GlideCircleTransform(mContext)).into((ImageView) helper.getView(R.id.tweetAvatar));
 */
    }


}
