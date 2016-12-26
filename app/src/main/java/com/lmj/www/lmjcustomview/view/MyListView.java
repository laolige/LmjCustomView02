package com.lmj.www.lmjcustomview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

/**
 * Created by limengjie
 * on 2016/10/10.15:00
 */

public class MyListView extends ListView {
    private String Tag = "MyListView";
    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 检测滑动到边界时候回调
     * @param deltaX
     * @param deltaY
     * @param scrollX
     * @param scrollY
     * @param scrollRangeX
     * @param scrollRangeY
     * @param maxOverScrollX
     * @param maxOverScrollY
     * @param isTouchEvent
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        Log.i(Tag,"deltaY:"+deltaY+",scrollY:"+scrollY+",scrollRangeY:"+scrollRangeY
                +",maxOverScrollY:"+maxOverScrollY+",isTouchEvent:"+isTouchEvent);

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }
}
