package com.example.recommenddemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.lifecycle.LiveData;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.model.NaviPoi;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.example.recommenddemo.app.ActivityManage;
import com.example.recommenddemo.base.BaseActivity;
import com.example.recommenddemo.databinding.ActivityNavifationBinding;
import com.example.recommenddemo.room.HeNanPos;
import com.example.recommenddemo.util.CalPosDistance;
import com.example.recommenddemo.viewmodel.ShareViewModelProvider;
import com.example.recommenddemo.viewmodel.SharedCalendarViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NavigationActivity extends BaseActivity implements INaviInfoCallback {
    ActivityNavifationBinding navifationBinding;
    LiveData<HashMap<Integer, List<HeNanPos>>> clusters;
    double[] longitudePos;
    double[] latitudePos;
    private SharedCalendarViewModel sharedCalendarViewModel = ShareViewModelProvider.get(this,SharedCalendarViewModel.class);
    DistanceSearch distanceSearch;
    DistanceSearch.DistanceQuery distanceQuery;
    int controlDistanceSearch = 0;
    int[] citySort;
    double timeRemain = 9.0;
    List<HeNanPos> heNanPosList1;
    AMapNavi aMapNavi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navifationBinding = ActivityNavifationBinding.inflate(getLayoutInflater());
        setContentView(navifationBinding.getRoot());
        // ????????????AMapNavi???????????????
        aMapNavi = AMapNavi.getInstance(getApplicationContext());
        //??????????????????8???00-18???00 ??????????????????

        //????????????????????????????????????????????????
        
        //113.625357,34.746152 ???????????????
        HeNanPos localPos = new HeNanPos(113.625357,34.746152);
        clusters=sharedCalendarViewModel.getHashMapMutableLiveData();
        Intent intent = getIntent();
        int sendToNavigationPosition = intent.getIntExtra("sendToNavigationPosition",0);
        Log.e(TAG, "onCreate: "+sendToNavigationPosition);
        heNanPosList1=clusters.getValue().get(sendToNavigationPosition);
        //????????????????????????
        longitudePos = new double[5];
        latitudePos = new double[5];
        longitudePos[0] = 113.625357;
        latitudePos[0] = 34.746152;
        //TSP??????
        CalPosDistance calPosDistance = new CalPosDistance(5);
        for(int j=1;j< 5;j++) {
            longitudePos[j] = heNanPosList1.get(j-1).getLongitude();
            latitudePos[j] = heNanPosList1.get(j-1).getLatitude();
        }
        //????????????
        /*longitudePos = new double[heNanPosList1.size()+1];
        latitudePos = new double[heNanPosList1.size()+1];
        longitudePos[0] = 113.625357;
        latitudePos[0] = 34.746152;
        //TSP??????
        CalPosDistance calPosDistance = new CalPosDistance(heNanPosList1.size()+1);
        for(int j=1;j< heNanPosList1.size()+1;j++) {
            longitudePos[j] = heNanPosList1.get(j-1).getLongitude();
            latitudePos[j] = heNanPosList1.get(j-1).getLatitude();
        }*/

        calPosDistance.init(longitudePos,latitudePos);
        citySort=calPosDistance.solve();

        Log.e(TAG, Arrays.toString(citySort));


        distanceQuery = new DistanceSearch.DistanceQuery();
        distanceSearch = new DistanceSearch(this);
/*
        for (int i = 0; i < citySort.length-1; i++) {
            LatLonPoint start = new LatLonPoint(latitudePos[citySort[i]], longitudePos[citySort[i]]);
            LatLonPoint dest = new LatLonPoint(latitudePos[citySort[i+1]], longitudePos[citySort[i+1]]);
            calTime(start,dest);
        }*/
        LatLonPoint start = new LatLonPoint(latitudePos[citySort[controlDistanceSearch]], longitudePos[citySort[controlDistanceSearch]]);
        LatLonPoint dest = new LatLonPoint(latitudePos[citySort[controlDistanceSearch+1]], longitudePos[citySort[controlDistanceSearch+1]]);
        calTime(start,dest);

       /* for(int j=0;j< heNanPosList1.size();j++) {
            Log.e(TAG, heNanPosList1.get(j).getName() + "shuliang" + j);
        }*/

    }

    private void calTime(LatLonPoint start, LatLonPoint dest) {

        List<LatLonPoint> latLonPoints = new ArrayList<LatLonPoint>();
        latLonPoints.add(start);
        distanceQuery.setOrigins(latLonPoints);
        distanceQuery.setDestination(dest);
//        ??????????????????????????????????????????
        distanceQuery.setType(DistanceSearch.TYPE_DRIVING_DISTANCE);
        distanceSearch.calculateRouteDistanceAsyn(distanceQuery);
        distanceSearch.setDistanceSearchListener(new DistanceSearch.OnDistanceSearchListener() {
            @Override
            public void onDistanceSearched(DistanceResult distanceResult, int i) {
                // Log.d("??????", "onDistanceSearched: " + i);
                double time = 0;
                if (i == 1000) {
                    //String time_string;

                    //?????????
                    String distance = Integer.valueOf((int) distanceResult.getDistanceResults().get(0).getDistance()) / 1000 + "";
                    //????????? ?????????
                    long second = (long) distanceResult.getDistanceResults().get(0).getDuration();

                       /* long days = second / 86400;            //????????????
                        second = second % 86400;            //????????????
                        long hours = second / 3600;            //????????????
                        second = second % 3600;                //????????????
                        long minutes = second / 60;            //????????????
                        second = second % 60;*/
                    time = second / 3600.0;
                        /*if (days > 0) {
                            time_string = days + "???" + hours + "??????" + minutes + "??????";
                        } else if (hours > 0) {
                            time_string = hours + "??????" + minutes + "??????";
                        } else {
                            time_string = minutes + "??????";
                        }*/
                    Log.e(TAG, "onDistanceSearched: " + "?????????" + distance + "??????????????????" + time + "??????");
                    controlDistanceSearch++;
                    timeRemain = timeRemain - heNanPosList1.get(citySort[controlDistanceSearch]-1).getPlay_time()-time;
                    Log.e(TAG, controlDistanceSearch+"---"+citySort.length+"---"+timeRemain);

                    if (controlDistanceSearch >= citySort.length-1 || timeRemain <= 0) {
                        controlDistanceSearch =controlDistanceSearch;

                        Log.e(TAG, "onDistanceSearched: over"+controlDistanceSearch);
                        startNavi();
                    } else {
                        LatLonPoint start = new LatLonPoint(latitudePos[citySort[controlDistanceSearch]], longitudePos[citySort[controlDistanceSearch]]);
                        LatLonPoint dest = new LatLonPoint(latitudePos[citySort[controlDistanceSearch + 1]], longitudePos[citySort[controlDistanceSearch + 1]]);
                        calTime(start, dest);
                        Log.e(TAG, "onDistanceSearched: continue");
                    }
                } else {
                    Log.e(TAG, "onDistanceSearched: ??????????????????");
                }

            }
        });
    }

    private void startNavi(){
        Poi start = new Poi("???????????????", new LatLng(latitudePos[0],longitudePos[0]),null);
        //????????????103.166964,36.890526
        Poi end = new Poi(heNanPosList1.get(citySort[controlDistanceSearch]-1).getName(), new LatLng(latitudePos[citySort[controlDistanceSearch]], longitudePos[citySort[controlDistanceSearch]]),null);
        // ???????????????
        List<Poi> waysPoiIds = new ArrayList<Poi>();
        Log.e(TAG, "startNavi: "+controlDistanceSearch);
        if(controlDistanceSearch>1)
        {
            for (int i = 1; i < controlDistanceSearch; i++) {
                waysPoiIds.add(new Poi(heNanPosList1.get(citySort[i]-1).getName(),new LatLng(latitudePos[citySort[i]], longitudePos[citySort[i]]),null));
            }
        }else {
            waysPoiIds = null;
        }
        // ??????????????????
        AmapNaviParams params = new AmapNaviParams(start, waysPoiIds, end, AmapNaviType.DRIVER, AmapPageType.ROUTE);

        params.setDayAndNightMode(this,1);
        // ????????????
        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, NavigationActivity.this);


        //?????????????????????NavigationActivity
        NavigationActivity.this.finish();


        /*// ????????????Manager
        AMapNavi mAMapNavi = AMapNavi.getInstance(this);
        // ????????????
        List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
        startList.add(new NaviLatLng(latitudePos[0], longitudePos[0]));
        // ????????????
        List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
        endList.add(new NaviLatLng(latitudePos[citySort[controlDistanceSearch]], longitudePos[citySort[controlDistanceSearch]]));

        // ???????????????
        List<NaviLatLng> waysPoiIds = new ArrayList<NaviLatLng>();
        if(controlDistanceSearch>1)
        {
            for (int i = 1; i < controlDistanceSearch; i++) {
                waysPoiIds.add(new NaviLatLng(latitudePos[citySort[i]], longitudePos[citySort[i]]));
            }
        }else {
            waysPoiIds = null;
        }

        // ???????????????
        mAMapNavi.calculateDriveRoute(startList, endList, waysPoiIds, PathPlanningStrategy.DRIVING_MULTIPLE_ROUTES_DEFAULT);*/

       /* // ????????????
        LatLonPoint startList = new LatLonPoint(latitudePos[0], longitudePos[0]);
        // ????????????
        LatLonPoint endList = new LatLonPoint(latitudePos[0], longitudePos[0]);
        List<LatLonPoint> waysPoiIds = new ArrayList<LatLonPoint>();
        waysPoiIds.add(new LatLonPoint(latitudePos[9], longitudePos[9]));
        waysPoiIds.add(new LatLonPoint(latitudePos[1], longitudePos[1]));
        waysPoiIds.add(new LatLonPoint(latitudePos[2], longitudePos[2]));
        waysPoiIds.add(new LatLonPoint(latitudePos[4], longitudePos[4]));*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Log.e(TAG, "onKeyDown: ");
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }


    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    @Override
    public void onStrategyChanged(int i) {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviDirectionChanged(int i) {

    }

    @Override
    public void onDayAndNightModeChanged(int i) {

    }

    @Override
    public void onBroadcastModeChanged(int i) {

    }

    @Override
    public void onScaleAutoChanged(boolean b) {

    }

    @Override
    public View getCustomMiddleView() {
        return null;
    }

    @Override
    public View getCustomNaviView() {
        return null;
    }

    @Override
    public View getCustomNaviBottomView() {
        return null;
    }
}
