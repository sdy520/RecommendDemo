package com.example.recommenddemo.room;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HeNanPosDao {
    @Query("SELECT * FROM HeNanPos WHERE ((xiashangzhou = :xiashangzhou AND xiashangzhou =1) OR (chunqiuzhanguo = :chunqiuzhanguo AND chunqiuzhanguo =1) OR (qin = :qin AND qin =1) " +
            "OR (han = :han AND han =1) OR (sanguo = :sanguo AND sanguo =1) OR (jin = :jin AND jin =1) OR (nanbeichao = :nanbeichao AND jin =1) OR (sui = :sui AND sui =1)OR (tang = :tang AND tang =1)" +
            "OR (song = :song AND song =1) OR (yuan = :yuan AND yuan =1) OR (ming = :ming AND ming =1) OR (qing = :qing AND qing =1)) AND (city_name IN (:city_name))")
    List<HeNanPos> getAllHeNanPos(int xiashangzhou,int chunqiuzhanguo,int qin,int han,int sanguo,int jin,int nanbeichao,int sui,int tang,int song,int yuan,int ming,int qing,List<String> city_name);
    /*@Query("SELECT longitude,latitude FROM HeNanPos")
    List<HeNanPosLocation> getAllHeNanPosLoaction();*/

}
