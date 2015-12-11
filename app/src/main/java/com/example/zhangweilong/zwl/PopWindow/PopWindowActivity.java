package com.example.zhangweilong.zwl.PopWindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.zhangweilong.zwl.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhangweilong on 15/12/8.
 */
public class PopWindowActivity extends Activity implements View.OnClickListener {

    @InjectView(R.id.bt1)
    Button bt1;
    @InjectView(R.id.bt2)
    Button bt2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popwindow);
        ButterKnife.inject(this);
        bt1 = (Button) findViewById(R.id.bt1);
        bt1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt1:
                cornerPopWindow(v);
                break;
            case R.id.bt2:
                gridViewPopWindow(v);
                break;
        }
    }

    private void gridViewPopWindow(View v) {


    }

    private void cornerPopWindow(View v) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(this);
        View cornerView = mLayoutInflater.inflate(R.layout.cornerpopwindow, null);
        PopupWindow pw = new PopupWindow(cornerView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //背景变暗
        /**
         * shadow效果太快，没有缓慢的动画效果
         * WindowManager.LayoutParams lp = getWindow().getAttributes();
         * lp.alpha = 0.4f;
         * getWindow().setAttributes(lp);
         */
        final View view = new View(this);
        view.setBackgroundColor(getResources().getColor(R.color.red_df8a67));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = -1;
        lp.height = -1;
        lp.windowAnimations = R.style.PopupWindowAnimationFadeIn;
        WindowManager windowManager = getWindowManager();
        windowManager.addView(view,lp);

        //弹出PopupWindow
        ColorDrawable backColor = new ColorDrawable(0x00000000);
        pw.setBackgroundDrawable(backColor);
        pw.setOutsideTouchable(true);
        pw.setFocusable(true);
        pw.setAnimationStyle(R.style.PopupWindowAnimationFade);
        pw.showAtLocation((View) v.getParent(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

        pw.update();
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                /**
                 * WindowManager.LayoutParams lp = getWindow().getAttributes();
                 * lp.alpha = 1f;
                 * getWindow().setAttributes(lp);
                 */
                Animation animation = AnimationUtils.loadAnimation(PopWindowActivity.this,R.anim.abc_fade_out);
                view.setAnimation(animation);
            }
        });
    }
}
