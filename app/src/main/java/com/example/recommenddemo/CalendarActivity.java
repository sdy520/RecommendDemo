package com.example.recommenddemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.viewpager.widget.ViewPager;

import com.example.recommenddemo.base.BaseActivity;
import com.example.recommenddemo.databinding.ActivityCalendarBinding;
import com.example.recommenddemo.room.HeNanPos;
import com.example.recommenddemo.viewmodel.ShareViewModelProvider;
import com.example.recommenddemo.viewmodel.SharedCalendarViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalendarActivity extends BaseActivity {
    ActivityCalendarBinding calendarBinding;
    LiveData<HashMap<Integer, List<HeNanPos>>> clusters;
    private String[] daies = {"第一天","第二天","第三天","第四天","第五天","第六天","第七天","第八天","第九天","第十天","第十一天","第十二天"};
    private int calendarDay;
    private List<Fragment> fragments = new ArrayList<>();
    private SharedCalendarViewModel sharedCalendarViewModel = ShareViewModelProvider.get(this,SharedCalendarViewModel.class);
    CalendarViewpager2Adapter viewpager2Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendarBinding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(calendarBinding.getRoot());
        clusters=sharedCalendarViewModel.getHashMapMutableLiveData();
        Intent intent =getIntent();
        calendarDay = intent.getIntExtra("day",0);
        viewpager2Adapter = new CalendarViewpager2Adapter(this,calendarDay);
        calendarBinding.viewpager.setAdapter(viewpager2Adapter);
        initView();

        /*for(int w=0;w<7;w++){
            List<HeNanPos> heNanPosList1=clusters.getValue().get(w);
            for(int j=0;j< heNanPosList1.size();j++) {
                Log.e(TAG, heNanPosList1.get(j).getRecommend_score() + "shuliang" + j);
            }
        }*/
    }

    private void initView() {

        //添加tabitem
        /*for(int i=0;i<calendarDay;i++)
        {
            calendarBinding.tablayout.addTab(calendarBinding.tablayout.newTab().setText(daies[i]));
        }*/
       /* for(int i=0;i<calendarDay;i++)
        {
            fragments.add(CalendarFragment.newInstance());
        }*/
        new TabLayoutMediator(calendarBinding.tablayout, calendarBinding.viewpager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                tab.setText(daies[position]);
            }
        }).attach();

    }
}
