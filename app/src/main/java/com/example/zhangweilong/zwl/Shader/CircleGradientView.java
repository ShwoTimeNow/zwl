package com.example.zhangweilong.zwl.Shader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by weilong on 2015/11/22.
 */
public class CircleGradientView extends View {


    private int mViewHeight,mViewWidth,mRadius;
    private Paint mPaint;
    private RadialGradient mRadiaGradient;
    private Matrix matrix;
    private static float MIN_SCALE = 0.1f;//
    private static float SCALE_VALUE = 1f;

    public CircleGradientView(Context context) {
        super(context);
    }

    public CircleGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleGradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        mRadius = Math.min(mViewHeight,mViewWidth)/2;
        mPaint = new Paint();
        matrix = new Matrix();
        mRadiaGradient = new RadialGradient(mViewWidth/2,mViewHeight/2,mRadius,Color.GREEN,Color.RED
        , Shader.TileMode.MIRROR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawRect(0,0,mViewWidth,mViewHeight,mPaint);
        matrix.setScale(SCALE_VALUE, SCALE_VALUE, mViewWidth / 2, mViewHeight/2);
        SCALE_VALUE -= MIN_SCALE;
        if (SCALE_VALUE < 0){
            SCALE_VALUE = 1f;
        }
        mRadiaGradient.setLocalMatrix(matrix);
        mPaint.setShader(mRadiaGradient);
        canvas.drawCircle(mViewWidth / 2, mViewHeight / 2, mRadius,mPaint);
        postInvalidateDelayed(200);
    }

}
