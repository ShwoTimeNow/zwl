package com.example.zhangweilong.zwl.RadioGroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangweilong on 15/11/26.
 */
public class UnScrollRadioGroup extends RadioGroup implements RadioGroup.OnCheckedChangeListener{

    private Paint linePaint;
    private boolean mIndicatorScroll;
    private Map<Integer,Integer> map;
    private OnCheckedChangeListener onCheckedChangeListener;
    private int checkedIndex;
    private int mCurrentIndex;


    public UnScrollRadioGroup(Context context) {
        super(context);
        init();
    }
    public UnScrollRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        map = new HashMap<Integer,Integer>();
        mIndicatorScroll = true;
        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(5);
        setOnCheckedChangeListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus){
            int index = 0;
            for (int i = 0;i < getChildCount();i++){
                if (getChildAt(i) instanceof RadioButton){
                    map.put(getChildAt(i).getId(), index);
                    if (index == 0) {
                        ((RadioButton) getChildAt(i)).setChecked(true);
                    }
                    index ++;
                }
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
            canvas.drawLine(checkedIndex * getWidth() / getChildCount(), getHeight(), (checkedIndex + 1) * getWidth() / getChildCount(), getHeight(), linePaint);
    }

    public boolean ismIndicatorScroll() {
        return mIndicatorScroll;
    }

    public void setmIndicatorScroll(boolean mIndicatorScroll) {
        this.mIndicatorScroll = mIndicatorScroll;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getCheckedRadioButtonId() == checkedId && map.keySet().contains(checkedId)){
            //do myself thing
            checkedIndex = map.get(checkedId);
            invalidate();
            mCurrentIndex = checkedIndex;
            //this mothod can apply for out using (zwl)
            if (onCheckedChangeListener != null){
                onCheckedChangeListener.setCheckedTrue(group, checkedId);
            }
        }
    }

    public void setCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener){
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    interface OnCheckedChangeListener{
        void setCheckedTrue(RadioGroup group, int checkedId);
    }
}
