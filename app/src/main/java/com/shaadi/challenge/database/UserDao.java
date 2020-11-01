package com.shaadi.challenge.database;

import com.shaadi.challenge.model.UserDataModel;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDao {

   // conflict resolution strategy
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   void insert(List<UserDataModel> word);

   @Query("DELETE FROM user_table")
   void deleteAll();

   @Query("SELECT * FROM user_table ORDER BY userId desc")
   List<UserDataModel> getAllUserData();

   @Query("UPDATE user_table SET approveStatus=:statusVal WHERE userId = :id")
   void update(int statusVal, int id);

   @Query("SELECT COUNT(*) FROM user_table")
   int getUserCount();
}