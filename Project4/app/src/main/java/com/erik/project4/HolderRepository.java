package com.erik.project4;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

//Useful documentation for repository
//https://developer.android.com/jetpack/docs/guide

//Middleman between room and view model
public class HolderRepository {
    //Get access to DAO
    private HolderDao holderDao;
    private LiveData<List<Holder>> allHolders;

    public HolderRepository(Application application){
        //Get our instance of the database
        HolderDatabase database = HolderDatabase.getInstance(application);
        //Set the DAO
        holderDao = database.holderDao();
        //Get the holders from the DAO
        allHolders = holderDao.getAllHolders();
    }

    //Insert method
    public void insert(Holder holder){
        //Create the async task, pass it the DAO we are working with, and pass the holder we will add
        new InsertHolderAsyncTask(holderDao).execute(holder);
    }
    //Update method
    public void update(Holder holder){
        //Update a holder requires a reference to a holder, the dao, and the async task
        new UpdateHolderAsyncTask(holderDao).execute(holder);
    }
    //Delete method
    public void delete(Holder holder){
        //Delete specific holder in the dao
        new DeleteHolderAsyncTask(holderDao).execute(holder);
    }
    //Delete all method
    public void deleteAllHolders(){
        //Dont need to pass anything since we are deleting all
        new DeleteAllHolderAsyncTask(holderDao).execute();
    }
    //Get all of the holders
    public LiveData<List<Holder>> getAllHolders(){
        return allHolders;
    }


    //Create a bunch of Async tasks in order to handle the database methods
    private static class InsertHolderAsyncTask extends AsyncTask<Holder, Void, Void>{
        //Need reference to this in order to make database operations
        private HolderDao holderDao;
        //Constructor
        private InsertHolderAsyncTask(HolderDao holderDao){
            this.holderDao = holderDao;
        }
        //Actually call the method we want to do
        @Override
        protected Void doInBackground(Holder... holders){

            holderDao.insert(holders[0]);
            return null;
        }
    }

    //These were just copied and pasted from the InsertAsyncTask, this time just changing what operations
    //will be called
    private static class UpdateHolderAsyncTask extends AsyncTask<Holder, Void, Void>{
        private HolderDao holderDao;

        private UpdateHolderAsyncTask(HolderDao holderDao){
            this.holderDao = holderDao;
        }

        @Override
        protected Void doInBackground(Holder... holders){

            holderDao.update(holders[0]);
            return null;
        }
    }

    private static class DeleteHolderAsyncTask extends AsyncTask<Holder, Void, Void>{
        private HolderDao holderDao;

        private DeleteHolderAsyncTask(HolderDao holderDao){
            this.holderDao = holderDao;
        }

        @Override
        protected Void doInBackground(Holder... holders){

            holderDao.delete(holders[0]);
            return null;
        }
    }

    private static class DeleteAllHolderAsyncTask extends AsyncTask<Void, Void, Void>{
        private HolderDao holderDao;

        private DeleteAllHolderAsyncTask(HolderDao holderDao){
            this.holderDao = holderDao;
        }

        @Override
        protected Void doInBackground(Void... voids){

            holderDao.deleteAllHolders();
            return null;
        }
    }


}
