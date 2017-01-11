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

public class SecondStepActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.sstep_btn_begin)
    Button sstep_btn_begin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

    }

    @Override
    void initWidget() {
        new TitleBuilder(this).setTitleText(getResources().getString(R.string.activity_title_sstep)).setLeftImage(R.mipmap.back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).setBackgroundColor(R.color.transparent);
        sstep_btn_begin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.sstep_btn_begin:
                Intent intent = new Intent(SecondStepActivity.this, ThirdStepActivity.class);
                startActivity(intent);
                break;
        }
    }
}
