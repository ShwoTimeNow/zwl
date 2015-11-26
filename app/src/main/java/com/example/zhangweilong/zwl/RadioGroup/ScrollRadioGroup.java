package com.example.zhangweilong.zwl.RadioGroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RadioGroup;

/**
 * Created by zhangweilong on 15/11/26.
 */
public class ScrollRadioGroup extends RadioGroup{

    private Paint linePaint;
    private boolean mIndicatorScroll;

    public ScrollRadioGroup(Context context) {
        super(context);
        init();
    }
    public ScrollRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(5);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawLine(0,getHeight(),getWidth()/4,getHeight(),linePaint);
    }

    public boolean ismIndicatorScroll() {
        return mIndicatorScroll;
    }

    public void setmIndicatorScroll(boolean mIndicatorScroll) {
        this.mIndicatorScroll = mIndicatorScroll;
    }

}
