package com.example.zhangweilong.zwl.RadioGroup;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.zhangweilong.zwl.R;

/**
 * Created by zhangweilong on 15/11/26.
 */
public class RadioGroupActivity extends Activity implements UnScrollRadioGroup.OnCheckedChangeListener,RadioGroup.OnCheckedChangeListener{
    UnScrollRadioGroup unScrollRadioGroup;
    ScrollIndicator scrollIndicator;
    RadioGroup  rg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radiogroup);
        unScrollRadioGroup = (UnScrollRadioGroup) findViewById(R.id.srg);
        unScrollRadioGroup.setCheckedChangeListener(this);
        scrollIndicator = (ScrollIndicator)findViewById(R.id.srollIndicator);
        rg = (RadioGroup)findViewById(R.id.rg);
        scrollIndicator.setOnCheckedChangeListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            scrollIndicator.setRadioGroup(rg);
        }
    }

    @Override
    public void setCheckedTrue(RadioGroup group, int checkedId) {
         Toast.makeText(this, ((RadioButton) findViewById(checkedId)).getText().toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Toast.makeText(this, ((RadioButton) findViewById(checkedId)).getText().toString(),Toast.LENGTH_LONG).show();

    }
}
