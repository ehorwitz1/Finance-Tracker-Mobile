package com.erik.project4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

//Helpful documentation on intents
//https://developer.android.com/reference/android/content/Intent

//Was having a lot of trouble communicating using Fragments,
//Decided to try using another activity to add and edit holders

public class AddEditHolderActivity extends AppCompatActivity {


    //All of the editTexts that the user will add values too
    private EditText editTextName;
    private EditText editTextCategory;
    private EditText editTextDate;
    private EditText editTextAmount;
    private EditText editTextNote;

    //This was for the intents, intents need keys and making them static and final help with that
    public static final String ADD_NAME = "com.erik.project4.ADD_NAME";
    public static final String ADD_CATEGORY = "com.erik.project4.ADD_CATEGORY";
    public static final String ADD_DATE = "com.erik.project4.ADD_DATE";
    public static final String ADD_AMOUNT = "com.erik.project4.ADD_AMOUNT";
    public static final String ADD_NOTE = "com.erik.project4.ADD_NOTE";
    public static final String ADD_ID = "com.erik.project4.ADD_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_holder);

        //Set all the editTexts
        editTextName = findViewById(R.id.edit_text_name);
        editTextCategory = findViewById(R.id.edit_text_category);
        editTextDate = findViewById(R.id.edit_text_date);
        editTextAmount = findViewById(R.id.edit_text_amount);
        editTextNote = findViewById(R.id.edit_text_note);

        //Set the menu
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);

        Intent intent = getIntent();

        //If the ID is set for editing
        if(intent.hasExtra(ADD_ID)){
            //Set the title of the menu to edit entry
            setTitle("Edit Entry");
            //These make it so the values to edit are already in the holder when you click on it
            editTextName.setText(intent.getStringExtra(ADD_NAME));
            editTextCategory.setText(intent.getStringExtra(ADD_CATEGORY));
            editTextDate.setText(intent.getStringExtra(ADD_DATE));

            //COULD BE CAUSING PROBLEMS
            //editTextAmount.setText(intent.getStringExtra(ADD_AMOUNT));
            editTextAmount.setText( Float.toString( intent.getFloatExtra(AddEditHolderActivity.ADD_AMOUNT, 0)));

            editTextNote.setText(intent.getStringExtra(ADD_NOTE));

        }
        else{
            setTitle("Add Entry");
        }


    }


    private void saveHolder(){
        //Get all the values from the edit text fields
        String name = editTextName.getText().toString();
        String category = editTextCategory.getText().toString();
        String date = editTextDate.getText().toString();
        String stringAmount = editTextAmount.getText().toString();
        String note = editTextNote.getText().toString();

        //Make sure that none of the values are empty
        if(name.trim().isEmpty() || category.trim().isEmpty() || date.trim().isEmpty() || note.trim().isEmpty() || stringAmount.trim().isEmpty() )
        {
            Toast.makeText(this, "Insert all information please", Toast.LENGTH_SHORT).show();
            return;
        }

        float amount =  Float.valueOf(stringAmount);

        //Create an intent that will get passed to other activity
        Intent holder_data = new Intent();
        //Put all the values in,
        holder_data.putExtra(ADD_NAME, name);
        holder_data.putExtra(ADD_CATEGORY, category);
        holder_data.putExtra(ADD_DATE, date);
        holder_data.putExtra(ADD_AMOUNT, amount);
        holder_data.putExtra(ADD_NOTE, note);


        long longID = getIntent().getLongExtra(ADD_ID, -1);

        //int id = getIntent().getIntExtra(ADD_ID, -1);
        int id = Long.valueOf(longID).intValue();

        if(id != -1)
        {
            holder_data.putExtra(ADD_ID, id);
        }
        //If everything went okay set the result
        setResult(RESULT_OK, holder_data);
        finish();


    }

    //Set the menu we want for this activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_holder_menu, menu);
        return true;
    }

    //Check to see what was clicked on the options menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //If we hit the save holder button
            case R.id.save_holder:
                //Call saveholder
                saveHolder();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}
