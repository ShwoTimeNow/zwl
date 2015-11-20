package com.example.zhangweilong.zwl.Shader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhangweilong on 15/11/20.
 * mSweepGradient 梯度渐变
 * matrix 矩阵
 * mDegrees 旋转角度
 * MIN_DEGREE 渐变的最小角度
 */
public class RoundGradientView extends View {

    private Paint mPaint;
    private int mViewHeight,mViewWidth;
    private SweepGradient mSweepGradient;
    private Matrix matrix;
    private float mDegrees;
    private float MIN_DEGREE = 5f;

    public RoundGradientView(Context context) {
        super(context);
    }

    public RoundGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundGradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewHeight == 0 || mViewWidth == 0){
            mViewHeight = getMeasuredHeight();
            mViewWidth = getMeasuredWidth();
        }
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        matrix = new Matrix();
        /**
         * @param cx       The x-coordinate of the center
         * @param cy       The y-coordinate of the center
         * @param color0   The color to use at the start of the sweep
         * @param color1   The color to use at the end of the sweep
         */
        mSweepGradient = new SweepGradient(mViewWidth/2, mViewHeight/2, Color.RED, Color.YELLOW);
        mPaint.setShader(mSweepGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mViewWidth/2,mViewHeight/2,mViewHeight/2,mPaint);
        mDegrees += MIN_DEGREE;
        if (mDegrees == 360){
            mDegrees = 0;
        }
        //canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
        matrix.setRotate(mDegrees,mViewWidth/2,mViewHeight/2);
        mSweepGradient.setLocalMatrix(matrix);
        mPaint.setShader(mSweepGradient);
        postInvalidateDelayed(50);
    }
}
