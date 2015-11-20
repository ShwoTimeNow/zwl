package com.example.zhangweilong.zwl.Shader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by zhangweilong on 15/11/20.
 * mViewWidth，mViewHeight：控件的宽高
 * mTranslateX：水平移动参数
 * mGradientMatrix：位移矩阵
 */
public class LinearGradientTextView extends TextView {


    private LinearGradient mLinearGradient;
    private Paint mPaint;
    private int mViewWidth,mViewHeight,mTranslateX;
    private Matrix mGradientMatrix;

    public LinearGradientTextView(Context context) {
        super(context);
    }

    public LinearGradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearGradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0 || mViewHeight==0)
        {
            mViewWidth = getMeasuredWidth();
            mViewHeight=getMeasuredHeight();
            if (mViewWidth > 0 ||  mViewHeight>0 )
            {
                mPaint = getPaint();
                // 创建LinearGradient对象
                // 起始点坐标（0, 0） 终点坐标（mViewWidth，mViewHeight）
                // 第一个,第二个参数表示渐变起点 可以设置起点终点在对角等任意位置
                // 第三个,第四个参数表示渐变终点
                // 第五个参数表示渐变颜色
                // 第六个参数可以为空,表示坐标,值为0-1
                // 如果这是空的，颜色均匀分布，沿梯度线。
                // 第七个表示平铺方式
                // CLAMP重复最后一个颜色至最后
                // MIRROR重复着色的图像水平或垂直方向已镜像方式填充会有翻转效果
                // REPEAT重复着色的图像水平或垂直方向
                mLinearGradient = new LinearGradient(0, 0, mViewWidth, mViewHeight,
                        Color.YELLOW, Color.BLACK, Shader.TileMode.MIRROR);
                mPaint.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ( mLinearGradient != null)
        {
            mTranslateX += mViewWidth / 30;
            if (mTranslateX >mViewWidth )
            {
                mTranslateX = -mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslateX, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(20);
        }
    }
}
