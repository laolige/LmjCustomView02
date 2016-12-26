package com.lmj.www.lmjcustomview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;

import java.util.Random;


/**
 * Created by aus on 2016/11/17.
 */

public class MyChartView extends View {
    private Paint paint_Chart;
    private Paint paint_Hstr;
    private Paint patin_Vstr;
    private int strokeWidth = 5;
    private int textSize = 30;
    private int vTextColor = 0xccc;
    private int hTextColor  = 0x00f;
    private String[] hStrs = {"11月17","11月18","11月19","11月20","11月21","11月22"};
    private int maxValue = 100;
    private int valueUnit = 10;

    private Context context ;
    private int height;
    private int width;
    private int paddingleft;
    private int paddingright;
    private int paddingtop;
    private int paddingbottom;
    private int textWidth;
    private int textHeight;


    public MyChartView(Context context) {
        this(context,null);
    }

    public MyChartView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public void init(Context context){
        this.context = context;
        paint_Chart = new Paint();
        paint_Chart.setStyle(Paint.Style.STROKE);
        paint_Chart.setColor(Color.GRAY);
        paint_Chart.setStrokeWidth(strokeWidth);
        paint_Chart.setAntiAlias(true);

        paint_Hstr = new Paint();
        paint_Hstr.setTextSize(textSize);
        paint_Hstr.setColor(hTextColor);
        paint_Hstr.setAntiAlias(true);

        patin_Vstr = new Paint();
        patin_Vstr.setTextSize(textSize);
        patin_Vstr.setColor(Color.BLACK);
        patin_Vstr.setAntiAlias(true);
        Rect rect = new Rect();
        paint_Hstr.getTextBounds(hStrs[0],0,hStrs[0].length(),rect);
        textWidth = rect.width();
        textHeight =  rect.height();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        paddingleft =  getPaddingLeft();
        paddingright = getPaddingRight();
        paddingtop = getPaddingTop();
        paddingbottom  = getPaddingBottom();
    }

    @Override
    public void draw(Canvas canvas) {
        //移动画布的原点
        int tx = paddingleft+textWidth;
        int ty = height-textHeight-textHeight;
        canvas.translate(tx,ty);
        paint_Chart.setColor(Color.GRAY);
        paint_Chart.setStyle(Paint.Style.STROKE);
        drawXY(canvas);
        drawKeDu(canvas);
    }
    /*
    绘制x，y轴
     */
    public void drawXY(Canvas canvas){
            Path path = new Path();
            path.moveTo(-strokeWidth/2,0);
            path.lineTo(width-paddingright,0);
            canvas.drawPath(path,paint_Chart);
            path.reset();
            path.lineTo(0,-height+paddingtop);
            canvas.drawPath(path,paint_Chart);

    }
    /*
    绘制刻度
     */
    public void drawKeDu(Canvas canvas){
        int count = maxValue/valueUnit;
        int everyUnitHeight = (height-textHeight*2-paddingtop)/count;//顶部预留一个字的高度
        int everyUnitWidth = (width-textWidth*2-paddingright)/hStrs.length;
        Path path = new Path();
        int maxX = width-paddingright;
        int y=0;
        String uv;
        for(int i=1;i<=count;i++){
             y = -everyUnitHeight*i;
            path.moveTo(0,y);
            path.lineTo(maxX,y);
            canvas.drawPath(path,paint_Chart);
             uv = valueUnit*i+"";
            //y轴刻度
            canvas.drawText(uv,-textWidth,y+textHeight/2,patin_Vstr);
            path.reset();

        }
        Random random = new Random();
        paint_Chart.setColor(Color.BLACK);

        for(int i=0;i<hStrs.length;i++){
            //x轴指标
            int x = everyUnitWidth*(i+1)-textWidth/2;
            canvas.drawText(hStrs[i],x,textHeight,patin_Vstr);
            int rondomY = (random.nextInt(10)+1)*everyUnitHeight*-1;
            if(i==0){
                path.reset();
                path.moveTo(x+textWidth/2,rondomY );
            }else{
                path.lineTo(x+textWidth/2, rondomY);
            }
            paint_Chart.setStyle(Paint.Style.FILL);
            canvas.drawCircle(x+textWidth/2,rondomY,5,paint_Chart);
        }
        paint_Chart.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path,paint_Chart);
    }




}
