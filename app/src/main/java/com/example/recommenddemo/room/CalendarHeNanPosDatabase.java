package com.example.recommenddemo.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {HeNanPos.class},version = 1,exportSchema = false)
public abstract class CalendarHeNanPosDatabase extends RoomDatabase {
    private static CalendarHeNanPosDatabase INSTANCE;
    public static synchronized CalendarHeNanPosDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),CalendarHeNanPosDatabase.class,"CalendarHeNanPosDatabase")
                    //.createFromAsset("database/HeNanPosDatabase")
                    //.fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
    public abstract CalendarHeNanPosDao getCalendarPosDao();
}
