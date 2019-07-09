package com.lcs.eventbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lcs.library.EventBus;
import com.lcs.library.Subscrible;
import com.lcs.library.ThreadMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        startActivity(new Intent(this, Main2Activity.class));
    }

    @Subscrible(threadMode = ThreadMode.MAIN)
    public void a(EventBean eventBean) {
        Log.e("MainActivity", eventBean.toString());
    }
}
