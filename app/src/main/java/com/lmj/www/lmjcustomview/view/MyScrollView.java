package com.lmj.www.lmjcustomview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * Created by limengjie
 * on 2016/10/10.15:07
 */

public class MyScrollView extends ScrollView {
    private String Tag = "MyScrollView";
    private Toast ta =null;

    public MyScrollView(Context context) {
        super(context);
        ta = Toast.makeText(getContext(),"",Toast.LENGTH_SHORT);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ta =Toast.makeText(getContext(),"",Toast.LENGTH_SHORT);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ta = Toast.makeText(getContext(),"",Toast.LENGTH_SHORT);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        Log.i(Tag,"deltaY:"+deltaY+",scrollY:"+scrollY+",scrollRangeY:"+scrollRangeY
                +",maxOverScrollY:"+maxOverScrollY+",isTouchEvent:"+isTouchEvent);
        if(scrollY==0&&deltaY<0){
            ta.setText("滑动到顶部");
            ta.show();
        }else if(scrollY==scrollRangeY&&deltaY>0){
            ta.setText("滑动到底部");
            ta.show();
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }




}
