package com.example.recommenddemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.recommenddemo.app.ActivityManage;
import com.example.recommenddemo.base.BaseActivity;
import com.example.recommenddemo.databinding.ActivityChooseBinding;
import com.example.recommenddemo.room.HeNanPos;
import com.example.recommenddemo.room.HeNanPosDao;
import com.example.recommenddemo.room.HeNanPosDatabase;
import com.example.recommenddemo.viewmodel.ShareViewModelProvider;
import com.example.recommenddemo.viewmodel.SharedCalendarViewModel;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ChooseActivity extends BaseActivity {
    //定义一个变量，来标识是否退出
    private int isExit=0;
    ActivityChooseBinding chooseBinding;
    int[] dynasties;
    List<String> cityList = new ArrayList<>();
    String[] daies;
    int[] numberDay ={3,4,5,6,7,8,9,10};
    int chooseDay;
    HeNanPosDao heNanPosDao;
    List<HeNanPos> heNanPosList;
    HashMap<Integer, List<HeNanPos>> clusters;
    private SharedCalendarViewModel sharedCalendarViewModel = ShareViewModelProvider.get(this,SharedCalendarViewModel.class);;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chooseBinding = ActivityChooseBinding.inflate(getLayoutInflater());
        setContentView(chooseBinding.getRoot());


        heNanPosDao = HeNanPosDatabase.getDatabase(this).getPosDao();
        daies = getResources().getStringArray(R.array.day_array);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.day_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseBinding.chooseDay.setAdapter(arrayAdapter);
        chooseBinding.chooseDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chooseDay = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chooseBinding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                determineSelected();
                Log.e(TAG, "onClick: "+numberDay[chooseDay]+ Arrays.toString(dynasties)+cityList.toString());
                new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        heNanPosList = heNanPosDao.getAllHeNanPos(dynasties[0],dynasties[1],dynasties[2],dynasties[3],dynasties[4],dynasties[5],dynasties[6],dynasties[7],
                                dynasties[8],dynasties[9],dynasties[10],dynasties[11],dynasties[12],cityList);
                        KMean kMean = new KMean(numberDay[chooseDay],heNanPosList);
                        clusters = kMean.doKMeans();
                        for(int i=0;i<clusters.size();i++)
                        {
                            List<HeNanPos> heNanPosList1=clusters.get(i);
                            for(HeNanPos heNanPos:heNanPosList1)
                            {
                                float recommend_score = (float) (heNanPos.getXiashangzhou()*0.077+heNanPos.getChunqiuzhanguo()*0.077+heNanPos.getQin()*0.077+heNanPos.getHan()*0.077+heNanPos.getSanguo()*0.077+heNanPos.getJin()*0.077
                                        + heNanPos.getNanbeichao()*0.077+heNanPos.getSui()*0.077+heNanPos.getTang()*0.077+heNanPos.getSong()*0.077+heNanPos.getYuan()*0.077+heNanPos.getMing()*0.077+heNanPos.getQing()*0.077);
                                heNanPos.setRecommend_score(recommend_score);
                            }
                            Collections.sort(heNanPosList1);
                            Collections.reverse(heNanPosList1);
                            clusters.replace(i,heNanPosList1);
                        }
                        handler.sendMessage(handler.obtainMessage(1));
                        Intent intent = new Intent(ChooseActivity.this,CalendarActivity.class);
                        intent.putExtra("day",numberDay[chooseDay]);
                        startActivity(intent);
                    }
                }).start();
            }
        });
    }

    private void determineSelected() {
        //初始化朝代选择
        dynasties = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        if(chooseBinding.chooseXiashangzhou.isChecked())
            dynasties[0] = 1;
        if(chooseBinding.chooseChunqiuzhanguo.isChecked())
            dynasties[1] = 1;
        if(chooseBinding.chooseQin.isChecked())
            dynasties[2] = 1;
        if(chooseBinding.chooseHan.isChecked())
            dynasties[3] = 1;
        if(chooseBinding.chooseSanguo.isChecked())
            dynasties[4] = 1;
        if(chooseBinding.chooseJin.isChecked())
            dynasties[5] = 1;
        if(chooseBinding.chooseNanbeichao.isChecked())
            dynasties[6] = 1;
        if(chooseBinding.chooseSui.isChecked())
            dynasties[7] = 1;
        if(chooseBinding.chooseTang.isChecked())
            dynasties[8] = 1;
        if(chooseBinding.chooseSong.isChecked())
            dynasties[9] = 1;
        if(chooseBinding.chooseYuan.isChecked())
            dynasties[10] = 1;
        if(chooseBinding.chooseMing.isChecked())
            dynasties[11] = 1;
        if(chooseBinding.chooseQing.isChecked())
            dynasties[12] = 1;
        //初始化城市选择
        cityList.clear();
        if(chooseBinding.chooseZhengzhou.isChecked())
            cityList.add("郑州市");
        if(chooseBinding.chooseLuoyang.isChecked())
            cityList.add("洛阳市");
        if(chooseBinding.chooseKaifeng.isChecked())
            cityList.add("开封市");
        if(chooseBinding.chooseXuchang.isChecked())
            cityList.add("许昌市");
        if(chooseBinding.chooseAnyang.isChecked())
            cityList.add("安阳市");
        if(chooseBinding.chooseXinyang.isChecked())
            cityList.add("信阳市");
        if(chooseBinding.chooseNanyang.isChecked())
            cityList.add("南阳市");
        if(chooseBinding.chooseShangqiu.isChecked())
            cityList.add("商丘市");
        if(chooseBinding.chooseZhouko.isChecked())
            cityList.add("周口市");
        if(chooseBinding.chooseJiaozuo.isChecked())
            cityList.add("焦作市");
        if(chooseBinding.chooseXinxiang.isChecked())
            cityList.add("新乡市");
        if(chooseBinding.chooseLuohe.isChecked())
            cityList.add("漯河市");
        if(chooseBinding.chooseJiyuan.isChecked())
            cityList.add("济源市");
        if(chooseBinding.chooseHebi.isChecked())
            cityList.add("鹤壁市");
        if(chooseBinding.choosePuyang.isChecked())
            cityList.add("濮阳市");
        if(chooseBinding.chooseSanmenxia.isChecked())
            cityList.add("三门峡市");
        if(chooseBinding.chooseZhumadian.isChecked())
            cityList.add("驻马店市");
        if(chooseBinding.choosePindingshan.isChecked())
            cityList.add("平顶山市");
    }

    //实现按两次后退才退出
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what) {
                case 1:
                    sharedCalendarViewModel.setHashMapMutableLiveData(clusters);
                    break;
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