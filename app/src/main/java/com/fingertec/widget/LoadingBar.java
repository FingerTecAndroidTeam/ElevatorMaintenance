package com.fingertec.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fingertec.elevatormaintenance.R;


public class LoadingBar extends RelativeLayout {
	public enum Status {
		LOADING, EMPTY, SUCCESS, FAIL, NETWORK_ERR, COMMENT_EMPTY,LIST_EMPTY
	}

    private LinearLayout loadingLayout, reloadLayout, emptyLayout, neterrLayout,listEmptyLayout;
    private ImageView ivLoading;
    private FontTextView tvEmpty, tvCommentEmpty,jzTV,problemTV;
    public Status mStatus = Status.LOADING;
    private Animation rotateAnim;
    public LoadingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.loading_view, this, true);
        jzTV=(FontTextView)findViewById(R.id.loading_text);
        loadingLayout = (LinearLayout) findViewById(R.id.loading);
        ivLoading = (ImageView) findViewById(R.id.loading_icon);
        reloadLayout = (LinearLayout) findViewById(R.id.reload);
        neterrLayout = (LinearLayout) findViewById(R.id.net);
        emptyLayout = (LinearLayout) findViewById(R.id.empty);
        tvEmpty = (FontTextView) findViewById(R.id.tv_empty);
        tvCommentEmpty = (FontTextView) findViewById(R.id.comment_empty);
        problemTV=(FontTextView)findViewById(R.id.problem);
        listEmptyLayout= (LinearLayout) findViewById(R.id.list_empty_ll);
        rotateAnim = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotate);
        ivLoading.startAnimation(rotateAnim);
    }

    public void setEmptyIcon(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvEmpty.setCompoundDrawables(null, drawable, null, null);
    }

    public void setEmptyText(int resId) {
        tvEmpty.setFullHalfText(resId);
    }

    public void setStatus(Status status){
        switch (status) {
            case LOADING:
                mStatus = Status.LOADING;
                loadingLayout.setVisibility(View.VISIBLE);
                reloadLayout.setVisibility(View.GONE);
                neterrLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                tvCommentEmpty.setVisibility(View.GONE);
                listEmptyLayout.setVisibility(View.GONE);
                setVisibility(View.VISIBLE);
                ivLoading.startAnimation(rotateAnim);
                break;

            case EMPTY:
                mStatus = Status.EMPTY;
                emptyLayout.setVisibility(View.VISIBLE);
                reloadLayout.setVisibility(View.GONE);
                neterrLayout.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.GONE);
                tvCommentEmpty.setVisibility(View.GONE);
                listEmptyLayout.setVisibility(View.GONE);
                setVisibility(View.VISIBLE);
                ivLoading.clearAnimation();
                break;

            case SUCCESS:
                mStatus = Status.SUCCESS;
                setVisibility(View.GONE);
                ivLoading.clearAnimation();
                break;

            case FAIL:
                mStatus = Status.FAIL;
                reloadLayout.setVisibility(View.VISIBLE);
                loadingLayout.setVisibility(View.GONE);
                neterrLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                tvCommentEmpty.setVisibility(View.GONE);
                listEmptyLayout.setVisibility(View.GONE);
                setVisibility(View.VISIBLE);
                ivLoading.clearAnimation();
                break;

            case NETWORK_ERR:
                mStatus = Status.NETWORK_ERR;
                neterrLayout.setVisibility(View.VISIBLE);
                reloadLayout.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                tvCommentEmpty.setVisibility(View.GONE);
                listEmptyLayout.setVisibility(View.GONE);
                setVisibility(View.VISIBLE);
                ivLoading.clearAnimation();
                break;

            case COMMENT_EMPTY:
                mStatus = Status.COMMENT_EMPTY;
                tvCommentEmpty.setVisibility(View.VISIBLE);
                neterrLayout.setVisibility(View.GONE);
                reloadLayout.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                listEmptyLayout.setVisibility(View.GONE);
                setVisibility(View.VISIBLE);
                ivLoading.clearAnimation();
                break;
            case LIST_EMPTY:
                mStatus = Status.LIST_EMPTY;
                listEmptyLayout.setVisibility(View.VISIBLE);
                tvCommentEmpty.setVisibility(View.GONE);
                neterrLayout.setVisibility(View.GONE);
                reloadLayout.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                setVisibility(View.VISIBLE);
                ivLoading.clearAnimation();
                break;
            default:
                break;
        }
    }

    public void setReloadListener(final OnClickListener listener){
        OnClickListener reloadListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
              /*  if (mStatus != Status.FAIL && mStatus != Status.NETWORK_ERR) {
                    return;
                }*/
                setStatus(Status.LOADING);
                if (listener != null){
                    listener.onClick(v);
                }
            }
        };
        reloadLayout.setOnClickListener(reloadListener);
        neterrLayout.setOnClickListener(reloadListener);
    }
}
