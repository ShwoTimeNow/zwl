package com.example.zhangweilong.zwl;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by zhangweilong on 15/11/26.
 */
public class OnClickLinearLayout extends LinearLayout {
    public OnClickLinearLayout(Context context) {
        super(context);
    }

    public OnClickLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnClickLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus){
            for (int i = 0; i < getChildCount();i++){
                final int finalI = i;
                String name = ((Button)getChildAt(i)).getText().toString();
                final String className = "com.example.zhangweilong.zwl."+name+"."+name+"Activity";
                ((Button)getChildAt(i)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClassName(getContext(),className);
                        getContext().startActivity(intent);
                    }
                });
            }
        }
    }
}
