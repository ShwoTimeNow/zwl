package com.example.zhangweilong.zwl.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import com.example.zhangweilong.zwl.R;


@TargetApi(Build.VERSION_CODES.FROYO)
public class RangeBar extends View {

    int fingerCount = 0;

    private static final int DEFAULT_DURATION = 100;

    private enum DIRECTION {
        LEFT, RIGHT;
    }

    private int mDuration;

    /**
     * 光标
     */
    private Scroller mLeftScroller;
    private Scroller mRightScroller;

    /**
     * 光标背景图片
     */
    private Drawable mLeftCursorBG;
    private Drawable mRightCursorBG;

    private int mLeftCursorBGWidth;
    private int mRightCursorBGWidth;

    /**
     * 代表状态
     */
    private int[] mPressedEnableState = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
    private int[] mUnPresseEanabledState = new int[] { -android.R.attr.state_pressed, android.R.attr.state_enabled };

    /**
     * 不同状态的颜色.
     */
    private int mTextColorNormal;
    private int mTextColorSelected;
    private int mSeekbarColorNormal;
    private int mSeekbarColorSelected;

    /**
     * seekbar的高度
     */
    private int mSeekbarHeight;

    /**
     * 文字尺寸
     */
    private int mTextSize;

    /**
     * seekbar和文字的间距
     */
    private int mMarginBetween;

    /**
     * 每一段的长度
     */
    private int mPartLength;

    /**
     * 标记的文字
     */
    private CharSequence[] mTextArray = { "", "" };

    /**
     * 刻度数组
     */
    private float[] mTextWidthArray;

    private Rect mPaddingRect;
    private Rect mLeftCursorRect;
    private Rect mRightCursorRect;

    private RectF mSeekbarRect;
    private RectF mSeekbarRectSelected;

    private float mLeftCursorIndex = 0;
    private float mRightCursorIndex = 1.0f;
    private int mLeftCursorNextIndex = 0;
    private int mRightCursorNextIndex = 1;
    private int mReturnLeft = 0;
    private int mReturnRight = 1;

    private Paint mPaint;

    private int mLeftPointerLastX;
    private int mRightPointerLastX;

    private int mLeftPointerID = -1;
    private int mRightPointerID = -1;

    private boolean mLeftHited;
    private boolean mRightHited;

    private int mRightBoundary;

    private OnUxinRangeBarActionUpListener mListener;

    private Rect[] mClickRectArray;
    private int mClickIndex = -1;
    private int mClickDownLastX = -1;
    private int mClickDownLastY = -1;

    private boolean isSuperposition;// 是否允许在两端重合，默认不允许重合

    public void setSuperposition(boolean isSuperposition) {
        this.isSuperposition = isSuperposition;
    }

    public RangeBar(Context context) {
        this(context, null, 0);
    }

    public RangeBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RangeBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyConfig(context, attrs);

        if (mPaddingRect == null) {
            mPaddingRect = new Rect();
        }

        mPaddingRect.left = getPaddingLeft();
        mPaddingRect.top = getPaddingTop();
        mPaddingRect.right = getPaddingRight();
        mPaddingRect.bottom = getPaddingBottom();

        mLeftCursorRect = new Rect();
        mRightCursorRect = new Rect();

        mSeekbarRect = new RectF();
        mSeekbarRectSelected = new RectF();

        if (mTextArray != null) {
            mTextWidthArray = new float[mTextArray.length];
            mClickRectArray = new Rect[mTextArray.length];
        }

        mLeftScroller = new Scroller(context, new DecelerateInterpolator());
        mRightScroller = new Scroller(context, new DecelerateInterpolator());

        initPaint();
        initTextWidthArray();

        setWillNotDraw(false);
        setFocusable(true);
        setClickable(true);
    }

    //记录seekbar的左端初始位置
    private int leftOriginal;
    //记录seekbar的右端初始位置
    private int rightOrigianl;
    /**
     * 应用配置
     *
     * @param context
     * @param attrs
     */
    private void applyConfig(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RangeSeekbar);

        mDuration = a.getInteger(R.styleable.RangeSeekbar_autoMoveDuration, DEFAULT_DURATION);

        mLeftCursorBG = a.getDrawable(R.styleable.RangeSeekbar_leftCursorBackground);
        mRightCursorBG = a.getDrawable(R.styleable.RangeSeekbar_rightCursorBackground);

        mTextColorNormal = a.getColor(R.styleable.RangeSeekbar_textColorNormal, Color.BLACK);
        mTextColorSelected = a.getColor(R.styleable.RangeSeekbar_textColorSelected, Color.rgb(242, 79, 115));

        //Color.rgb(218, 215, 215)  Color.rgb(250, 128, 60)
        mSeekbarColorNormal = a.getColor(R.styleable.RangeSeekbar_seekbarColorNormal,Color.rgb(218, 215, 215));
        mSeekbarColorSelected = a.getColor(R.styleable.RangeSeekbar_seekbarColorSelected, getResources().getColor(R.color.red_df8a67));

        mSeekbarHeight = (int) a.getDimension(R.styleable.RangeSeekbar_seekbarHeight, 10);
        mTextSize = (int) a.getDimension(R.styleable.RangeSeekbar_textSize, 15);
        mMarginBetween = (int) a.getDimension(R.styleable.RangeSeekbar_spaceBetween, 15);

        mTextArray = a.getTextArray(R.styleable.RangeSeekbar_markTextArray);
        if (mTextArray != null && mTextArray.length > 0) {
            mLeftCursorIndex = 0;
            mRightCursorIndex = mTextArray.length - 1;
            mRightCursorNextIndex = (int) mRightCursorIndex;
        }
        a.recycle();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.FILL);
        mPaint.setTextSize(mTextSize);
    }

    private void initTextWidthArray() {
        if (mTextArray != null && mTextArray.length > 0) {
            final int length = mTextArray.length;
            for (int i = 0; i < length; i++) {
                mTextWidthArray[i] = mPaint.measureText(mTextArray[i].toString());
            }
        }
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);

        if (mPaddingRect == null) {
            mPaddingRect = new Rect();
        }
        mPaddingRect.left = left;
        mPaddingRect.top = top;
        mPaddingRect.right = right;
        mPaddingRect.bottom = bottom;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        final int leftPointerH = mLeftCursorBGWidth = mLeftCursorBG.getIntrinsicHeight();
        final int rightPointerH = mRightCursorBGWidth = mRightCursorBG.getIntrinsicHeight();

        // Get max height between left and right cursor.
        final int maxOfCursor = Math.max(leftPointerH, rightPointerH);

        // Then get max height between seekbar and cursor.
        final int maxOfCursorAndSeekbar = Math.max(mSeekbarHeight, maxOfCursor);

        // So we get the needed height.
        int heightNeeded = maxOfCursorAndSeekbar + mMarginBetween + mTextSize + mPaddingRect.top + mPaddingRect.bottom;

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(heightSize, heightNeeded), MeasureSpec.EXACTLY);

        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        mSeekbarRect.left = leftOriginal = mPaddingRect.left + mLeftCursorBG.getIntrinsicWidth() / 2;
        mSeekbarRect.right = rightOrigianl = widthSize - mPaddingRect.right - mRightCursorBG.getIntrinsicWidth() / 2;
        mSeekbarRect.top = mPaddingRect.top + mTextSize + mMarginBetween;
        mSeekbarRect.bottom = mSeekbarRect.top + mSeekbarHeight;

        mSeekbarRectSelected.top = mSeekbarRect.top;
        mSeekbarRectSelected.bottom = mSeekbarRect.bottom;

        mPartLength = ((int) (mSeekbarRect.right - mSeekbarRect.left)) / (mTextArray.length - 1);

        mRightBoundary = (int) (mSeekbarRect.right + mRightCursorBG.getIntrinsicWidth() / 2);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*** Draw text marks ***/
        final int length = mTextArray.length;
        mPaint.setTextSize(mTextSize);
        for (int i = 0; i < length; i++) {
            if ((i > mLeftCursorIndex && i < mRightCursorIndex) || (i == mLeftCursorIndex || i == mRightCursorIndex)) {
                mPaint.setColor(mTextColorSelected);
            } else {
                mPaint.setColor(mTextColorNormal);
            }
            final String text2draw = mTextArray[i].toString();
            final float textWidth = mTextWidthArray[i];

            float textDrawLeft = 0;
            // The last text mark's draw location should be adjust.
            if (i == length - 1) {
                textDrawLeft = mSeekbarRect.right + (mRightCursorBG.getIntrinsicWidth() / 2) - textWidth;
            } else {
                textDrawLeft = mSeekbarRect.left + i * mPartLength - textWidth / 2;
            }

            // canvas.drawText(text2draw, textDrawLeft, mPaddingRect.top +
            // mTextSize, mPaint);

            Rect rect = mClickRectArray[i];
            if (rect == null) {
                rect = new Rect();
                rect.top = mPaddingRect.top;
                rect.bottom = rect.top + mTextSize + mMarginBetween + mSeekbarHeight;
                rect.left = (int) textDrawLeft;
                rect.right = (int) (rect.left + textWidth);

                mClickRectArray[i] = rect;
            }
        }

        /*** Draw seekbar ***/
        final float radius = (float) mSeekbarHeight / 2;
        mSeekbarRectSelected.left = mSeekbarRect.left + mPartLength * mLeftCursorIndex;
        mSeekbarRectSelected.right = mSeekbarRect.left + mPartLength * mRightCursorIndex;
        // If whole of seekbar is selected, just draw seekbar with selected
        // color.
        if (mLeftCursorIndex == 0 && mRightCursorIndex == length - 1) {
            mPaint.setColor(mSeekbarColorSelected);
            canvas.drawRoundRect(mSeekbarRect, radius, radius, mPaint);
        } else {
            // Draw background first.
            mPaint.setColor(mSeekbarColorNormal);
            canvas.drawRoundRect(mSeekbarRect, radius, radius, mPaint);

            // Draw selected part.
            mPaint.setColor(mSeekbarColorSelected);
            // Can draw rounded rectangle, but original rectangle is enough.
            // Because edges of selected part will be covered by cursors.
            canvas.drawRect(mSeekbarRectSelected, mPaint);
        }

        /*** Draw cursors ***/
        // left cursor first
        final int leftWidth = mLeftCursorBG.getIntrinsicWidth();
        final int leftHieght = mLeftCursorBG.getIntrinsicHeight();
        final int leftLeft = (int) (mSeekbarRectSelected.left - (float) leftWidth / 2);
        final int leftTop = (int) ((mSeekbarRect.top + mSeekbarHeight / 2) - (leftHieght / 2));
        mLeftCursorRect.left = leftLeft;
        mLeftCursorRect.top = leftTop;
        mLeftCursorRect.right = leftLeft + leftWidth;
        mLeftCursorRect.bottom = leftTop + leftHieght;
        mLeftCursorBG.setBounds(mLeftCursorRect);
        mLeftCursorBG.draw(canvas);
        {
            // draw left text
            final int leftTextIndex = (int) Math.rint(mLeftCursorIndex) < 0 ? 0 : (int) Math.rint(mLeftCursorIndex);
            final String leftText = mTextArray[leftTextIndex].toString();

            final int leftCenter = mLeftCursorRect.left + (leftWidth / 2);
            Rect leftTextRect = new Rect(leftCenter - 30, mPaddingRect.top, leftCenter + 30, mPaddingRect.top + mTextSize);
            mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            FontMetricsInt leftFontMetrics = mPaint.getFontMetricsInt();
            int leftBaseline = leftTextRect.top + (leftTextRect.bottom - leftTextRect.top - leftFontMetrics.bottom + leftFontMetrics.top) / 2
                    - leftFontMetrics.top;
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(leftText, leftOriginal, leftBaseline, mPaint);
        }

        // right cursor second
        final int rightWidth = mRightCursorBG.getIntrinsicWidth();
        final int rightHeight = mRightCursorBG.getIntrinsicHeight();
        final int rightLeft = (int) (mSeekbarRectSelected.right - (float) rightWidth / 2);
        final int rightTop = (int) ((mSeekbarRectSelected.top + mSeekbarHeight / 2) - (rightHeight / 2));
        mRightCursorRect.left = rightLeft;
        mRightCursorRect.top = rightTop;
        mRightCursorRect.right = rightLeft + rightWidth;
        mRightCursorRect.bottom = rightTop + rightHeight;
        mRightCursorBG.setBounds(mRightCursorRect);
        mRightCursorBG.draw(canvas);
        {
            // draw right text
            final int rightTextIndex = (int) Math.rint(mRightCursorIndex) < 0 ? 0 : (int) Math.rint(mRightCursorIndex);
            final String rightText = mTextArray[rightTextIndex].toString();
            final int rightCenter = mRightCursorRect.left + (rightWidth / 2);
            Rect rightTextRect = new Rect(rightCenter - 30, mPaddingRect.top, rightCenter + 30, mPaddingRect.top + mTextSize);
            FontMetricsInt rightFontMetrics = mPaint.getFontMetricsInt();
            int rightBaseline = rightTextRect.top + (rightTextRect.bottom - rightTextRect.top - rightFontMetrics.bottom + rightFontMetrics.top) / 2
                    - rightFontMetrics.top;
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(rightText, rightOrigianl -  rightWidth / 4, rightBaseline, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // if (getParent() != null) {
        // getParent().requestDisallowInterceptTouchEvent(true);
        // }

        // For multiple touch
        final int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                fingerCount++;
                handleTouchDown(event);

                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                handleTouchDown(event);

                break;
            case MotionEvent.ACTION_MOVE:

                handleTouchMove(event);

                break;
            case MotionEvent.ACTION_POINTER_UP:

                handleTouchUp(event);

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                fingerCount--;
                triggleCallback(false, 0);
                handleTouchUp(event);
                mClickIndex = -1;
                mClickDownLastX = -1;
                mClickDownLastY = -1;
                break;
        }

        return super.onTouchEvent(event);
    }

    private void handleTouchDown(MotionEvent event) {
        final int actionIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int downX = (int) event.getX(actionIndex);
        final int downY = (int) event.getY(actionIndex);

        if (mLeftCursorRect.contains(downX, downY)) {
            if (mLeftHited) {
                return;
            }

            // If hit, change state of drawable, and record id of touch pointer.
            mLeftPointerLastX = downX;
            mLeftCursorBG.setState(mPressedEnableState);
            mLeftPointerID = event.getPointerId(actionIndex);
            mLeftHited = true;

            invalidate();
        } else if (mRightCursorRect.contains(downX, downY)) {
            if (mRightHited) {
                return;
            }

            mRightPointerLastX = downX;
            mRightCursorBG.setState(mPressedEnableState);
            mRightPointerID = event.getPointerId(actionIndex);
            mRightHited = true;

            invalidate();
        } else {
            // If touch x-y not be contained in cursor,
            // then we check if it in click areas
            final int clickBoundaryTop = mClickRectArray[0].top;
            final int clickBoundaryBottom = mClickRectArray[0].bottom;
            mClickDownLastX = downX;
            mClickDownLastY = downY;

            // Step one : if in boundary of total Y.
            if (downY < clickBoundaryTop || downY > clickBoundaryBottom) {
                mClickIndex = -1;
                return;
            }

            // Step two: find nearest mark in x-axis
            final int partIndex = (int) ((downX - mSeekbarRect.left) / mPartLength);
            final int partDelta = (int) ((downX - mSeekbarRect.left) % mPartLength);
            if (partDelta < mPartLength / 2) {
                mClickIndex = partIndex;
            } else if (partDelta > mPartLength / 2) {
                mClickIndex = partIndex + 1;
            }

            if (mClickIndex == mLeftCursorIndex || mClickIndex == mRightCursorIndex
                    || mClickIndex < 0 ||  mClickIndex > mTextArray.length -1) {
                mClickIndex = -1;
                return;
            }

            // Step three: check contain
            if (mClickIndex != -1) {

                if(mClickIndex >= mClickRectArray.length) {
                    mClickIndex = mClickRectArray.length-1;
                }
                if (!mClickRectArray[mClickIndex].contains(downX, downY)) {
                    mClickIndex = -1;
                }
            } else {
                System.out.println("bug");
            }
        }
    }

    private void handleTouchUp(MotionEvent event) {
        final int actionIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int actionID = event.getPointerId(actionIndex);

        if (actionID == mLeftPointerID) {
            if (!mLeftHited) {
                return;
            }

            // 如果滑动到两个标记之间, 它应该位于其中一个

            // step 1:计算较低的偏移量
            final int lower = (int) Math.floor(mLeftCursorIndex);
            final int higher = (int) Math.ceil(mLeftCursorIndex);

            final float offset = mLeftCursorIndex - lower;
            if (offset != 0) {

                // step 2:Decide which mark will go to.
                if (offset < 0.5f) {
                    // If left cursor want to be located on lower mark, go ahead
                    // guys.
                    // Because right cursor will never appear lower than the
                    // left one.
                    mLeftCursorNextIndex = lower;
                } else if (offset > 0.5f) {
                    mLeftCursorNextIndex = higher;
                    // If left cursor want to be located on higher mark,
                    // situation becomes a little complicated.
                    // We should check that whether distance between left and
                    // right cursor is less than 1, and next index of left
                    // cursor is difference with current
                    // of right one.
                    if (Math.abs(mLeftCursorIndex - mRightCursorIndex) <= 1 && mLeftCursorNextIndex == mRightCursorNextIndex) {
                        // Left can not go to the higher, just to the lower one.
                        mLeftCursorNextIndex = lower;
                    }
                }

                // step 3: Move to.
                if (!mLeftScroller.computeScrollOffset()) {
//                	mLeftCursorIndex = Math.round(mLeftCursorIndex);
                    final int fromX = (int) (mLeftCursorIndex * mPartLength);
                    mLeftScroller.startScroll(fromX, 0, mLeftCursorNextIndex * mPartLength - fromX, 0, mDuration);

                    triggleCallback(true, mLeftCursorNextIndex);
                }
            }

            // Reset values of parameters
            mLeftPointerLastX = 0;
            mLeftCursorBG.setState(mUnPresseEanabledState);
            mLeftPointerID = -1;
            mLeftHited = false;

            invalidate();
        } else if (actionID == mRightPointerID) {
            if (!mRightHited) {
                return;
            }

            final int lower = (int) Math.floor(mRightCursorIndex);
            final int higher = (int) Math.ceil(mRightCursorIndex);

            final float offset = mRightCursorIndex - lower;
            if (offset != 0) {

                if (offset > 0.5f) {
                    mRightCursorNextIndex = higher;
                } else if (offset < 0.5f) {
                    mRightCursorNextIndex = lower;
                    if (Math.abs(mLeftCursorIndex - mRightCursorIndex) <= 1 && mRightCursorNextIndex == mLeftCursorNextIndex) {
                        mRightCursorNextIndex = higher;
                    }
                }
                if (!mRightScroller.computeScrollOffset()) {
//                	mRightCursorIndex = Math.round(mRightCursorIndex);
                    final int fromX = (int) (mRightCursorIndex * mPartLength);
                    mRightScroller.startScroll(fromX, 0, mRightCursorNextIndex * mPartLength - fromX, 0, mDuration);

                    triggleCallback(false, mRightCursorNextIndex);
                }
            }

            mRightPointerLastX = 0;
            mLeftCursorBG.setState(mUnPresseEanabledState);
            mRightPointerID = -1;
            mRightHited = false;

            invalidate();
        } else {
            final int pointerIndex = event.findPointerIndex(actionID);
            final int upX = (int) event.getX(pointerIndex);
            final int upY = (int) event.getY(pointerIndex);

            if(mClickIndex < 0 ||  mClickIndex > mTextArray.length -1){
                mClickIndex = -1;
                return;
            }
            if (mClickIndex != -1 && mClickRectArray[mClickIndex].contains(upX, upY)) {
                // Find nearest cursor
                final float distance2LeftCursor = Math.abs(mLeftCursorIndex - mClickIndex);
                final float distance2Right = Math.abs(mRightCursorIndex - mClickIndex);

                final boolean moveLeft = distance2LeftCursor <= distance2Right;
                int fromX = 0;
                if (moveLeft) {
                    if (!mLeftScroller.computeScrollOffset()) {
                        mLeftCursorNextIndex = mClickIndex;
                        fromX = (int) (mLeftCursorIndex * mPartLength);
                        mLeftScroller.startScroll(fromX, 0, mLeftCursorNextIndex * mPartLength - fromX, 0, mDuration);

                        triggleCallback(true, mLeftCursorNextIndex);

                        invalidate();
                    }
                } else {
                    if (!mRightScroller.computeScrollOffset()) {
                        mRightCursorNextIndex = mClickIndex;
                        fromX = (int) (mRightCursorIndex * mPartLength);
                        mRightScroller.startScroll(fromX, 0, mRightCursorNextIndex * mPartLength - fromX, 0, mDuration);

                        triggleCallback(false, mRightCursorNextIndex);

                        invalidate();
                    }
                }
            }
        }
    }

    private void handleTouchMove(MotionEvent event) {

        if (mClickIndex != -1) {
            final int actionIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
            final int x = (int) event.getX(actionIndex);
            final int y = (int) event.getY(actionIndex);

            if (!mClickRectArray[mClickIndex].contains(x, y)) {
                mClickIndex = -1;
            }
        }

        if (mLeftHited && mLeftPointerID != -1) {
            final int index = event.findPointerIndex(mLeftPointerID);
            final float x = event.getX(index);

            float deltaX = x - mLeftPointerLastX;
            mLeftPointerLastX = (int) x;

            DIRECTION direction = (deltaX < 0 ? DIRECTION.LEFT : DIRECTION.RIGHT);

            if (direction == DIRECTION.LEFT && mLeftCursorIndex == 0) {
                return;
            }

            // 检查光标是否移出边界了
            if (mLeftCursorRect.left + deltaX < mPaddingRect.left) {
                mLeftCursorIndex = 0;
                invalidate();
                return;
            }

            // 碰撞互斥
//            if (mLeftCursorRect.right + deltaX >= mRightCursorRect.left) {
            // Check whether right cursor is in "Touch" mode( if in touch
            // mode, represent that we can not move it at all), or right
            // cursor reach the boundary.
//                if (mRightHited || mRightCursorIndex >= mTextArray.length - 1.5 || mRightScroller.computeScrollOffset()) {
            // Just move left cursor to the left side of right one.
            // deltaX = mRightCursorRect.left - mLeftCursorRect.right;
//                } else {
            // Move right cursor to higher location.
//                    final int maxMarkIndex = mTextArray.length - 1;
//                    triggleCallback(false, (int)mRightCursorIndex);
//                    if (mRightCursorIndex <= maxMarkIndex - 1) {
//                        mRightCursorNextIndex = (int) (mRightCursorIndex + 1);
//
//                        if (!mRightScroller.computeScrollOffset()) {
//                            final int fromX = (int) (mRightCursorIndex * mPartLength);
//
//                            mRightScroller.startScroll(fromX, 0, mRightCursorNextIndex * mPartLength - fromX, 0, mDuration);
//                            triggleCallback(false, mRightCursorNextIndex);
//                        }
//                    }
//                }
//            }

            // After some calculate, if deltaX is still be zero, do quick
            // return.
            if (deltaX == 0) {
                return;
            }
            // Calculate the movement.
            final float moveX = deltaX / mPartLength;
            mLeftCursorIndex += moveX;
            //不允许与最右侧的重合
            if (Math.rint(mLeftCursorIndex) >= mTextArray.length - 2) {
                if (!isSuperposition) {
                    mLeftCursorIndex = mTextArray.length - 2;
                } else {
                    mLeftCursorIndex = mTextArray.length - 1;
                }

            }
            invalidate();
        }

        if (mRightHited && mRightPointerID != -1) {
            final int index = event.findPointerIndex(mRightPointerID);
            final float x = event.getX(index);

            float deltaX = x - mRightPointerLastX;
            mRightPointerLastX = (int) x;

            DIRECTION direction = (deltaX < 0 ? DIRECTION.LEFT : DIRECTION.RIGHT);

            final int maxIndex = mTextArray.length - 1;
            if (direction == DIRECTION.RIGHT && mRightCursorIndex == maxIndex) {
                return;
            }

            if (mRightCursorRect.right + deltaX > mRightBoundary) {
                deltaX = mRightBoundary - mRightCursorRect.right;
            }

            final int maxMarkIndex = mTextArray.length - 1;
            if (direction == DIRECTION.RIGHT && mRightCursorIndex == maxMarkIndex) {
                return;
            }

//            if (mRightCursorRect.left + deltaX < mLeftCursorRect.right) {
//                if (mLeftHited || mLeftCursorIndex <= 0.5 || mLeftScroller.computeScrollOffset()) {
//                    deltaX = mLeftCursorRect.right - mRightCursorRect.left;
//                } else {
//                    if (mLeftCursorIndex >= 1) {
//                    	triggleCallback(false, (int)mLeftCursorIndex);
//                        mLeftCursorNextIndex = (int) (mLeftCursorIndex - 1);
//
//                        if (!mLeftScroller.computeScrollOffset()) {
//                            final int fromX = (int) (mLeftCursorIndex * mPartLength);
//                            mLeftScroller.startScroll(fromX, 0, mLeftCursorNextIndex * mPartLength - fromX, 0, mDuration);
//                            triggleCallback(true, mLeftCursorNextIndex);
//                        }
//                    }
//                }
//            }

            if (deltaX == 0) {
                return;
            }

            final float moveX = deltaX / mPartLength;
            mRightCursorIndex += moveX;
            if((int) Math.rint(mRightCursorIndex) > maxMarkIndex){
                mRightCursorIndex = maxMarkIndex;
            }
            //不允许与最左侧的重合
            if ((int) Math.rint(mRightCursorIndex) < 0.5) {
                if (!isSuperposition) {
                    mRightCursorIndex = 1;
                } else {
                    mRightCursorIndex = 0;
                }
            }

            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        if (mLeftScroller.computeScrollOffset()) {
            final int deltaX = mLeftScroller.getCurrX();

            mLeftCursorIndex = (float) deltaX / mPartLength;

            invalidate();
        }

        if (mRightScroller.computeScrollOffset()) {
            final int deltaX = mRightScroller.getCurrX();

            mRightCursorIndex = (float) deltaX / mPartLength;

            invalidate();
        }
    }

    int lastL;
    int lastR;

    private void triggleCallback(boolean isLeft, int location) {
        if (fingerCount > 0)
            return;
        if (mListener == null)
            return;
        if(mLeftCursorIndex > mRightCursorIndex) {
            mLeftCursorIndex = mLeftCursorIndex + mRightCursorIndex;
            mRightCursorIndex = mLeftCursorIndex - mRightCursorIndex;
            mLeftCursorIndex = mLeftCursorIndex - mRightCursorIndex;

            mLeftCursorNextIndex = mLeftCursorNextIndex + mRightCursorNextIndex;
            mRightCursorNextIndex = mLeftCursorNextIndex - mRightCursorNextIndex;
            mLeftCursorNextIndex = mLeftCursorNextIndex - mRightCursorNextIndex;

            mLeftPointerLastX = mLeftPointerLastX + mRightPointerLastX;
            mRightPointerLastX = mLeftPointerLastX - mRightPointerLastX;
            mLeftPointerLastX = mLeftPointerLastX - mRightPointerLastX;

            Rect temp = mLeftCursorRect;
            mLeftCursorRect = mRightCursorRect;
            mRightCursorRect = temp;
        }

        if (lastL == getLeftIndex() && lastR == getRightIndex()) {
            return;
        }
        lastL = getLeftIndex();
        lastR = getRightIndex();

        mListener.onActionUpListener(this);
    }

    public void setLeftSelection(int partIndex) {
        if (partIndex >= mTextArray.length - 1 || partIndex <= 0) {
            throw new IllegalArgumentException("Index should from 0 to size of text array minus 2!");
        }

        if (partIndex != mLeftCursorIndex) {
            if (!mLeftScroller.isFinished()) {
                mLeftScroller.abortAnimation();
            }
            mLeftCursorNextIndex = partIndex;
            final int leftFromX = (int) (mLeftCursorIndex * mPartLength);
            mLeftScroller.startScroll(leftFromX, 0, mLeftCursorNextIndex * mPartLength - leftFromX, 0, mDuration);
            triggleCallback(true, mLeftCursorNextIndex);

            if (mRightCursorIndex <= mLeftCursorNextIndex) {
                if (!mRightScroller.isFinished()) {
                    mRightScroller.abortAnimation();
                }
                mRightCursorNextIndex = mLeftCursorNextIndex + 1;
                final int rightFromX = (int) (mRightCursorIndex * mPartLength);
                mRightScroller.startScroll(rightFromX, 0, mRightCursorNextIndex * mPartLength - rightFromX, 0, mDuration);
                triggleCallback(false, mRightCursorNextIndex);
            }

            postInvalidate();
        }
    }

    public void setRightSelection(int partIndex) {
        if (partIndex >= mTextArray.length || partIndex <= 0) {
            throw new IllegalArgumentException("Index should from 1 to size of text array minus 1!");
        }

        if (partIndex != mRightCursorIndex) {
            if (!mRightScroller.isFinished()) {
                mRightScroller.abortAnimation();
            }

            mRightCursorNextIndex = partIndex;
            final int rightFromX = (int) (mPartLength * mRightCursorIndex);
            mRightScroller.startScroll(rightFromX, 0, mRightCursorNextIndex * mPartLength - rightFromX, 0, mDuration);
            triggleCallback(false, mRightCursorNextIndex);

            if (mLeftCursorIndex >= mRightCursorNextIndex) {
                if (!mLeftScroller.isFinished()) {
                    mLeftScroller.abortAnimation();
                }

                mLeftCursorNextIndex = mRightCursorNextIndex - 1;
                final int leftFromX = (int) (mLeftCursorIndex * mPartLength);
                mLeftScroller.startScroll(leftFromX, 0, mLeftCursorNextIndex * mPartLength - leftFromX, 0, mDuration);
                triggleCallback(true, mLeftCursorNextIndex);
            }
            invalidate();
        }
    }

    public void setLeftCursorBackground(Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Do you want to make left cursor invisible?");
        }

        mLeftCursorBG = drawable;

        requestLayout();
        invalidate();
    }

    public void setLeftCursorBackground(int resID) {
        if (resID < 0) {
            throw new IllegalArgumentException("Do you want to make left cursor invisible?");
        }

        mLeftCursorBG = getResources().getDrawable(resID);

        requestLayout();
        invalidate();
    }

    public void setRightCursorBackground(Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Do you want to make right cursor invisible?");
        }

        mRightCursorBG = drawable;

        requestLayout();
        invalidate();
    }

    public void setRightCursorBackground(int resID) {
        if (resID < 0) {
            throw new IllegalArgumentException("Do you want to make right cursor invisible?");
        }

        mRightCursorBG = getResources().getDrawable(resID);

        requestLayout();
        invalidate();
    }

    public void setTextMarkColorNormal(int color) {
        if (color == Color.TRANSPARENT) {
            throw new IllegalArgumentException("Do you want to make text mark invisible?");
        }

        mTextColorNormal = color;

        invalidate();
    }

    public void setTextMarkColorSelected(int color) {
        if (color == Color.TRANSPARENT) {
            throw new IllegalArgumentException("Do you want to make text mark invisible?");
        }

        mTextColorSelected = color;

        invalidate();
    }

    public void setSeekbarColorNormal(int color) {
        if (color == Color.TRANSPARENT) {
            throw new IllegalArgumentException("Do you want to make seekbar invisible?");
        }

        mSeekbarColorNormal = color;

        invalidate();
    }

    public void setSeekbarColorSelected(int color) {
        if (color <= 0 || color == Color.TRANSPARENT) {
            throw new IllegalArgumentException("Do you want to make seekbar invisible?");
        }

        mSeekbarColorSelected = color;

        invalidate();
    }

    /**
     * In pixels. Users should call this method before view is added to parent.
     *
     * @param height
     */
    public void setSeekbarHeight(int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Height of seekbar can not less than 0!");
        }

        mSeekbarHeight = height;
    }

    /**
     * To set space between text mark and seekbar.
     *
     * @param space
     */
    public void setSpaceBetween(int space) {
        if (space < 0) {
            throw new IllegalArgumentException("Space between text mark and seekbar can not less than 0!");
        }

        mMarginBetween = space;

        requestLayout();
        invalidate();
    }

    /**
     * This method should be called after {@link #setTextMarkSize(int)}, because
     * view will measure size of text mark by paint.
     *
     * @param size
     */
    public void setTextMarks(String[] marks) {
        if (marks == null || marks.length == 0) {
            throw new IllegalArgumentException("Text array is null, how can i do...");
        }

        mTextArray = marks;
        mLeftCursorIndex = 0;
        mRightCursorIndex = mTextArray.length - 1;
        mRightCursorNextIndex = (int) mRightCursorIndex;
        mTextWidthArray = new float[marks.length];
        mClickRectArray = new Rect[mTextArray.length];
        initTextWidthArray();

        requestLayout();
        invalidate();
    }

    /**
     * Users should call this method before view is added to parent.
     *
     * @param size
     *            in pixels
     */
    public void setTextMarkSize(int size) {
        if (size < 0) {
            return;
        }

        mTextSize = size;
        mPaint.setTextSize(size);
    }

    public int getLeftCursorIndex() {
        return (int) Math.rint(mLeftCursorIndex);
    }

    public int getRightCursorIndex() {
        return (int) Math.rint(mRightCursorIndex);
    }

    public int getLeftIndex() {
        return getLeftCursorIndex();
    }

    public int getRightIndex() {
        return getRightCursorIndex();
    }

    public void setData(String[] args) {
        setTextMarks(args);
    }

    public void setOnUxinRangeBarActionUpListener(OnUxinRangeBarActionUpListener l) {
        mListener = l;
    }

    public interface OnUxinRangeBarActionUpListener {
        void onActionUpListener(RangeBar seekbar);
    }

    public void setThumbIndices(int left, int right) {
        if (left < 0 || left > right)
            left = 0;
        if (right < left || right >= mTextArray.length - 1)
            right = mTextArray.length - 1;
        mLeftCursorIndex = left;
        mRightCursorIndex = right;
        invalidate();
    }
}
