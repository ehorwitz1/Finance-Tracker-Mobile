package com.erik.project4;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//Helpful DAO documentation
//https://developer.android.com/training/data-storage/room/accessing-data

//Make sure interface is set as DAO
@Dao
public interface HolderDao {

    //Set up all the operations we would need
    @Insert
    void insert(Holder hold);
    //Update specific holder
    @Update
    void update(Holder hold);
    //Delete specific holder
    @Delete
    void delete(Holder hold);
    //If we want to delete everything in the table
    @Query("DELETE FROM holder_table")
    void deleteAllHolders();

    //Not sure if we need to sort by anything
    //Notice we return live data in order to have observers
    //@Query("SELECT * FROM holder_table ORDER BY Amount DESC")
    @Query("SELECT * FROM holder_table")
    LiveData<List<Holder>> getAllHolders();

}
