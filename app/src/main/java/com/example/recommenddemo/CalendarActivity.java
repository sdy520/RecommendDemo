package com.example.recommenddemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.recommenddemo.base.BaseActivity;
import com.example.recommenddemo.databinding.ActivityCalendarBinding;
import com.example.recommenddemo.room.CalendarHeNanPosDao;
import com.example.recommenddemo.room.CalendarHeNanPosDatabase;
import com.example.recommenddemo.ui.ChooseActivity;
import com.example.recommenddemo.ui.LoginActivity;
import com.example.recommenddemo.util.SharedPreferencesUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

public class CalendarActivity extends BaseActivity {
    ActivityCalendarBinding calendarBinding;
    private String[] daies = {"第一天","第二天","第三天","第四天","第五天","第六天","第七天","第八天","第九天","第十天","第十一天","第十二天"};
    private int calendarDay;
    CalendarViewpager2Adapter viewpager2Adapter;
    CalendarHeNanPosDao calendarHeNanPosDao;
    private SharedPreferencesUtil sp;
    int sendToNavigationPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendarBinding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(calendarBinding.getRoot());
        calendarHeNanPosDao = CalendarHeNanPosDatabase.getDatabase(this).getCalendarPosDao();
        sp = new SharedPreferencesUtil(getApplicationContext());
        calendarDay = (int) sp.getParam("day",0);
        viewpager2Adapter = new CalendarViewpager2Adapter(CalendarActivity.this,calendarDay);
        calendarBinding.viewpager.setAdapter(viewpager2Adapter);
        calendarBinding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                sendToNavigationPosition = position;
                super.onPageSelected(position);
            }
        });
        new TabLayoutMediator(calendarBinding.tablayout, calendarBinding.viewpager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                tab.setText(daies[position]);
            }
        }).attach();

        calendarBinding.topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent1;
                switch (item.getItemId()){
                    case R.id.exit:
                        sp.setParam("isRememberPassword",false);
                        sp.setParam("isAutoLogin",false);
                        intent1 = new Intent(CalendarActivity.this, LoginActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.reset:
                        sp.setParam("isChoose",false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                calendarHeNanPosDao.deleteAllCalendarHeNanPos();
                            }
                        }).start();
                        intent1 = new Intent(CalendarActivity.this, ChooseActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.reset_exit:
                        sp.clear();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                calendarHeNanPosDao.deleteAllCalendarHeNanPos();
                            }
                        }).start();
                        intent1 = new Intent(CalendarActivity.this, LoginActivity.class);
                        startActivity(intent1);
                        break;
                }
                return false;
            }
        });
        calendarBinding.extendedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(CalendarActivity.this,NavigationActivity.class);
                intent2.putExtra("sendToNavigationPosition",sendToNavigationPosition);
                startActivity(intent2);
            }
        });
        /*for(int w=0;w<7;w++){
            List<HeNanPos> heNanPosList1=clusters.getValue().get(w);
            for(int j=0;j< heNanPosList1.size();j++) {
                Log.e(TAG, heNanPosList1.get(j).getRecommend_score() + "shuliang" + j);
            }
        }*/
    }


}
