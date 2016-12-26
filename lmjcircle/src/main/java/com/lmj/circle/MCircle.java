package com.lmj.circle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lmj on 2016/11/15 0015. on 下午 7:54
 * limengjie
 */
public class MCircle extends View {
    private int mHeight = 0;//控件的高度
    private int mWidth = 0;//控件的宽度
    private String[] mIndexStr = {"五杀能力", "中单能力", "打野能力", "协作能力", "带崩能力"};
    private int defaultUnit = 30;//默认单位间隔
    private int defaultMaxVlue = 100;//默认最大的值
    private int firstRadius = 50;
    private int[] initValue = {2, 0, 3, 1, 0};
    private Paint rectPain;
    private Paint textPain;
    private Paint solidPain;
    private Context context;
    public MCircle(Context context) {
        this(context,null);
    }

    public MCircle(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,R.styleable.MCircle,defStyleAttr,0);
        for(int i=0;i<ta.getIndexCount();i++){
            int id = ta.getIndex(i);
//            switch(id){
//                case R.styleable.MCircle_FirstR:
//
//                    break;
//
//            }

        }

        rectPain = new Paint();
        rectPain.setAntiAlias(true);
        rectPain.setStyle(Paint.Style.STROKE);
        rectPain.setStrokeWidth(3);
        rectPain.setColor(Color.BLACK);
        textPain = new Paint();
        textPain.setAntiAlias(true);
        textPain.setTextSize(40);
        textPain.setColor(Color.GRAY);
        solidPain = new Paint();
        solidPain.setAntiAlias(true);
        solidPain.setStyle(Paint.Style.FILL);
        solidPain.setColor(0x800000ff);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //将画布坐标系移动到view的中心
        canvas.translate(mWidth / 2, mHeight / 2);
        drawRect(canvas);
    }

    /*
        绘制多边形
         */
    private void drawRect(Canvas canvas) {
        Path path_rect = new Path();
        Path path_line = new Path();
        Path path_sloid = new Path();
        for (int i = 0; i < mIndexStr.length; i++) {
            int radus = firstRadius+i*defaultUnit;

            for (int j = 0; j < mIndexStr.length; j++) {
                int angle = j*360/mIndexStr.length+72-90;
                double radain = Math.PI*angle/180;
                float x = (float) (Math.cos(radain)*radus);
                float y = (float)(Math.sin(radain)*radus);
                if (j == 0) {
                    path_rect.moveTo(x, y);
                } else {
                    path_rect.lineTo(x,y);
                }
                if(i==mIndexStr.length-1){
                    //最后一个多边形，画上中心与顶点的连线
                    path_line.lineTo(x,y);
                    canvas.drawPath(path_line,rectPain);
                    path_line.reset();

                    //绘制文字
                    Rect rect = new Rect();
                    textPain.getTextBounds(mIndexStr[j],0,mIndexStr[j].length(),rect);
                    if(x<0){
                        x=x-rect.width()-20;
                    }else if(x==0){
                         x=x-rect.width()/2;
                    }else{
                        x+=20;
                    }
                    canvas.drawText(mIndexStr[j],x,y,textPain);
                    //
                    int radus2 = firstRadius+initValue[j]*defaultUnit;
                    float x2 = (float) (Math.cos(radain)*radus2);
                    float y2 = (float)(Math.sin(radain)*radus2);
                    if(j==0){
                        path_sloid.moveTo(x2,y2);
                    }else{
                        path_sloid.lineTo(x2,y2);
                    }

                }
            }
            path_rect.close();
            canvas.drawPath(path_rect,rectPain);
            path_rect.reset();



        }
        path_sloid.close();
        canvas.drawPath(path_sloid,solidPain);


    }

}
