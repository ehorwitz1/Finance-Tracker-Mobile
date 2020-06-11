package com.erik.project4;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

//Helpful view model documentation
//https://developer.android.com/topic/libraries/architecture/viewmodel

public class HolderViewModel extends AndroidViewModel {
    //Get reference to repository
    private HolderRepository repository;
    //Get liveData for all the holders
    private LiveData<List<Holder>> allHolders;

    //Instantiate variables
    public HolderViewModel(@NonNull Application application) {
        super(application);
        //Get the repository attached to this application
        repository = new HolderRepository(application);
        //Get the live data from the repository
        allHolders = repository.getAllHolders();
    }
    //Create wrapper methods for database operations from the repository
    //Just call all the same methods in the repository, making sure to pass in the holder we want to change
    public void insert(Holder holder){
        repository.insert(holder);
    }

    public void update(Holder holder){
        repository.update(holder);
    }

    public void delete(Holder holder){
        repository.delete(holder);
    }

    public void deleteAll(){
        repository.deleteAllHolders();
    }

    public LiveData<List<Holder>> getAllHolders() {
        return allHolders;
    }
}
