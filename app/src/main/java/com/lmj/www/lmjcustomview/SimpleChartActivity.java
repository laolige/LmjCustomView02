package com.lmj.www.lmjcustomview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SimpleChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_chart);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("aaa","onStart"+getClass());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("aaa","onResume"+getClass());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("aaa","onPause"+getClass());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("aaa","onStop"+getClass());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("aaa","onDestroy"+getClass());
    }
}
