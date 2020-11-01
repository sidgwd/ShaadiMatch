package com.shaadi.challenge.database;

import android.content.Context;

import com.shaadi.challenge.model.Agedetails;
import com.shaadi.challenge.model.LocationDetails;
import com.shaadi.challenge.model.NameDetails;
import com.shaadi.challenge.model.Picturedetails;
import com.shaadi.challenge.model.UserDataModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserDataModel.class,
        Picturedetails.class,
        NameDetails.class,
        LocationDetails.class,
        Agedetails.class}, version = 1, exportSchema = false)
public abstract class RoomHelper extends RoomDatabase {

   public abstract UserDao UserDao();

   private static volatile RoomHelper INSTANCE;
   private static final int NUMBER_OF_THREADS = 4;
   public static final ExecutorService databaseWriteExecutor =
        Executors.newFixedThreadPool(NUMBER_OF_THREADS);

   static RoomHelper getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            RoomHelper.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}