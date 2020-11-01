package com.shaadi.challenge.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
 
    private Context mCtx;
    private static DatabaseClient mInstance;
    
    //our app database object
    private RoomHelper appDatabase;
 
    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        
        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = RoomHelper.getDatabase(mCtx);
    }
 
    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }
 
    public RoomHelper getAppDatabase() {
        return appDatabase;
    }
}