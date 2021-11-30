package com.example.recommenddemo.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {HeNanPos.class},version = 1,exportSchema = false)
public abstract class HeNanPosDatabase extends RoomDatabase {
    private static HeNanPosDatabase INSTANCE;
    public static synchronized HeNanPosDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),HeNanPosDatabase.class,"HeNanPosDatabase")
                    .createFromAsset("database/HeNanPosDatabase")
                    //.fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
    public abstract HeNanPosDao getPosDao();
}
