package com.example.recommenddemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recommenddemo.databinding.FragmentCalendarBinding;
import com.example.recommenddemo.room.CalendarHeNanPosDao;
import com.example.recommenddemo.room.CalendarHeNanPosDatabase;
import com.example.recommenddemo.room.HeNanPos;
import com.example.recommenddemo.util.SharedPreferencesUtil;
import com.example.recommenddemo.viewmodel.ShareViewModelProvider;
import com.example.recommenddemo.viewmodel.SharedCalendarViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class CalendarFragment extends Fragment {
    private static final String TAG = "CalendarFragment";
    FragmentCalendarBinding fragmentCalendarBinding;
    CalendarActivity calendarActivity;
    Activity activity;
    CalendarAdapter calendarAdapter;
    MutableLiveData<HashMap<Integer, List<HeNanPos>>> clusters = new MutableLiveData<>();
    int calendarPosition;
    CalendarHeNanPosDao calendarHeNanPosDao;
    HashMap<Integer, List<HeNanPos>> localClusters = new HashMap<>();
    private SharedCalendarViewModel sharedCalendarViewModel = ShareViewModelProvider.get(this,SharedCalendarViewModel.class);
    //记下要删除的日程数据库的元素
    HeNanPos deleteHeNanPos;
    boolean deleteHeNanPosFlag =false;
    public CalendarFragment(){}
    public CalendarFragment(int position){
        calendarPosition = position;
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
        calendarHeNanPosDao = CalendarHeNanPosDatabase.getDatabase(activity).getCalendarPosDao();
        SharedPreferencesUtil sp = new SharedPreferencesUtil(activity);

        int calendarDay = (int) sp.getParam("day",0);
        Boolean isChoose = (Boolean) sp.getParam("isChoose",false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(isChoose){
                    for (int i = 0; i < calendarDay; i++){
                        List<HeNanPos> localHeNanPos= calendarHeNanPosDao.getAllCalendarHeNanPos(i);
                        localClusters.put(i,localHeNanPos);
                    }
                    handler.sendMessage(handler.obtainMessage(1));
                } else {
                    clusters = (MutableLiveData<HashMap<Integer, List<HeNanPos>>>) sharedCalendarViewModel.getHashMapMutableLiveData();

                }
                handler.sendMessage(handler.obtainMessage(2));
                //跳转界面后再设置choose为true
                sp.setParam("isChoose",true);
            }
        }).start();
    }
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what) {
                case 1:
                    clusters.setValue(localClusters);
                    sharedCalendarViewModel.setHashMapMutableLiveData(localClusters);
                    break;
                case 2:
                    loadCalendar();
                    break;
            }
        }
    };
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        fragmentCalendarBinding = FragmentCalendarBinding.inflate(inflater,container,false);
        fragmentCalendarBinding.pbLoading.setVisibility(View.VISIBLE);
        fragmentCalendarBinding.CalendarData.setVisibility(View.GONE);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        //fragmentCalendarBinding.CalendarData.setLayoutManager(linearLayoutManager);
        //calendarAdapter = new CalendarAdapter(activity,clusters.getValue().get(calendarPosition));
        //fragmentCalendarBinding.CalendarData.setAdapter(calendarAdapter);

        //SharedCalendarViewModel sharedCalendarViewModel = ShareViewModelProvider.get(this,SharedCalendarViewModel.class);
        //clusters = sharedCalendarViewModel.getHashMapMutableLiveData();

        /*clusters.observe(this, new Observer<HashMap<Integer, List<HeNanPos>>>() {
            @Override
            public void onChanged(HashMap<Integer, List<HeNanPos>> integerListHashMap) {

                    List<HeNanPos> heNanPosList1=integerListHashMap.get(calendarPosition);
                    for(int j=0;j< heNanPosList1.size();j++) {
                    Log.e(TAG, heNanPosList1.get(j).getName()+ "shuliang" + j);

                }
            }
        });*/
        return fragmentCalendarBinding.getRoot();

    }

    private void loadCalendar(){
        fragmentCalendarBinding.pbLoading.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        fragmentCalendarBinding.CalendarData.setLayoutManager(linearLayoutManager);
        calendarAdapter = new CalendarAdapter(activity,clusters.getValue().get(calendarPosition));
        fragmentCalendarBinding.CalendarData.setVisibility(View.VISIBLE);
        //calendarAdapter.notifyDataSetChanged();
        fragmentCalendarBinding.CalendarData.setAdapter(calendarAdapter);

        calendarAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.e(TAG, "onItemClick: "+clusters.getValue().get(calendarPosition).get(position).getName() );
                Intent intent = new Intent(activity,DayDetailActivity.class);
                intent.putExtra("url",clusters.getValue().get(calendarPosition).get(position).getPos_link());
                startActivity(intent);

               /* List<HeNanPos> heNanPosList1=clusters.getValue().get(calendarPosition);
                for(int j=0;j< heNanPosList1.size();j++) {
                    Log.e(TAG, heNanPosList1.get(j).getName() + "shuliang" + j);
                }*/
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemLongClick(View view, int position) {
                PopupMenu popupMenu = new PopupMenu(activity,view);
                popupMenu.getMenuInflater().inflate(R.menu.delete_menu,popupMenu.getMenu());
                popupMenu.setGravity(Gravity.END);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        deleteHeNanPos = clusters.getValue().get(calendarPosition).get(position);
                        deleteHeNanPosFlag = true;
                        //适配器中删除
                        calendarAdapter.removeItem(position);
                        //livedata中更新删除
                        clusters.postValue(clusters.getValue());
                        return false;
                    }
                });
            }
        });
         clusters.observe(this, new Observer<HashMap<Integer, List<HeNanPos>>>() {
            @Override
            public void onChanged(HashMap<Integer, List<HeNanPos>> integerListHashMap) {
                if(deleteHeNanPosFlag) {
                    deleteHeNanPosFlag = false;
                    Log.e(TAG, "onChanged: " + deleteHeNanPos.getName());
                    new Thread(new Runnable() {
                    @Override
                    public void run() {
                        calendarHeNanPosDao.deleteCalendarHeNanPos(deleteHeNanPos);
                    }
                    }).start();
                }
            }
        });
    }
}
