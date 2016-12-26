package com.lmj.www.lmjcustomview;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.lmj.www.lmjcustomview.util.OkHttpUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by limengjie
 * on 2016/10/26.10:30
 */
public class MainActivity extends FragmentActivity {
    private Intent intent = new Intent();
    private int i = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            this.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        doGet();

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("lmj","onKeyDown");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void goActivity(View view) {
        switch (view.getId()) {
            case R.id.button_qq_slide:
                intent.setClass(this, Main2Activity.class);
                break;
            case R.id.button_folder_textview:
                MainActivity.this.finish();
                intent.setClass(this, FolderTextViewActivity.class);
                doPost();
                break;
            case R.id.button_chart:
                intent.setClass(this, SimpleChartActivity.class);
                doPostJson();
                break;
            case R.id.button_circleLyout:
                intent.setClass(this, CircleLayoutActivity.class);
                break;
            case R.id.button_beziser:
                intent.setClass(this, BezierActivity.class);
                break;
        }
       startActivity(intent);
    }

    public void doGet() {
        try {
            OkHttpUtil.getInstance().doGetBackCall("http://192.168.3.171:8080/LmjWeb2/test", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("aaa", "加载失败:");
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "加载失败:", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String str = response.body().string();
                    Log.i("aaa", "加载成功:" + str);

                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();

                                }
                            }
                    );
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doPost() {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", "lmj");
        map.put("pwd", "1234556");
        try {
            OkHttpUtil.getInstance().doPostBackCall(map, "http://192.168.3.171:8080/LmjWeb2/test", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("aaa", "加载失败:");
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "加载失败:", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String str = response.body().string();
                    Log.i("aaa", "加载成功:" + str);
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doPostJson() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", "lmj");
            jsonObject.put("pwd", "1234556");

            OkHttpUtil.getInstance().doPostJsonBackCall(jsonObject.toString(), "http://192.168.3.171:8080/LmjWeb2/test", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("aaa", "加载失败:");
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "加载失败:", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String str = response.body().string();
                    Log.i("aaa", "加载成功:" + str);
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("aaa", "onStart" + getClass());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("aaa", "onResume" + getClass());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("aaa", "onPause" + getClass());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("aaa", "onStop" + getClass());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("aaa", "onDestroy" + getClass());
    }
}
