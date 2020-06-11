package com.erik.project4;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Helpful documentation
//https://developer.android.com/training/data-storage/room/defining-data

//Make a room annotation
@Entity(tableName = "holder_table")
public class Holder {
    //Everything that would be stored into a holder
    private String Name;
    private String Category;
    private String Date;
    private float Amount;
    private String Note;
    //Create a private key for everything
    @PrimaryKey(autoGenerate = true)
    private long _id;

    //Constructor for a holder object
    public Holder(String Name, String Category, String Date, float Amount, String Note) {
        this.Name = Name;
        this.Category = Category;
        this.Date = Date;
        this.Amount = Amount;
        this.Note = Note;
    }

    //Can set specific id
    public void set_id(long _id) {
        this._id = _id;
    }
    //General getters
    public String getName() {
        return Name;
    }

    public String getCategory() {
        return Category;
    }

    public String getDate() {
        return Date;
    }

    public float getAmount() {
        return Amount;
    }

    public String getNote() {
        return Note;
    }

    public long get_id() {
        return _id;
    }
}



