package com.zhanglin.signature;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhanglin.signature.doodle.DoodleView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DoodleView doodleView;
    private TextView tvClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        doodleView = findViewById(R.id.doodle_view);
        tvClear = findViewById(R.id.tvClear);
        tvClear.setOnClickListener(this);
        findViewById(R.id.tvPre).setOnClickListener(this);
        initDoodleView();
    }

    private void initDoodleView() {
        doodleView.setZOrderOnTop(true);
        doodleView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        tvClear.measure(width, height);
        doodleView.setPaintOffset(0, tvClear.getMeasuredHeight());
        doodleView.setPaintSize(5);
        doodleView.setPaintColor("#ff0000");
        doodleView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvClear:
                doodleView.clear();
                break;
            case R.id.tvPre:
                doodleView.paintBack();
                break;
        }
    }
}
