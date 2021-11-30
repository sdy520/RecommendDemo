package com.example.recommenddemo.retrofit;

import android.util.Log;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    private static final String TAG = LiveDataCallAdapterFactory.class.getSimpleName();

    @SuppressWarnings("ClassGetClass")
    @Nullable
    @Override
    public CallAdapter<?, ?> get(@NotNull Type returnType, @NotNull Annotation[] annotations, @NotNull Retrofit retrofit) {
        if (getRawType(returnType) != LiveData.class) {
            return null;
        }
        //获取第一个泛型类型
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawType = getRawType(observableType);
        Log.e(TAG, "rawType = " + rawType.getClass().getSimpleName());
        //boolean isApiResponse = true;
        if (rawType != ResponseBody.class) {
            throw new IllegalArgumentException("type must be a resource");
            //不是返回ApiResponse类型的返回值
            //isApiResponse = false;
        }
        if (observableType instanceof ParameterizedType) {
            throw new IllegalArgumentException("resource must be parameterized");
        }
        return new LiveDataCallAdapter<>(observableType);
    }

}
