package com.example.zhangweilong.zwl.PropertyAnimation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.zhangweilong.zwl.R;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhangweilong on 15/11/26.
 */
public class PropertyAnimationActivity extends Activity {


    @InjectView(R.id.tv1)
    TextView tv1;
    @InjectView(R.id.tv2)
    TextView tv2;
    @InjectView(R.id.tv3)
    TextView tv3;
    @InjectView(R.id.tv4)
    TextView tv4;
    @InjectView(R.id.tv5)
    TextView tv5;
    @InjectView(R.id.tv6)
    TextView tv6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        ButterKnife.inject(this);
    }

    public void startAnimation(View view){
        /**
         * objectAnimation together
         */
        ObjectAnimator translateX = ObjectAnimator.ofFloat(tv1, "translationX", 200, 500);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(tv1, "scaleX", 4f, 0.5f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(translateX,scaleX);
        set.setDuration(500);
        set.start();
        /**
         * objectAnimation with before after
         * */
        ObjectAnimator translationX = ObjectAnimator.ofFloat(tv2,"translationX",0,600,300);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(tv2,"alpha",1f,0f,0.5f);
        AnimatorSet set1 = new AnimatorSet();
        set1.setDuration(2000);
        set1.play(translationX).after(alpha);
        set1.start();
        /**
        * objectAnimation rotation
        * */
        ObjectAnimator rotation = ObjectAnimator.ofFloat(tv3,"rotation",0,-360);
        rotation.setDuration(1000);
        rotation.start();
        //rotation never stop
        ObjectAnimator rotation1 = ObjectAnimator.ofFloat(tv4,"rotation",0,360);
        rotation1.setRepeatCount(ValueAnimator.INFINITE);
        rotation1.setRepeatMode(ValueAnimator.REVERSE);
        rotation1.setDuration(2000);
        rotation1.start();
        //rotation pivotX pivotY
        ObjectAnimator rotation2 = ObjectAnimator.ofFloat(tv5,"rotation",0,360);
        rotation2.setRepeatCount(ValueAnimator.INFINITE);
        rotation2.setRepeatMode(ValueAnimator.RESTART);
        tv5.setPivotX(tv5.getMeasuredWidth());
        tv5.setPivotY(tv5.getMeasuredHeight());
        rotation2.setDuration(3000);
        rotation2.start();
        //rotationX rotationY
        ObjectAnimator rotationX = ObjectAnimator.ofFloat(tv6,"rotationX",0,90,0,180,0,360,0);
        rotationX.setDuration(10000);
        rotationX.start();

    }

    public void damajia(View view){
        startActivity(new Intent(this,MyActivity.class));
    }

}
