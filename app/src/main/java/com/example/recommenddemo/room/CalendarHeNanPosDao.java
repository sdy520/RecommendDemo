package com.example.recommenddemo.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CalendarHeNanPosDao {
    @Insert
    void insertCalendarHeNanPos(List<HeNanPos> heNanPos);
    @Delete
    void deleteCalendarHeNanPos(HeNanPos... heNanPos);
    @Query("DELETE FROM HeNanPos")
    void deleteAllCalendarHeNanPos();
    @Query("SELECT * FROM HeNanPos WHERE day = :day ORDER BY recommend_score DESC")
    List<HeNanPos> getAllCalendarHeNanPos(int day);
}
