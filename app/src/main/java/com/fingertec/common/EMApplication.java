package com.fingertec.common;

import android.app.ActivityManager;
import android.app.Application;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by jqr on 2016/12/27.
 */
public class EMApplication extends Application {
    private static volatile EMApplication instance = null;
    public static ActivityManager activityManager;
    private static Typeface typeface;

    public static EMApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        typeface = Typeface.createFromAsset(getAssets(), "fonts/yl.otf");//字体
    }

    public static void doFont(EditText textView) {
        textView.setTypeface(typeface);
    }

    public static void doFont(TextView textView) {
        textView.setTypeface(typeface);
    }

    public static void doFont(Button textView) {
        textView.setTypeface(typeface);
    }

    public static void doFont(RadioButton textView) {
        textView.setTypeface(typeface);
    }

    /**
     * 字体全角/半角 转换
     */
    public static String ToDBC(CharSequence input) {
        if (!TextUtils.isEmpty(input)) {
            char[] c = input.toString().toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == 12288) {
                    c[i] = (char) 32;
                    continue;
                }
                if (c[i] > 65280 && c[i] < 65375)
                    c[i] = (char) (c[i] - 65248);
            }
            return new String(c);
        }
        return "";
    }
}
