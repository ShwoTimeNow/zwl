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
public class RadioGroupActivity extends Activity implements ScrollRadioGroup.OnCheckedChangeListener{
    ScrollRadioGroup scrollRadioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radiogroup);
        scrollRadioGroup = (ScrollRadioGroup) findViewById(R.id.srg);
        scrollRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void setCheckedTrue(RadioGroup group, int checkedId) {
         Toast.makeText(this, ((RadioButton) findViewById(checkedId)).getText().toString(),Toast.LENGTH_LONG).show();
    }
}
