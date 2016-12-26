package com.lmj.www.lmjcustomview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lmj.www.lmjcustomview.util.Cheeses;
import com.lmj.www.lmjcustomview.view.MyListView;
import com.lmj.www.lmjcustomview.view.MySlideLayout;
import com.lmj.www.lmjcustomview.view.MySlideLineLayout;

public class QQslideMenuActivity extends Activity {
    private MySlideLayout mySlideLayout;
    private MySlideLineLayout mySlideLineLayout;
    private MyListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qqslide_menu);
        mySlideLayout = (MySlideLayout) findViewById(R.id.myslidelayout);
        mySlideLineLayout = (MySlideLineLayout) findViewById(R.id.myslidelayout_contentmenu);
        lv = (MyListView) findViewById(R.id.lv);
        //去掉滑动到边界时候再往下滑动出现的圆弧效果
        lv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Cheeses.NAMES));
        init();
    }
    public void init(){
        mySlideLineLayout.setMySlideLayout(mySlideLayout);
        mySlideLayout.setOnMySldeLayoutStatusChangeListener(new MySlideLayout.OnMySldeLayoutStatusChangeListener(){

            @Override
            public void opean() {
                Toast.makeText(QQslideMenuActivity.this,"opean",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void close() {
                Toast.makeText(QQslideMenuActivity.this,"close",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
