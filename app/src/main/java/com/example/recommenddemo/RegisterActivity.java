package com.example.recommenddemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import com.example.recommenddemo.base.BaseActivity;
import com.example.recommenddemo.databinding.ActivityRegisterBinding;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.MessageDigest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegisterActivity extends BaseActivity {
    ActivityRegisterBinding registerBinding;
    private ProgressDialog mDialog;
    private Retrofit mRetrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(registerBinding.getRoot());
        registerBinding.register.setOnClickListener(v -> {
            //客服端防止sql注入
            String newUserName = StringHandle(registerBinding.registerUsername.getText().toString());
            String newPassword= StringHandle(registerBinding.registerPassword1.getText().toString());
            String confirmPassword= StringHandle(registerBinding.registerPassword2.getText().toString());
            //判断注册的信息是否为空.
            if(newUserName.trim().equals("")||newPassword.trim().equals("")||confirmPassword.trim().equals("")){
                Toast.makeText(getApplicationContext(),"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                registerBinding.registerUsername.setText("");
                registerBinding.registerPassword1.setText("");
                registerBinding.registerPassword2.setText("");
            //判断俩次输入的密码是否一致
            }else if (!newPassword.trim().equals(confirmPassword.trim())){
                Toast.makeText(getApplicationContext(),"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                registerBinding.registerPassword1.setText("");
                registerBinding.registerPassword2.setText("");
            }else {
                SharedPreferences sp = getSharedPreferences("userdata",0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("username", newUserName);
                editor.putString("password", newPassword);
                editor.apply();
                mDialog = new ProgressDialog(RegisterActivity.this);
                mDialog.setTitle("提升");
                mDialog.setMessage("用户创建中...");
                mDialog.show();
                //String newPassword = md5(registerBinding.registerPassword1.getText().toString());
                registerServer(newUserName, newPassword);
              /*  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        registerServer(newUserName, newPassword);
                    }
                }).start();*/
            }
        });
        //构建Retrofit实例
        mRetrofit = new Retrofit.Builder()
                //设置网络请求BaseUrl地址
                //.baseUrl("https://www.23txt.com/")
                .baseUrl(APPCONST.urlBase)
                //设置数据解析器
                //.addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();
    }

    private void registerServer(String username, String password)
    {
        mRetrofit.create(RegisterApi.class)
                .registerUsers(username,password)
                .observe(RegisterActivity.this, new Observer<ResponseBody>() {
                    @Override
                    public void onChanged(ResponseBody responseBody) {
                        String body = null;
                        try {
                            body = responseBody.string();
                            Log.e(TAG,body);
                            //判断用户名重复
                            //203 账号存在 101 注册成功  204注册失败 300 数据库查询错误
                            if(body.equals("101")) {
                                handler.sendMessage(handler.obtainMessage(0));
                            }else if(body.equals("203")) {
                                handler.sendMessage(handler.obtainMessage(2));
                            }else if(body.equals("204")){
                                handler.sendMessage(handler.obtainMessage(1));
                            }else if(body.equals("300")){
                                handler.sendMessage(handler.obtainMessage(3));
                            }
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                });
        /*// 步骤5:创建网络请求接口对象实例
        RegisterApi api = mRetrofit.create(RegisterApi.class);
        //步骤6：对发送请求进行封装，传入接口参数
        //Call<ResponseBody> jsonDataCall = api.getData(searchKey);
                //api.getJsonData(searchKey);
        Call<ResponseBody> DataCall = api.registerUser(username,password);
        //步骤7:发送网络请求(异步)
        Log.e("TAG", "get == url：" + DataCall.request().url());
        DataCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                String body = null;
                try {
                    body = response.body().string();
                    Log.e("TAG",body);
                    //判断用户名重复
                    //203 账号存在 101 注册成功  204注册失败 300 数据库查询错误
                    assert body != null;
                    if(body.equals("101")) {
                        handler.sendMessage(handler.obtainMessage(0));
                    }else if(body.equals("203")) {
                        handler.sendMessage(handler.obtainMessage(2));
                    }else if(body.equals("204")){
                        handler.sendMessage(handler.obtainMessage(1));
                    }else if(body.equals("300")){
                        handler.sendMessage(handler.obtainMessage(3));
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.e("TAG", "get回调失败2：" + t.getMessage() + "," + t.toString());
                handler.sendMessage(handler.obtainMessage(1));
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
                case 0:
                    mDialog.cancel();
                    showDialog();
                    break;
                case 1:
                    mDialog.cancel();
                    Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    mDialog.cancel();
                    Toast.makeText(getApplicationContext(), "用户名已存在", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    mDialog.cancel();
                    Toast.makeText(getApplicationContext(), "数据库连接错误", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    mDialog.cancel();
                    Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    class RegisterThread implements Runnable
    {
        @Override
        public void run() {
            String username = registerBinding.registerUsername.getText().toString();
            //String password = md5(registerBinding.registerPassword1.getText().toString());
            String password = registerBinding.registerPassword1.getText().toString();
            registerServer(username, password);

        }

    }

    private void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("注册");
        builder.setMessage("注册成功");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //密码md5加密
    public static String md5(String str) {
        MessageDigest md5 = null;
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        }catch(Exception e)
        {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for(int i = 0; i < charArray.length; i++)
        {
            byteArray[i] = (byte)charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for( int i = 0; i < md5Bytes.length; i++)
        {
            int val = ((int)md5Bytes[i])&0xff;
            if(val < 16)
            {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
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
