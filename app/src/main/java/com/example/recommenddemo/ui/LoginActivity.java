package com.example.recommenddemo.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import com.example.recommenddemo.ChooseActivity;
import com.example.recommenddemo.retrofit.LiveDataCallAdapterFactory;
import com.example.recommenddemo.api.LoginApi;
import com.example.recommenddemo.base.BaseActivity;
import com.example.recommenddemo.config.APPCONST;
import com.example.recommenddemo.databinding.ActivityLoginBinding;
import com.example.recommenddemo.util.SharedPreferencesUtil;


import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding loginBinding;
    private String account;
    private String password;
    private Retrofit mRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
        //构建Retrofit实例
        mRetrofit = new Retrofit.Builder()
                //设置网络请求BaseUrl地址
                .baseUrl(APPCONST.urlBase)
                //设置数据解析器
                //.addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();
        // 自动填充
        SharedPreferencesUtil spu = new SharedPreferencesUtil(this);
        Boolean isRememberPassword = (Boolean) spu.getParam("isRememberPassword",false);
        Boolean isAutoLogin = (Boolean) spu.getParam("isAutoLogin",false);
        // SharedPreference获取用户账号密码，存在则填充
        account = (String) spu.getParam("account","");
        password = (String)spu.getParam("password","");
        if(!account.equals("") && !password.equals("")){
            if(isRememberPassword){
                loginBinding.loginUsername.setText(account);
                loginBinding.loginPassword.setText(password);
                loginBinding.loginRememberPassword.setChecked(true);
            }
            if(isAutoLogin) {
                loginBinding.loginRememberAutologin.setChecked(true);
                Login();
            }

        }


        loginBinding.loginRegister.setOnClickListener(v -> {
            Intent intent =new Intent();
            intent.setClass(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        loginBinding.login.setOnClickListener(v -> {
            Login();
        });
    }

    private void Login() {
        //客服端防止sql注入
        password = StringHandle(loginBinding.loginPassword.getText().toString());
        account = StringHandle(loginBinding.loginUsername.getText().toString());
        if(account.trim().equals("") || password.trim().equals("")) {
            Toast.makeText(getApplicationContext(),"用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        loginBinding.allControl.setVisibility(View.GONE);
        loginBinding.pbLoading.setVisibility(View.VISIBLE);
        OptionHandle(account,password);// 处理自动登录及记住密码
        registerServer(account,password);
    }

    private void registerServer(String username, String password)
    {
        mRetrofit.create(LoginApi.class)
                .loginUsers(username,password)
                .observe(LoginActivity.this, new Observer<ResponseBody>() {
                    @Override
                    public void onChanged(ResponseBody responseBody) {
                        String body = null;
                        try {
                            body = responseBody.string();
                            Log.e(TAG,body);
                            if(body.equals("100")) {
                                handler.sendMessage(handler.obtainMessage(1));
                            }else if(body.equals("201")) {
                                handler.sendMessage(handler.obtainMessage(2));
                            }else if(body.equals("202")){
                                handler.sendMessage(handler.obtainMessage(3));
                            }else if(body.equals("300")){
                                handler.sendMessage(handler.obtainMessage(4));
                            }
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                });
       /* // 步骤5:创建网络请求接口对象实例
        LoginApi api = mRetrofit.create(LoginApi.class);
        //步骤6：对发送请求进行封装，传入接口参数
        //Call<ResponseBody> jsonDataCall = api.getData(searchKey);
        //api.getJsonData(searchKey);
        Call<ResponseBody> DataCall = api.loginUser(username,password);
        //步骤7:发送网络请求(异步)
        Log.e("TAG", "get == url：" + DataCall.request().url());
        DataCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                String body = null;
                try {
                    body = response.body().string();
                    Log.e("TAG",body);
                    if(body.equals("100")) {
                        handler.sendMessage(handler.obtainMessage(1));
                    }else if(body.equals("201")) {
                        handler.sendMessage(handler.obtainMessage(2));
                    }else if(body.equals("202")){
                        handler.sendMessage(handler.obtainMessage(3));
                    }else if(body.equals("300")){
                        handler.sendMessage(handler.obtainMessage(4));
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.e("TAG", "get回调失败2：" + t.getMessage() + "," + t.toString());
                handler.sendMessage(handler.obtainMessage(5));
            }
        });*/
    }

    //Handler
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    loginBinding.pbLoading.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(LoginActivity.this, ChooseActivity.class);
                    startActivity(intent1);
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "密码错误,请重新输入",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "账号不存在,请注册",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "数据库连接错误", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void OptionHandle(String username,String pwd){
        //SharedPreferences.Editor editor = getSharedPreferences("UserData",MODE_PRIVATE).edit();
        SharedPreferencesUtil spu = new SharedPreferencesUtil(this);
        if(loginBinding.loginRememberPassword.isChecked()){
            spu.setParam("isRememberPassword",true);
            //editor.putBoolean("isRememberPassword",true);
            // 保存账号密码
            spu.setParam("account",username);
            spu.setParam("password",pwd);
        }else{
            spu.setParam("isRememberPassword",false);
            //editor.putBoolean("isRememberPassword",false);
        }

        if(loginBinding.loginRememberAutologin.isChecked()){
            spu.setParam("isAutoLogin",true);
            //editor.putBoolean("isAutoLogin",true);
        }else{
            spu.setParam("isAutoLogin",false);
            //editor.putBoolean("isAutoLogin",false);
        }
        //editor.apply();
    }

    /**
     * 字符串处理，防止SQL注入
     */
    public static String StringHandle(String input){
        String output;
        // 将包含有 单引号(')的语句替换成A #替换成B
        output = input.trim().replaceAll("'", "A");
        output = output.replaceAll("#", "B");
        // 将包含有 单引号(')，分号(;) 和 注释符号(--)的语句替换掉
        //output = input.trim().replaceAll(".*([';]+|(--)+).*", " ");
        return output;
    }

}
