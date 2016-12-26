package com.lmj.www.lmjcustomview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lmj.www.lmjcustomview.view.BezierPath;

public class BezierActivity extends AppCompatActivity {

    private BezierPath bezierPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
      bezierPath = (BezierPath) findViewById(R.id.bpath);
        bezierPath.setListener(new BezierPath.BezierPathListener() {
            @Override
            public void onAnimationEnd() {
                Toast.makeText(BezierActivity.this,"动画结束",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void startAni(View view){
        bezierPath.startAnimation();
    }
}
