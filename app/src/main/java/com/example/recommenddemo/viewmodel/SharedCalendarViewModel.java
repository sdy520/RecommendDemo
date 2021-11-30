package com.example.recommenddemo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recommenddemo.room.HeNanPos;

import java.util.HashMap;
import java.util.List;

public class SharedCalendarViewModel extends ViewModel {
    private MutableLiveData<HashMap<Integer, List<HeNanPos>>> hashMapMutableLiveData =new MutableLiveData<>();
    public void setHashMapMutableLiveData(HashMap<Integer, List<HeNanPos>> hashMap){
        hashMapMutableLiveData.setValue(hashMap);
    }
    public LiveData<HashMap<Integer, List<HeNanPos>>> getHashMapMutableLiveData(){
        return hashMapMutableLiveData;
    }
}
