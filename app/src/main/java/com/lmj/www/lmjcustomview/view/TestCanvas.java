package com.lmj.www.lmjcustomview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by limengjie
 * on 2016/10/11.10:55
 */

public class TestCanvas extends View {
    private String Tag = "TestCanvas";
    private Handler handler = new Handler();


    public TestCanvas(Context context) {
        this(context, null);
    }


    public TestCanvas(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        Rect rect = canvas.getClipBounds();
        Log.i(Tag, "rect:" + rect.top);

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.i(Tag,"rect:");
//                invalidate();
//            }
//        },2000);

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(20, 20, 10, paint);
        canvas.saveLayerAlpha(10,10,100,100,255,1);
        paint.setColor(Color.RED);
        canvas.translate(10,10);
        canvas.drawCircle(20, 20, 20, paint);
        canvas.restore();
        canvas.saveLayerAlpha(10,10,100,100,255,2);
        paint.setColor(Color.GREEN);
        canvas.drawCircle(20, 20, 20, paint);
        canvas.save();
        paint.setColor(Color.BLACK);
        canvas.drawCircle(10, 10, 10, paint);
    }
}
