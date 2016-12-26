package com.lmj.www.lmjcustomview.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.lmj.www.lmjcustomview.view.ViewPath;
import com.lmj.www.lmjcustomview.view.ViewPathEvaluator;
import com.lmj.www.lmjcustomview.view.ViewPoint;

/**
 * Created by limengjie on 2016/12/21.
 */

public class BezierPath extends View {
    Paint paint1 = new Paint();
    Paint paint2 = new Paint();
    Paint paint3 = new Paint();
    Paint paint4 = new Paint();
    Paint paintpath = new Paint();

    int radus = 300;
    int time = 5000;
    int width;
    int height;
    private ValueAnimator redAnim1;
    private ValueAnimator redAnim2;
    private ValueAnimator redAnim3;
    private ValueAnimator redAnim4;
    private ViewPoint cpoint =new ViewPoint();
    private ViewPoint cpoint2=new ViewPoint();
    private ViewPoint cpoint3=new ViewPoint();
    private ViewPoint cpoint4=new ViewPoint();
    private AnimatorSet animatorSet2;
    private BezierPathListener bezierPathListener;
    private Path path1 = new Path();
    private Path path2 = new Path();
    private Path path3 = new Path();
    private Path path4 = new Path();

    public BezierPath(Context context) {
        super(context);
        init();
    }

    public BezierPath(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BezierPath(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        paint1.setStyle(Paint.Style.FILL);
        paint1.setAntiAlias(true);
        paint1.setColor(Color.RED);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setAntiAlias(true);
        paint2.setColor(Color.GREEN);

        paint3.setStyle(Paint.Style.FILL);
        paint3.setAntiAlias(true);
        paint3.setColor(Color.BLUE);
        paint4.setStyle(Paint.Style.FILL);
        paint4.setAntiAlias(true);
        paint4.setColor(Color.GRAY);

        paintpath.setStyle(Paint.Style.FILL);
        paintpath.setAntiAlias(true);
        paintpath.setColor(Color.BLUE);
        paintpath.setStyle(Paint.Style.STROKE);
        paintpath.setAntiAlias(true);
        paintpath.setColor(Color.GRAY);
    }

    public void initPath() {

        ViewPath viewPath = new ViewPath();
        viewPath.moveTo(width / 2, height / 2);
        cpoint.x  = width/2;
        cpoint.y = height/2;
        path1.moveTo(cpoint.x ,cpoint.y);
        path2.moveTo(cpoint.x ,cpoint.y);
        path3.moveTo(cpoint.x ,cpoint.y);
        path4.moveTo(cpoint.x ,cpoint.y);
        viewPath.quadTo(width / 2 - radus , height / 2 - radus , width / 2, height / 2 - radus);
        viewPath.quadTo(width / 2 + radus , height / 2 - radus , width / 2, height / 2);
        redAnim1 = ValueAnimator.ofObject(new ViewPathEvaluator(), viewPath.getPoints().toArray());
        redAnim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                cpoint = (ViewPoint) valueAnimator.getAnimatedValue();
                path1.lineTo(cpoint.x ,cpoint.y);
                postInvalidate();
            }
        });
        redAnim1.setDuration(time);

        ViewPath viewPath2 = new ViewPath();
        viewPath2.moveTo(width / 2, height / 2);
        cpoint2.x  = width/2;
        cpoint2.y = height/2;
        viewPath2.quadTo(width / 2 + radus , height / 2 - radus , width / 2+radus, height / 2 );
        viewPath2.quadTo(width / 2 + radus , height / 2 + radus , width / 2, height / 2);
        redAnim2 = ValueAnimator.ofObject(new ViewPathEvaluator(), viewPath2.getPoints().toArray());
        redAnim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                cpoint2 = (ViewPoint) valueAnimator.getAnimatedValue();
                path2.lineTo(cpoint2.x ,cpoint2.y);
                postInvalidate();
            }
        });
        redAnim2.setDuration(time);

        ViewPath viewPath3 = new ViewPath();
        viewPath3.moveTo(width / 2, height / 2);
        cpoint3.x  = width/2;
        cpoint3.y = height/2;
        viewPath3.quadTo(width / 2+radus , height / 2 + radus , width / 2, height / 2+radus );
        viewPath3.quadTo(width / 2 - radus , height / 2 + radus , width / 2, height / 2);
        redAnim3 = ValueAnimator.ofObject(new ViewPathEvaluator(), viewPath3.getPoints().toArray());
        redAnim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                cpoint3 = (ViewPoint) valueAnimator.getAnimatedValue();
                path3.lineTo(cpoint3.x ,cpoint3.y);
                postInvalidate();
            }
        });
        redAnim3.setDuration(time);

        redAnim3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimationing = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimationing = false;
                if(null!=bezierPathListener){
                    bezierPathListener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                isAnimationing = false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        ViewPath viewPath4 = new ViewPath();
        viewPath4.moveTo(width / 2, height / 2);
        cpoint4.x  = width/2;
        cpoint4.y = height/2;
        viewPath4.quadTo(width / 2-radus , height / 2 + radus , width / 2-radus, height / 2 );
        viewPath4.quadTo(width / 2 - radus , height / 2 - radus , width / 2, height / 2);
        redAnim4 = ValueAnimator.ofObject(new ViewPathEvaluator(), viewPath4.getPoints().toArray());
        redAnim4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                cpoint4 = (ViewPoint) valueAnimator.getAnimatedValue();
                path4.lineTo(cpoint4.x ,cpoint4.y);
                postInvalidate();
            }
        });
        redAnim4.setDuration(time);

        animatorSet2 = new AnimatorSet();
        animatorSet2.playTogether(redAnim1,redAnim2,redAnim3,redAnim4);
        animatorSet2.setDuration(time);


    }
    public boolean isAnimationing = false;
    boolean isInitPath = false;



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        if (width > 0) {
            if (!isInitPath) {
                isInitPath = true;
                initPath();
            }
        }

    }


    @Override
    public void draw(final Canvas canvas) {
        canvas.drawPath(path1, paintpath);
        canvas.drawPath(path2, paintpath);
        canvas.drawPath(path3, paintpath);
        canvas.drawPath(path4, paintpath);
        canvas.drawCircle(cpoint.x-20, cpoint.y-20, 25, paint1);
        canvas.drawCircle(cpoint2.x+20, cpoint2.y-20, 25, paint2);
        canvas.drawCircle(cpoint3.x+20, cpoint3.y+20, 25, paint3);
        canvas.drawCircle(cpoint4.x-20, cpoint4.y+20, 25, paint4);

    }
    public void startAnimation(){
        if (!isAnimationing) {
            animatorSet2.start();
        }
    }
    public void setListener(BezierPathListener bezierPathListener){
        this.bezierPathListener =bezierPathListener;
    }
    public  interface BezierPathListener{
        void onAnimationEnd();
    }


}
