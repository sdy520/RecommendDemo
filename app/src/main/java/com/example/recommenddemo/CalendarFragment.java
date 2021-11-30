package com.example.recommenddemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recommenddemo.databinding.FragmentCalendarBinding;
import com.example.recommenddemo.room.HeNanPos;
import com.example.recommenddemo.viewmodel.ShareViewModelProvider;
import com.example.recommenddemo.viewmodel.SharedCalendarViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class CalendarFragment extends Fragment {
    FragmentCalendarBinding fragmentCalendarBinding;
    CalendarActivity calendarActivity;
    Activity activity;
    CalendarAdapter calendarAdapter;
    LiveData<HashMap<Integer, List<HeNanPos>>> clusters;
    int position;
    public CalendarFragment(){}
    public CalendarFragment(int position){
        this.position = position;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendarActivity = (CalendarActivity) getContext();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        fragmentCalendarBinding = FragmentCalendarBinding.inflate(inflater,container,false);
        SharedCalendarViewModel sharedCalendarViewModel = ShareViewModelProvider.get(this,SharedCalendarViewModel.class);
        clusters = sharedCalendarViewModel.getHashMapMutableLiveData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        fragmentCalendarBinding.CalendarData.setLayoutManager(linearLayoutManager);
        calendarAdapter = new CalendarAdapter(activity,clusters.getValue().get(position));
        Log.e("TAG", position+"");
        calendarAdapter.notifyDataSetChanged();
        fragmentCalendarBinding.CalendarData.setAdapter(calendarAdapter);
        calendarAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        return fragmentCalendarBinding.getRoot();

    }
}
