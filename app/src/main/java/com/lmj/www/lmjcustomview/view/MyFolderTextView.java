package com.lmj.www.lmjcustomview.view;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by limengjie
 * on 2016/10/9.17:30
 */
public class MyFolderTextView extends TextView {
    private String TAG = "MyFolderTextView";
    public int lineHeight7;
    private int originHeight;
    private ValueAnimator vAnimator;
    private String contentText;


    public MyFolderTextView(Context context) {
        this(context,null);

    }

    public MyFolderTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public MyFolderTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


    }
    public void init(final    String contentparam,int lines){
        //判断是否超多7行，多了就设置为7行
        contentText = "";
        if(!TextUtils.isEmpty(contentparam)){
            this.setText(contentparam);
            contentText = contentparam;
        }else{
            contentText = (String) this.getText();
        }
        if(lines<=0){
            lines=7;
        }
        TextView textView = new TextView(getContext());

        textView.setLines(lines);
        textView.measure(MeasureSpec.makeMeasureSpec(100000,MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(100000,MeasureSpec.AT_MOST));
        lineHeight7= textView.getMeasuredHeight();

        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(originHeight<=0){
                    TextView textView2 = new TextView(getContext());
                    textView2.setText(contentText);
                    textView2.measure(MeasureSpec.makeMeasureSpec(MyFolderTextView.this.getMeasuredWidth()
                            ,MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(100000,MeasureSpec.AT_MOST));
                    originHeight= textView2.getMeasuredHeight();
                    MyFolderTextView.this.getLayoutParams().height =Math.min(lineHeight7,originHeight);
                    MyFolderTextView.this.postInvalidate();

                    Log.i(TAG,"originHeight:"+originHeight);
                }
            }
        });

        Log.i(TAG,"height:"+lineHeight7);
    }

    /**
     * 必须初始化这个方法
     * @param content 如果传入空，则表示xml中的内容
     */
    public void initView(String content){
        init(content,0);

    }

    /**
     *
     * @param content  content 如果传入空，则表示xml中的内容
     * @param maxlines lines 传入小于0的就会默认7行
     */
    public void initView(int maxlines,String content){
        init(content,maxlines);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


    }

    /**
     * 是否可以折叠
     * @return
     */
    public boolean isCanFold(){
        return originHeight>lineHeight7;
    }

    public void open(){
        if(null!=vAnimator){
            if(vAnimator.isRunning()){
                vAnimator.end();
            }
        }else{
            vAnimator = new ValueAnimator();
        }

        vAnimator.setIntValues(lineHeight7,originHeight);
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction=  animation.getAnimatedFraction();
                int height=    evaluate(fraction,lineHeight7,originHeight);
                MyFolderTextView.this.getLayoutParams().height=height;
                MyFolderTextView.this.setLayoutParams(MyFolderTextView.this.getLayoutParams());
               Log.i(TAG,"ValueAnimator-height:"+height+",fraction:"+fraction);
            }
        });
        vAnimator.start();
    }
    public void close(){
        if(null!=vAnimator){
            if(vAnimator.isRunning()){
                vAnimator.end();
            }
        }else{
            vAnimator = new ValueAnimator();
        }

        vAnimator.setIntValues(originHeight,lineHeight7);
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction=  animation.getAnimatedFraction();
                int height=    evaluate(fraction,originHeight,lineHeight7);
                MyFolderTextView.this.getLayoutParams().height=height;
                MyFolderTextView.this.setLayoutParams(MyFolderTextView.this.getLayoutParams());
                Log.i(TAG,"ValueAnimator-height:"+height+",fraction:"+fraction);
            }
        });
        vAnimator.start();
    }
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int)(startInt + fraction * (endValue - startInt));
    }

}
