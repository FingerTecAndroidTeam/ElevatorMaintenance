package com.fingertec.widget;

import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fingertec.common.EMApplication;


/**
 * Created by liukai on 2015/5/11.
 */
public class FontTextView extends TextView {
    public FontTextView(Context context) {
        super(context);
        EMApplication.doFont(this);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        EMApplication.doFont(this);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        EMApplication.doFont(this);
    }


    public void setFullHalfText(CharSequence text) {
        this.setText(EMApplication.ToDBC(text));
    }

    public void setFullHalfText(int id) {
        this.setText(EMApplication.ToDBC(getContext().getResources().getText(id).toString()));
    }
}
