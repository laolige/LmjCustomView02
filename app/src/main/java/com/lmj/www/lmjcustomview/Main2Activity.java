package com.lmj.www.lmjcustomview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by limengjie
 * on 2016/10/26.10:18
 */
public class Main2Activity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }
    public void toa(View view){
        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
