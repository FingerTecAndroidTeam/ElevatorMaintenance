package com.fingertec.elevatormaintenance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fingertec.widget.TitleBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstStepActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.fstep_btn_begin)
    Button fstep_btn_begin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_step);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.fstep_btn_begin:
                Intent intent = new Intent(FirstStepActivity.this, SecondStepActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    void initWidget() {
        new TitleBuilder(this).setTitleText(getResources().getString(R.string.activity_title_fstep)).setLeftImage(R.mipmap.back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).setBackgroundColor(R.color.transparent);
        fstep_btn_begin.setOnClickListener(this);

    }
}
