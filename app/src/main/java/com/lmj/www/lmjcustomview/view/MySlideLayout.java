package com.lmj.www.lmjcustomview.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.nineoldandroids.animation.FloatEvaluator;
import com.nineoldandroids.view.ViewHelper;

import static com.lmj.www.lmjcustomview.view.MySlideLayout.State.close;

/**
 * 继承frameLayout，免去子view的测量和布局工作
 * ViewDragHelper的使用学习
 * Created by limengjie
 * on 2016/9/30.9:22
 */
public class MySlideLayout extends FrameLayout {
    private String TAG = "MySlideLayout";
    private final ViewDragHelper viewDragHelper;
    private LinearLayout leftMenu;
    private LinearLayout contentMenu;
    private LinearLayout bgMenu;
    private int groupWidth;
    private int slideWidthRange;//滑动的最大宽度
    private int groupHeight;
    private State status = State.close;
    private int contentLeft = 0;
    private FloatEvaluator fevaluator = new FloatEvaluator();
    private OnMySldeLayoutStatusChangeListener listener;
    private boolean isFirst;

    public MySlideLayout(Context context) {
        this(context, null);
    }

    public MySlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // this，传入当前的viewgroup，1.0f表示灵敏度，越大越灵敏，默认是1.0
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

            //child为当前拖拽的view，如果这个child可以被拖动，需要返回true，这个根据情况自己判断
            @Override
            public boolean tryCaptureView(View child, int pointerId) {


                return true;
            }

            // child为当前拖拽的view,left当前child被拖动到的left坐标，可以根据需要修改返回
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                //修正contentmenu  left的返回值范围 0，range
                if (child == contentMenu) {
                    left = fixLeft(left);
                }

                return left;
            }

            // dx,当前x轴的变化量
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                Log.i(TAG, "onViewPositionChanged");
                //当leftmenu发生拖拽之后，将其放回原位，并且将这个拖动变化的值，传递给contentMenu
                int newLeft = contentMenu.getLeft() + dx;//contentMenu现在要拖动到的位置
                if (leftMenu == changedView) {

                    newLeft = fixLeft(newLeft);
                    leftMenu.layout(0, 0, groupWidth, groupHeight);
                    contentMenu.layout(newLeft, 0, newLeft + groupWidth, groupHeight);
                }
                newLeft = fixLeft(newLeft);
                contentLeft = newLeft;
                // 为了兼容低版本, 每次修改值之后, 进行重绘
                // 因为2.3版本，在 ViewCompat.offsetLeftAndRight后没有重绘，而clampViewPositionHorizontal
                // 是通过这个offsetLeftAndRight方法实现的
                invalidate();
                //计算变化的百分比
                float percent = newLeft * 1.0f / slideWidthRange;
                animView(percent);
            }

            //view被释放
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                Log.i(TAG, "onViewReleased");
                if (contentMenu.getLeft() < slideWidthRange / 2) {
                    close();
                } else {
                    open();
                }
            }

            //在viewdraghelper的事件分发中，如果范围返回0，则在子view中消费事件时候，viewdarghelper
            //无法处理事件了，所有返回实际的一个大于0的范围，viewdraghelper可以在优先处理滑动事件
            @Override
            public int getViewHorizontalDragRange(View child) {
                return slideWidthRange;
            }
        });
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "onLayout");
        if (!isFirst) {
            //方法1
            //    super.onLayout(changed, left, top, right, bottom);
            isFirst = true;
        } else {
            //记录下contentMenu的位置，在华为5.0系统亮屏和黑屏之后，contentMenu会回到原来的位置上
            //所以记录下来，恢复回去，或者用方法1，第一次调用onlayout的时候才调用父类布局，以后就不变
            contentMenu.layout(contentLeft, 0, contentLeft + groupWidth,
                    contentMenu.getTop() + groupHeight);
        }
        Scroller sc = new Scroller(getContext());

    }



    // 执行伴随动画
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void animView(float percent) {
        upDateState(percent);
        //contentMeun，缩放动画  1.0,0.8
        ViewHelper.setScaleX(contentMenu, (1.0f + (0.8f - 1.0f) * percent));
        ViewHelper.setScaleY(contentMenu, (1.0f + (0.8f - 1.0f) * percent));
        //leftMeun，缩放动画 0.5,1.0，平移动画,-width/2,0,透明度，0.5，1
        ViewHelper.setScaleX(leftMenu, (0.5f + (1.0f - 0.5f) * percent));
        ViewHelper.setScaleY(leftMenu, fevaluator.evaluate(percent, 0.5, 1.0));
        ViewHelper.setTranslationX(leftMenu, fevaluator.evaluate(percent, -groupWidth * 1.0f / 2, 0f));


        ViewHelper.setAlpha(leftMenu, fevaluator.evaluate(percent, 0.5, 1.0));
        ViewHelper.setAlpha(bgMenu, fevaluator.evaluate(percent, 0.5, 0));
        // ArgbEvaluator 颜色估值器
//       getBackground().setColorFilter((Integer)evaluateColor(percent, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
    }

    public Object evaluateColor(float fraction, Object startValue, Object endValue) {
        int startInt = ((Integer) startValue).intValue();
        int startA = startInt >> 24;
        int startR = startInt >> 16 & 255;
        int startG = startInt >> 8 & 255;
        int startB = startInt & 255;
        int endInt = ((Integer) endValue).intValue();
        int endA = endInt >> 24;
        int endR = endInt >> 16 & 255;
        int endG = endInt >> 8 & 255;
        int endB = endInt & 255;
        return Integer.valueOf(startA + (int) (fraction * (float) (endA - startA)) << 24 | startR + (int) (fraction * (float) (endR - startR)) << 16 | startG + (int) (fraction * (float) (endG - startG)) << 8 | startB + (int) (fraction * (float) (endB - startB)));
    }

    public void open() {
        if (viewDragHelper.smoothSlideViewTo(contentMenu, slideWidthRange, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }


    }

    public void close() {
        if (viewDragHelper.smoothSlideViewTo(contentMenu, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    //修正left坐标
    public int fixLeft(int left) {
        if (left < 0) {
            return 0;
        } else if (left > slideWidthRange) {
            return slideWidthRange;
        }
        return left;
    }

    // 2、交给helper决定是否要拦截事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    //3、处理所有分发过来的事件，并且传递给helper
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    //在view从xml文件中实例化之后调用可以拿到子view
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bgMenu = (LinearLayout) getChildAt(0);
        leftMenu = (LinearLayout) getChildAt(1);
        contentMenu = (LinearLayout) getChildAt(2);
    }

    //在view的大小发生变化时候调用
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        groupWidth = getMeasuredWidth();
        groupHeight = getMeasuredHeight();
        slideWidthRange = (int) (groupWidth * 0.6);
    }

    enum State {
        open, close, draging
    }

    // 根据变化修改状态
    public void upDateState(float precent) {
        Log.i(TAG, "preStatus:" + status);
        State preStatus = status;
        if (precent == 0f) {
            status = State.close;
            Log.i(TAG, "precent1111111111111111111:" + precent);
        } else if (precent == 1.0f) {
            Log.i(TAG, "precent222222222222222222:" + precent);
            status = State.open;
        } else {
            Log.i(TAG, "precent333333333333333:" + precent);
            status = State.draging;
        }
        Log.i(TAG, "status:" + status);
        if (preStatus != status && null != listener) {
            Log.i(TAG, "precent444444444444444444:" + precent);
            //通知状态更新
            switch (status) {
                case close:
                    listener.close();
                    break;
                case open:
                    listener.opean();
                    break;
            }
        }
    }

    public void setOnMySldeLayoutStatusChangeListener(OnMySldeLayoutStatusChangeListener listener) {
        this.listener = listener;
    }

    public interface OnMySldeLayoutStatusChangeListener {
        void opean();

        void close();
    }

    public State getStatus() {
        return status;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
