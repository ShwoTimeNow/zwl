package com.example.zhangweilong.zwl.Shader.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by weilong on 2015/11/22.
 * 1.Android图片倒影，
 * 2.Android图片模糊处理，
 * 3.Android图片圆角处理，
 * 4.图片沿着y轴旋转一定角度，
 * 5.Android给图片添加边框。
 */
public class BitmapUtils {
    private static final String TAG = "BitmapUtils";

    /**
     *
     * TODO
     * @throw
     * @return Bitmap
     * @param srcBitmap 源图片的bitmap
     * @param reflectionHeight 图片倒影的高度
     */
    public static Bitmap createReflectedBitmap(Bitmap srcBitmap,int reflectionHeight) {

        if (null == srcBitmap) {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }

        // The gap between the reflection bitmap and original bitmap.
        final int REFLECTION_GAP = 0;

        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();

        if (0 == srcWidth || srcHeight == 0) {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }

        // The matrix
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        try {

            // The reflection bitmap, width is same with original's, height is
            // half of original's.
            Bitmap reflectionBitmap = Bitmap.createBitmap(srcBitmap, 0, srcHeight - reflectionHeight,
                    srcWidth, reflectionHeight, matrix, false);

            if (null == reflectionBitmap) {
                Log.e(TAG, "Create the reflectionBitmap is failed");
                return null;
            }

            // Create the bitmap which contains original and reflection bitmap.
            Bitmap bitmapWithReflection = Bitmap.createBitmap(srcWidth, srcHeight + reflectionHeight,
                    Bitmap.Config.ARGB_8888);

            if (null == bitmapWithReflection) {
                return null;
            }

            // Prepare the canvas to draw stuff.
            Canvas canvas = new Canvas(bitmapWithReflection);

            // Draw the original bitmap.
            canvas.drawBitmap(srcBitmap, 0, 0, null);

            // Draw the reflection bitmap.
            canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP,
                    null);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            LinearGradient shader = new LinearGradient(0, srcHeight, 0,
                    bitmapWithReflection.getHeight() + REFLECTION_GAP,
                    0x70FFFFFF, 0x00FFFFFF, Shader.TileMode.MIRROR);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(
                    android.graphics.PorterDuff.Mode.DST_IN));

            canvas.save();
            // Draw the linear shader.
            canvas.drawRect(0, srcHeight, srcWidth,
                    bitmapWithReflection.getHeight() + REFLECTION_GAP, paint);
            if (reflectionBitmap != null && !reflectionBitmap.isRecycled()){
                reflectionBitmap.recycle();
                reflectionBitmap = null;
            }

            canvas.restore();

            return bitmapWithReflection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "Create the reflectionBitmap is failed");
        return null;
    }

    /**
     *
     * TODO
     * @throw
     * @return Bitmap
     * @param srcBitmap 源图片的bitmap
     * @param ret 圆角的度数
     */
    public static Bitmap getRoundImage(Bitmap srcBitmap, float ret) {

        if(null == srcBitmap){
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }

        int bitWidth = srcBitmap.getWidth();
        int bitHight = srcBitmap.getHeight();

        BitmapShader bitmapShader = new BitmapShader(srcBitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);

        RectF rectf = new RectF(0, 0, bitWidth, bitHight);

        Bitmap outBitmap = Bitmap.createBitmap(bitWidth, bitHight,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawRoundRect(rectf, ret, ret, paint);
        canvas.save();
        canvas.restore();

        return outBitmap;
    }


    /**
     *
     * TODO
     * @throw
     * @return Bitmap
     * @param srcBitmap 源图片的bitmap
     * @param reflectionHeight 图片倒影的高度
     * @param rotate 图片旋转的角度
     */
    public static Bitmap skewImage(Bitmap srcBitmap, float rotate, int reflectionHeight) {

        if(null == srcBitmap){
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }

        Bitmap reflecteBitmap = createReflectedBitmap(srcBitmap, reflectionHeight);

        if (null == reflecteBitmap){
            Log.e(TAG, "failed to createReflectedBitmap");
            return null;
        }

        int wBitmap = reflecteBitmap.getWidth();
        int hBitmap = reflecteBitmap.getHeight();
        float scaleWidth = ((float) 180) / wBitmap;
        float scaleHeight = ((float) 270) / hBitmap;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        reflecteBitmap = Bitmap.createBitmap(reflecteBitmap, 0, 0, wBitmap, hBitmap, matrix,
                true);
        Camera localCamera = new Camera();
        localCamera.save();
        Matrix localMatrix = new Matrix();
        localCamera.rotateY(rotate);
        localCamera.getMatrix(localMatrix);
        localCamera.restore();
        localMatrix.preTranslate(-reflecteBitmap.getWidth() >> 1,
                -reflecteBitmap.getHeight() >> 1);
        Bitmap localBitmap2 = Bitmap.createBitmap(reflecteBitmap, 0, 0,
                reflecteBitmap.getWidth(), reflecteBitmap.getHeight(), localMatrix,
                true);
        Bitmap localBitmap3 = Bitmap.createBitmap(localBitmap2.getWidth(),
                localBitmap2.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas localCanvas = new Canvas(localBitmap3);
        Paint localPaint = new Paint();
        localPaint.setAntiAlias(true);
        localPaint.setFilterBitmap(true);
        localCanvas.drawBitmap(localBitmap2, 0.0F, 0.0F, localPaint);
        if (null != reflecteBitmap && !reflecteBitmap.isRecycled()) {
            reflecteBitmap.recycle();
            reflecteBitmap = null;
        }
        if (null != localBitmap2 && !localBitmap2.isRecycled()) {
            localBitmap2.recycle();
            localBitmap2 = null;
        }
        localCanvas.save();
        localCanvas.restore();
        return localBitmap3;
    }


    /**
     *
     * TODO
     * @throw
     * @return Bitmap
     * @param  /bitmap 源图片
     * @param /radius The radius of the blur Supported range 0  25){
     * radius = 25.0f;
     * }else if (radius
     * @param srcBitmap 原图片
     * @param borderWidth 边框宽度
     * @param color 边框的颜色值
     * @return
     */
    public static Bitmap addFrameBitmap(Bitmap srcBitmap,int borderWidth,int color)
    {
        if (srcBitmap == null){
            Log.e(TAG, "the srcBitmap or borderBitmap is null");
            return null;
        }

        int newWidth = srcBitmap.getWidth() + borderWidth ;
        int newHeight = srcBitmap.getHeight() + borderWidth ;

        Bitmap outBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(outBitmap);

        Rect rec = canvas.getClipBounds();
        rec.bottom--;
        rec.right--;
        Paint paint = new Paint();
        //设置边框颜色
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        //设置边框宽度
        paint.setStrokeWidth(borderWidth);
        canvas.drawRect(rec, paint);

        canvas.drawBitmap(srcBitmap, borderWidth/2, borderWidth/2, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        if (srcBitmap !=null && !srcBitmap.isRecycled()){
            srcBitmap.recycle();
            srcBitmap = null;
        }

        return outBitmap;
    }

    /**
     * 放大缩小图片
     * @param bitmap,w,h
     */
    public static Bitmap zoomBitmap(Bitmap bitmap,int w,int h){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float)w / width);
        float scaleHeight = ((float)h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newbmp;
    }
    //将Drawable转化为Bitmap
    public static Bitmap drawableToBitmap(Drawable drawable){
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,width,height);
        drawable.draw(canvas);
        return bitmap;

    }

    //获得圆角图片的方法
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        /**
         * 画一个圆角矩形
         * rectF: 矩形
         * roundPx 圆角在x轴上或y轴上的半径
         */
        canvas.drawRoundRect(rectF, roundPx, roundPx+10, paint);
        //设置两张图片相交时的模式
        //setXfermode前的是 dst 之后的是src
        //在正常的情况下，在已有的图像上绘图将会在其上面添加一层新的形状。
        //如果新的Paint是完全不透明的，那么它将完全遮挡住下面的Paint；
        //PorterDuffXfermode就可以来解决这个问题
        //canvas原有的图片 可以理解为背景 就是dst
        //新画上去的图片 可以理解为前景 就是src
//      paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    //获得带倒影的图片方法
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap){
        // 图片与倒影间隔距离
        final int reflectionGap = 4;
        // 图片的宽度
        int width = bitmap.getWidth();
        // 图片的高度
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        // 图片缩放，x轴变为原来的1倍，y轴为-1倍,实现图片的反转
        matrix.preScale(1, -1);
        // 创建反转后的图片Bitmap对象，图片高是原图的一半。
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap,
                0, height/2, width, height/2, matrix, false);
        // 创建标准的Bitmap对象，宽和原图一致，高是原图的1.5倍。 可以理解为这张图将会在屏幕上显示 是原图和倒影的合体
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height/2), Bitmap.Config.ARGB_8888);
        // 构造函数传入Bitmap对象，为了在图片上画图
        Canvas canvas = new Canvas(bitmapWithReflection);
        // 画原始图片
        canvas.drawBitmap(bitmap, 0, 0, null);
        // 画间隔矩形
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height,width,height + reflectionGap,
                deafalutPaint);
        // 画倒影图片
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
        // 实现倒影渐变效果
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                bitmap.getHeight(), 0, bitmapWithReflection.getHeight()
                + reflectionGap, 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);

        // Set the Transfer mode to be porter duff and destination in
        // 覆盖效果
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }
}
