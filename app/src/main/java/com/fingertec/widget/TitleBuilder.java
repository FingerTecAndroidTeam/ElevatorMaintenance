package com.fingertec.widget;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.fingertec.elevatormaintenance.R;


/**
 * 初始化、设置自定义actionbar的各控件 Created by tangao on 2016/7/18.
 */
public class TitleBuilder {
    private Context context = null;
    private View viewContext = null;
    private View viewTitle;
    private TextView tvTitle;
    private ImageView ivLeft;
    private TextView tvRight;

    // activity的上下文参数构造方法
    public TitleBuilder(Activity context) {
        this.context = context;
        viewTitle = context.findViewById(R.id.custom_actionbar_rl);
        tvTitle = (TextView) viewTitle
                .findViewById(R.id.custom_actionbar_title);
        ivLeft = (ImageView) viewTitle
                .findViewById(R.id.custom_actionbar_return_img);
        tvRight = (TextView) viewTitle
                .findViewById(R.id.custom_actionbar_tv_right);
    }

    // fragment等view对象参数构造方法
    public TitleBuilder(View context) {
        this.viewContext = context;
        viewTitle = context.findViewById(R.id.custom_actionbar_rl);
        tvTitle = (TextView) viewTitle
                .findViewById(R.id.custom_actionbar_title);
        ivLeft = (ImageView) viewTitle
                .findViewById(R.id.custom_actionbar_return_img);
        tvRight = (TextView) viewTitle
                .findViewById(R.id.custom_actionbar_tv_right);
    }

    /**
     * 设置标题
     *
     * @param text
     * @return
     */
    public TitleBuilder setTitleText(String text) {
        tvTitle.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        tvTitle.setText(text);
        return this;
    }

    /**
     * 设置标题
     *
     * @param resid
     * @return
     */
    public TitleBuilder setTitleText(int resid) {
        CharSequence title = null;
        if (context != null) {
            title = context.getResources().getText(resid);
        } else if (viewContext != null) {
            title = viewContext.getResources().getText(resid);
        }
        tvTitle.setVisibility(TextUtils.isEmpty(title) ? View.GONE
                : View.VISIBLE);
        tvTitle.setText(title);
        return this;
    }

    /**
     * 设置 自定义actionbar的左侧图片
     *
     * @param resId
     * @return
     */
    public TitleBuilder setLeftImage(int resId) {
        ivLeft.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        ivLeft.setImageResource(resId);
        return this;
    }

    /**
     * 给actionbar的左侧图片添加监听事件
     *
     * @param listener
     * @return
     */
    public TitleBuilder setLeftOnClickListener(View.OnClickListener listener) {
        if (ivLeft.getVisibility() != View.VISIBLE) {
            // System.err.println("ActionBar左侧图片未设置，不能监听！");
            return null;
        }
        ivLeft.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置actionbar的右侧文字
     *
     * @param text
     * @return
     */
    public TitleBuilder setRightText(String text) {
        tvRight.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        tvRight.setText(text);
        return this;
    }

    // 重载
    public TitleBuilder setRightText(int resid) {
        CharSequence rightText = null;
        if (context != null) {
            rightText = context.getResources().getText(resid);
        } else if (viewContext != null) {
            rightText = viewContext.getResources().getText(resid);
        }
        tvRight.setVisibility(TextUtils.isEmpty(rightText) ? View.GONE
                : View.VISIBLE);
        tvRight.setText(rightText);
        return this;
    }

    //设置右边textView的背景图片
    public TitleBuilder setRightBgPic(int resid) {
        if (resid == 0) {
            return this;
        }
        tvRight.setVisibility(View.VISIBLE);
        LayoutParams params = (LayoutParams) tvRight.getLayoutParams();
        params.setMargins(0, 0, 1, 0);
        params.height = 100;
        params.width = 100;
        tvRight.setLayoutParams(params);
        tvRight.setBackgroundResource(resid);
        return this;
    }

    public TitleBuilder setBackgroundColor(int resid) {
        if (resid == 0) {
            return this;
        }
        viewTitle.setBackgroundColor(context.getResources().getColor(resid));
        return this;
    }

    /**
     * 设置
     *
     * @param listener
     * @return
     */
    public TitleBuilder setRightOnClickListener(View.OnClickListener listener) {
        if (tvRight.getVisibility() != View.VISIBLE) {
            return this;
        }
        tvRight.setOnClickListener(listener);
        return this;
    }

    public View build() {
        return viewTitle;
    }

}
