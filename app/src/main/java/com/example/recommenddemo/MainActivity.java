package com.example.recommenddemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.recommenddemo.app.ActivityManage;
import com.example.recommenddemo.base.BaseActivity;
import com.example.recommenddemo.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {
    //定义一个变量，来标识是否退出
    private int isExit=0;
    ActivityMainBinding mainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        mainBinding.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil spu = new SharedPreferencesUtil(getApplicationContext());
                spu.clear();
            }
        });
    }

    //实现按两次后退才退出
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what) {
                case 9:
                    isExit--;
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            isExit++;
            exitApp();
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void exitApp(){
        if(isExit<2){
            Toast.makeText(getApplicationContext(),R.string.exit_app,Toast.LENGTH_SHORT).show();
            //利用handler延迟发送更改状态信息
            handler.sendMessageDelayed(handler.obtainMessage(9),2000);
            //handler.sendEmptyMessageDelayed(0,2000);
        }else{
            //super.onBackPressed();
            ActivityManage.finishAllActivities();
        }
    }

}