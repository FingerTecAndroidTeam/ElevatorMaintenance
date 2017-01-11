package com.fingertec.utils;

import android.content.Context;
import android.content.Intent;

import com.fingertec.elevatormaintenance.LoginActivity;
import com.fingertec.elevatormaintenance.MainActivity;

/**
 * Created by jqr on 2017/1/10.
 */
public class ActivityToActivity {
    static public void goMainActivity(Context context) {
        context.startActivity(new Intent(context,MainActivity.class));
    }
    static public void goLoginActivity(Context context) {
        context.startActivity(new Intent(context,LoginActivity.class));
    }
}
