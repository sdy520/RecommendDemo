package com.example.recommenddemo;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<T>> {

    private Type mResponseType;
    LiveDataCallAdapter(Type mResponseType) {
        this.mResponseType = mResponseType;

    }

    @NotNull
    @Override
    public Type responseType() {
        return mResponseType;
    }

    @NotNull
    @Override
    public LiveData<T> adapt(@NotNull final Call<T> call) {
        return new LiveData<T>() {
            private AtomicBoolean stat = new AtomicBoolean(false);
            @Override
            protected void onActive() {
                if(stat.compareAndSet(false, true)){
                    //请求入队，执行网络操作
                    call.enqueue(new Callback<T>() {
                        @Override
                        public void onResponse(Call<T> call, Response<T> response) {
                            //将返回的数据发送到liveData中去，订阅了这个liveData的都可以收到
                            T body = response.body();
                            postValue(body);
                        }
                        @Override
                        public void onFailure(Call<T> call, Throwable t) {
                            postValue(null);
                        }
                    });
                }
            }
        };
    }

}

