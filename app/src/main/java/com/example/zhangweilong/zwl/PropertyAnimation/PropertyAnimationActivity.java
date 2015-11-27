package com.example.zhangweilong.zwl.PropertyAnimation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.zhangweilong.zwl.R;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhangweilong on 15/11/26.
 */
public class PropertyAnimationActivity extends Activity {


    @InjectView(R.id.tv1)
    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        ButterKnife.inject(this);
    }

    public void startAnimation(View view){
        ObjectAnimator translateX = ObjectAnimator.ofFloat(tv1, "translationX", 200, 500);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(tv1, "scaleX", 4f, 0.5f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(translateX,scaleX);
        set.setDuration(500);
        set.start();
    }

}
