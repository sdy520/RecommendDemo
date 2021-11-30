package com.example.recommenddemo;

import android.util.Log;

import com.example.recommenddemo.room.HeNanPos;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * KMeans算法的实现
 * 包装成工具类，直接传参数调用即可使用
 */
public class KMean {
    //聚类的个数
    int cluterNum;
    //数据集中的点
    List<HeNanPos> points = new ArrayList<>();
    //簇的中心点
    List<HeNanPos> centerPoints = new ArrayList<>();
    //聚类结果的集合簇，key为聚类中心点在centerPoints中的下标，value为该类簇下的数据点
    HashMap<Integer, List<HeNanPos>> clusters = new HashMap<>();
    public KMean(int cluterNum, List<HeNanPos> points){
        this.cluterNum = cluterNum;
        this.points = points;
    }

    //KMeans聚类
    public HashMap<Integer, List<HeNanPos>> doKMeans(){

        //初始化KMeans模型，这里选数据集前classNum个点作为初始中心点
        for (int i = 0; i < cluterNum; i++) {
            centerPoints.add(points.get(i));
            clusters.put(i, new ArrayList<>());
        }
        double err = Integer.MAX_VALUE;
        while (err > 0.01 * cluterNum){
            //每次聚类前清空原聚类结果的list
            for (int key : clusters.keySet()){
                List<HeNanPos> list = clusters.get(key);
                list.clear();
                clusters.put(key, list);
            }
            //计算每个点所属类簇
            for (int i=0; i<points.size(); i++){
                dispatchPointToCluster(points.get(i), centerPoints);
            }
            //计算每个簇的中心点，并得到中心点偏移误差
            err = getClusterCenterPoint(centerPoints, clusters);

            //System.out.println("*************************");
            Log.d("TAG", "*************************");
        }
        //show(centerPoints, clusters);
        return clusters;
    }

    //计算点对应的中心点，并将该点划分到距离最近的中心点的簇中
    public void dispatchPointToCluster(HeNanPos point, List<HeNanPos> centerPoints){
        int index = 0;
        double tmpMinDistance = Double.MAX_VALUE;
        for (int i=0; i<centerPoints.size(); i++){
            double distance = calDistance(point, centerPoints.get(i));
            if (distance < tmpMinDistance){
                tmpMinDistance = distance;
                index = i;
            }
        }
        List<HeNanPos> list = clusters.get(index);
        list.add(point);
        clusters.put(index, list);
    }

    //计算每个类簇的中心点，并返回中心点偏移误差
    public double getClusterCenterPoint(List<HeNanPos> centerPoints, HashMap<Integer, List<HeNanPos>> clusters){
        double error = 0;
        for (int i=0; i<centerPoints.size(); i++){
            HeNanPos tmpCenterPoint = centerPoints.get(i);
            double centerX = 0, centerY = 0;
            List<HeNanPos> lists = clusters.get(i);
            for (int j=0; j<lists.size(); j++){
                centerX += lists.get(j).getLatitude();
                centerY += lists.get(j).getLongitude();
            }
            centerX /= lists.size();
            centerY /= lists.size();
            error += Math.abs(centerX - tmpCenterPoint.getLatitude());
            error += Math.abs(centerY - tmpCenterPoint.getLongitude());
            centerPoints.set(i, new HeNanPos(centerY,centerX));
        }
        return error;
    }

    //计算点之间的距离，这里计算欧氏距离（不开方）
    public double calDistance(HeNanPos point1, HeNanPos point2){
        return Math.pow((point1.getLatitude() - point2.getLatitude()), 2) + Math.pow((point1.getLongitude() - point2.getLongitude()), 2);
    }

    //打印簇中心点坐标，及簇中其他点坐标
    public void show(List<HeNanPos> centerPoints, HashMap<Integer, List<HeNanPos>> clusters){
        for (int i=0; i<centerPoints.size(); i++){
            Log.e("TAG", MessageFormat.format("类{0}的中心点: <{1}, {2}>",(i+1), centerPoints.get(i).getLongitude(), centerPoints.get(i).getLatitude()) );
            List<HeNanPos> lists = clusters.get(i);
            Log.e("TAG", "类中成员点有：");
            for (int j=0; j<lists.size(); j++){
                Log.e("TAG", "<"+lists.get(j).getLongitude()+ ","+ lists.get(j).getLatitude()+">\t");
            }
        }
    }
}
