package com.example.zhangweilong.zwl.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.zhangweilong.zwl.R;

/**
 * Created by zhangweilong on 15/12/1.
 * skcan the follow address  zwl
 * {#link http://www.jianshu.com/p/11210b14f743}
 */
public class CircleBar extends View {

    private int viewHeight,viewWidth;
    private float bigCircleRadius,smallCirCleRadius,bigStrokeWidth,smallStrokeWidth;
    private int bigCircleCenterX,bigCircleCenterY;
    private Paint bigPaint,smallPaint;

    private int bigColor,smallColor;

    private int degree;


    public CircleBar(Context context) {
        super(context,null);
    }

    public CircleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public CircleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private void  initView(Context context,AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.CircleBar);
        bigColor = a.getColor(R.styleable.CircleBar_bigCircleColor,Color.GREEN);
        smallColor = a.getColor(R.styleable.CircleBar_smallCircleColor,Color.RED);
        bigCircleRadius = a.getDimension(R.styleable.CircleBar_bigCircleRadius,50);
        smallCirCleRadius = a.getDimension(R.styleable.CircleBar_smallCircleRadius,10);
        bigStrokeWidth = a.getDimension(R.styleable.CircleBar_bigCircleStrokeWidth,6);
        smallStrokeWidth = a.getDimension(R.styleable.CircleBar_smallCircleStrokeWidth,4);
        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        bigCircleCenterX = viewWidth/2;
        bigCircleCenterY = viewHeight/2;
        bigPaint = new Paint();
        bigPaint.setStrokeWidth(bigStrokeWidth);
        bigPaint.setStyle(Paint.Style.STROKE);
        bigPaint.setColor(bigColor);
        smallPaint = new Paint();
        smallPaint.setColor(smallColor);
        smallPaint.setStyle(Paint.Style.FILL);
        smallPaint.setStrokeWidth(smallStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(bigCircleCenterX,bigCircleCenterY,bigCircleRadius,bigPaint);

        double sweepAngle = Math.PI/180 * degree;
        degree += 2;
        //caculate the circle's center(x, y)
        float y = (float)Math.sin(sweepAngle)*(bigCircleRadius);
        float x = (float)Math.cos(sweepAngle)*(bigCircleRadius);
        int restoreCount = canvas.save();
        //change aix center position
        canvas.translate(bigCircleCenterX, bigCircleCenterY);
        canvas.drawCircle(x, y, smallCirCleRadius, smallPaint);
        canvas.restoreToCount(restoreCount);

        postInvalidateDelayed(10);
    }
}
