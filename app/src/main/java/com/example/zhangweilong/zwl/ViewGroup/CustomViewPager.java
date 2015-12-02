package com.example.zhangweilong.zwl.ViewGroup;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by zhangweilong on 15/12/1.
 * {#link http://www.jianshu.com/p/525ccf61db94#}
 *
 */
public class CustomViewPager extends ViewGroup {

    private final static int SCROLL_STATE_DRAGGING = 0x1;
    private final static int LEFT_TO_RIGHT = 0x1;
    private final static int RIGHT_TO_LEFT = 0x2;
    private final static int SNAP_VELOCITY = 1;
    private final static int MAX_SETTLE_DURATION = 200;
    private static final int SCROLL_STATE_SETTLING = 0x2;


    private int marginLeftRight;//view试图两边的距离
    private int mGutterSize;//缩小后与原来view图的距离
    private int mSwitchSize;//切换一个page需要滑动的距离
    private int SCALE_RATIO;//缩放比例

    private VelocityTracker mVelocityTracker;
    private int mActivePointerId;
    private float mDownX;
    private Scroller mScroller;
    private ViewPager.OnPageChangeListener mOnPagerChangeListener;
    private int mCurrentPosition;
    private float mMaximumVelocity =10;
    private int mCurrentDir;


    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //viewgroup充满整个屏幕
        setMeasuredDimension(getDefaultSize(0,widthMeasureSpec),getDefaultSize(0,heightMeasureSpec));

        //viewGroup的宽高
        int viewGroupWidth  = getMeasuredWidth();;
        int viewGroupHeight = getMeasuredHeight();

        int childCount = getChildCount();

        //子view的宽度
        int childWidth = viewGroupWidth - marginLeftRight*2;
        int childHeight = viewGroupHeight;

        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth,MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight,MeasureSpec.EXACTLY);
        for (int i = 0; i < childCount;i++){
            getChildAt(i).measure(childWidthMeasureSpec,childHeightMeasureSpec);
        }


        //切换一个page需要移动的距离为一个page的宽度
        mSwitchSize = childWidth;
        //确定缩放比例
        confirmScaleRatio(childWidth, mGutterSize);
    }

    private void confirmScaleRatio(int childWidth, int mGutterSize) {
        SCALE_RATIO = (childWidth - mGutterSize * 2) / childWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int originLeft =  marginLeftRight;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int left = originLeft + child.getMeasuredWidth() * i;
            int right = originLeft + child.getMeasuredWidth() * (i + 1);
            int bottom = child.getMeasuredHeight();
            child.layout(left, 0, right, bottom);
            if (i != 0) {
                child.setScaleX(SCALE_RATIO);
                child.setScaleY(SCALE_RATIO);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int actionIndex = MotionEventCompat.getActionIndex(event);
        mActivePointerId = MotionEventCompat.getPointerId(event, 0);

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getRawX();
                if (mScroller != null && !mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:

                //calculate moving distance
                float distance = -(event.getRawX() - mDownX);
                mDownX = event.getRawX();
                performDrag((int)distance);
                break;
            case MotionEvent.ACTION_UP:
                releaseViewForTouchUp();
//                cancel();
                break;
        }
        return true;
    }

    private void performDrag(int distance) {
        if (mOnPagerChangeListener != null){
            mOnPagerChangeListener.onPageScrollStateChanged(SCROLL_STATE_DRAGGING);
        }
        scrollBy(distance, 0);
        if (distance < 0) {
            dragScaleShrinkView(mCurrentPosition, LEFT_TO_RIGHT);
        } else {
            dragScaleShrinkView(mCurrentPosition, RIGHT_TO_LEFT);
        }
    }


    private void dragScaleShrinkView(int position,int flag){
        int moveSize = getScrollX() - position * mSwitchSize;
        float ratio = (float) moveSize / mSwitchSize;
        float scaleRatio = SCALE_RATIO + (1.0f - SCALE_RATIO) * ratio;
        float shrinkRatio = 1.0f - (1.0f - SCALE_RATIO) * ratio;

        View scaleView = getChildAt(position);
        View shrinkView;
        if (flag == LEFT_TO_RIGHT){
            shrinkView = getChildAt(position - 1);
        }else {
            shrinkView = getChildAt(position +1);
        }

        //放大
        ViewCompat.setScaleX(scaleView, scaleRatio);
        ViewCompat.setScaleY(scaleView, scaleRatio);
        scaleView.invalidate();
        //缩小
        ViewCompat.setScaleX(shrinkView,shrinkRatio);
        ViewCompat.setScaleY(shrinkView, shrinkRatio);
        shrinkView.invalidate();
    }


    private void releaseViewForTouchUp() {

        final VelocityTracker velocityTracker = mVelocityTracker;
        velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
        int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(
                velocityTracker, mActivePointerId);
        float xVel = mVelocityTracker.getXVelocity();
        //向左滑动，速度大于限定的值滑动到下一个页面
        if (xVel > SNAP_VELOCITY && mCurrentPosition > 0) {
            smoothScrollToItemView(mCurrentPosition - 1, true);
            //向右滑动时，速度为负数，所以当小于限定值的负数滑动到上一个页面
        } else if (xVel < -SNAP_VELOCITY && mCurrentPosition < getChildCount() - 1) {
            smoothScrollToItemView(mCurrentPosition + 1, true);
        } else {
            //没有达到一定的速度，根据移动的距离确定滑动到哪个页面
            smoothScrollToDes();
        }
        setScrollState(SCROLL_STATE_SETTLING);
    }

    private void setScrollState(int i){

    }

    private void smoothScrollToDes() {
        //整个ViewGroup已经滑动的距离
        int scrollX = getScrollX();
        //确定滑动到哪个页面，mSwitchSize是切换一个页面ViewGroup需要滑动的距离
        //如果超过一半滚动到下一页，如果没有超过一半滚动原来的页
        int position = (scrollX + mSwitchSize / 2) / mSwitchSize;
        smoothScrollToItemView(position, mCurrentPosition == position);
    }

    private void smoothScrollToItemView(int position, boolean pageSelected) {
        mCurrentPosition = position;
        if (mCurrentPosition > getChildCount() - 1) {
            mCurrentPosition = getChildCount() - 1;
        }
        if (mOnPagerChangeListener != null && pageSelected){
            mOnPagerChangeListener.onPageSelected(position);
        }
        //确定滑动的距离
        int dx = position * (getMeasuredWidth() -  marginLeftRight * 2) - getScrollX();
        if (dx > 0){
            mCurrentDir = LEFT_TO_RIGHT;
        }else {
            mCurrentDir = RIGHT_TO_LEFT;
        }
        mScroller.startScroll(getScrollX(), 0, dx, 0, Math.min(Math.abs(dx) * 2, MAX_SETTLE_DURATION));
        invalidate();
    }


    @Override
    public void computeScroll() {
        if (!mScroller.isFinished() && mScroller.computeScrollOffset()) {
            dragScaleShrinkView(mCurrentPosition, mCurrentDir);
            scrollTo(mScroller.getCurrX(), 0);
        }

    }
}
