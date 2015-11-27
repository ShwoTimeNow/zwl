package com.example.zhangweilong.zwl.RadioGroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.zhangweilong.zwl.R;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangweilong on 15/11/27.
 */
public class ScrollIndicator extends LinearLayout implements RadioGroup.OnCheckedChangeListener{


    private Map<Integer,Integer> map;
    private int startX,startY,endX,endY;
    private float indicatorHeight;
    private int indicatorColor;
    private View indicatorLine;
    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener;
    private RadioGroup radioGroup;
    private int mCurrentIndex;
    private  int width;
    private float oldRato;

    public ScrollIndicator(Context context) {
        super(context,null);
    }

    public ScrollIndicator(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

    }

    public ScrollIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs == null)
            return;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollIndicator);
        indicatorHeight = a.getDimension(R.styleable.ScrollIndicator_indicatorHeight,6);
        indicatorColor = a.getColor(R.styleable.ScrollIndicator_indicatorColor, Color.RED);
        a.recycle();
    }


    public void setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener onCheckedChangeListener){
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public void setRadioGroup(RadioGroup radioGroup){
        map = new HashMap<Integer, Integer>();
        oldRato = 1f;
        indicatorLine = getChildAt(0);
        width = radioGroup.getWidth()/radioGroup.getChildCount();
        ViewGroup.LayoutParams lp = indicatorLine.getLayoutParams();
        lp.width = width;
        indicatorLine.setLayoutParams(lp);
        this.radioGroup = radioGroup;
        this.radioGroup.setOnCheckedChangeListener(this);
        Log.e("zwl"," radioGroup.getWidth():"+ radioGroup.getWidth()+",getChildCount"+radioGroup.getChildCount());
        int index = 0;
        for (int i = 0;i < radioGroup.getChildCount();i++){
            if (radioGroup.getChildAt(i) instanceof  RadioButton){
                map.put(radioGroup.getChildAt(i).getId(),index);
                index ++;
            }
        }
        this.indicatorLine = getChildAt(0);
        ((RadioButton)this.radioGroup.getChildAt(0)).setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (map.keySet().contains(checkedId)&&group.getCheckedRadioButtonId() == checkedId){
            /**
             * 获取checked的radiobutton的文字长度
             */
            RadioButton checkedRadioButton = (RadioButton) group.getChildAt(map.get(checkedId));
            float textWidthChecked = checkedRadioButton.getPaint().measureText(checkedRadioButton.getText().toString());
            /**
             * 获取上一次选中的radiobutotn的文字长度
             */
            RadioButton radioButton = (RadioButton) group.getChildAt(mCurrentIndex);
            float textWidthCurrent = radioButton.getPaint().measureText(radioButton.getText().toString());

            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(indicatorLine,"translationX",mCurrentIndex*width,map.get(checkedId)*width);
            ObjectAnimator animator = ObjectAnimator.ofFloat(indicatorLine, "scaleX",oldRato, textWidthChecked/textWidthCurrent );
            AnimatorSet set = new AnimatorSet();
            set.playTogether(objectAnimator,animator);
            set.setDuration(500);
            set.start();
            oldRato = textWidthChecked/textWidthCurrent;
            mCurrentIndex = map.get(checkedId);
        }
        if (onCheckedChangeListener != null){
            onCheckedChangeListener.onCheckedChanged(group, checkedId);
        }
    }
}
