package com.example.recommenddemo.base;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recommenddemo.app.ActivityManage;


public class BaseActivity extends AppCompatActivity {
    //获取TAG的activity名称
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将每一个Activity都加入Activity管理器
        ActivityManage.addActivity(this);
        Log.d("ActivityName: ",getLocalClassName());
    }

    @Override
    protected void onDestroy() {
        ActivityManage.removeActivity(this);
        super.onDestroy();

    }

}
