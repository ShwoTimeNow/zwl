package com.example.zhangweilong.zwl.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhangweilong on 15/12/1.
 */
public class ArcCircleBar extends View {

    private float circleRadius,centerX,centerY;
    private Paint mPaint;
    private RectF mRect;
    private float degree;

    public ArcCircleBar(Context context) {
        super(context);
    }
    public ArcCircleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArcCircleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = getMeasuredWidth()/2;
        centerY = getMeasuredHeight()/2;
        mRect = new RectF(0f,0f,getMeasuredWidth(),getMeasuredHeight());
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        degree += 3;
        if (degree > 360){
            degree = 0;
        }
        canvas.drawArc(mRect, degree,60,false,mPaint);
        canvas.drawArc(mRect, degree +120,60,false,mPaint);
        canvas.drawArc(mRect, degree +240,60,false,mPaint);

        RectF f = new RectF(0,0,400,200);
        canvas.drawArc(f,(float) 240,360,true,mPaint);
        canvas.drawRect(f,mPaint);
        postInvalidateDelayed(10);
    }
}
