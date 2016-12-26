package com.lmj.www.lmjcustomview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by limengjie
 * on 2016/9/30.14:44
 */
public class MySlideLineLayout extends LinearLayout {

    private MySlideLayout mySlideLayout;
    public MySlideLineLayout(Context context) {
        super(context);
    }

    public MySlideLineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setMySlideLayout(MySlideLayout mySlideLayout){
        this.mySlideLayout = mySlideLayout;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if(mySlideLayout.getStatus()== MySlideLayout.State.close){
            return super.onInterceptTouchEvent(ev);
        }else{
            return true;//如果没有在关闭状态，这个时候拦截所有动作，注意，在viewdraghelper拖动的
            //时候，drag已经拦截了，这里不会进来，所以不存在影响，只有在drag分发下来的时候才有用
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction()==MotionEvent.ACTION_UP){
            if(mySlideLayout.getStatus()!= MySlideLayout.State.close){
                mySlideLayout.close();
                return true;//消费这个手指抬起事件
            }
        }
        return super.onTouchEvent(event);
    }
}
