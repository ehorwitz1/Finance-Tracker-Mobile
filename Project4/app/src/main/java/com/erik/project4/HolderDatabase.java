package com.erik.project4;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

//Helpful info on room database
//https://developer.android.com/reference/android/arch/persistence/room/RoomDatabase

//Create a room database, tell it what class we want it to hold
@Database(entities = Holder.class, version = 1)
public abstract class HolderDatabase extends RoomDatabase {

    //Get in instance to the database, helps with creating singletons
    //need to use the same instance everywhere in the app
    private static HolderDatabase instance;

    //Get a reference to the DAO
    public abstract HolderDao holderDao();

    //Return holder database instance
    //Allows us to get reference to the single instance we want to talk too
    //Dont want multiple threads accessing multiple versions
    public static synchronized HolderDatabase getInstance(Context context){
        //Only create a new instance if we do not have one
        if(instance == null){
            //Set up our room instance
            instance = Room.databaseBuilder(context.getApplicationContext(), HolderDatabase.class, "holder_database").fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }

    //This allows us access to OnCreate
    private static HolderDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDatabaseAsyncTask(instance).execute();

        }
    };

    //Create an async task that goes in and sets initial values
    private static class PopulateDatabaseAsyncTask extends AsyncTask<Void, Void, Void>{
        private HolderDao holderDao;

        private PopulateDatabaseAsyncTask(HolderDatabase database){
            holderDao = database.holderDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Insert values into the DAO when the database is first created
            holderDao.insert(new Holder("Erik", "Food", "10/13/19", 6200, "Lots of Chinese food"));
            holderDao.insert(new Holder("Derik", "Travel", "9/12/63", 420.25f, "Traveled to Malta"));
            holderDao.insert(new Holder("Berik", "Gas", "4/2/87", 40, "Filled up on gas"));

            return null;
        }
    }


}
