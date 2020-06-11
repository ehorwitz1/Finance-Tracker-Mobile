package com.erik.project4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

//Erik Horwitz
//Program 4
//Ward
//COSC 4730

public class MainActivity extends AppCompatActivity {
    //This is for the intents, it helps determine if we are adding or editings
    public static final int ADD_HOLDER_REQ = 1;
    public static final int EDIT_HOLDER_REQ = 2;

    //Create reference for view model
    private HolderViewModel holderViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This is the floating button that handles adding entries
        FloatingActionButton buttonAddHolder = findViewById(R.id.button_add);
        buttonAddHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create in intent that allows us to get info from the addHolderActivity
                Intent intent = new Intent(MainActivity.this, AddEditHolderActivity.class);
                startActivityForResult(intent, ADD_HOLDER_REQ);
            }
        });

        //Set up the recycler view
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        //Give it a linear layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //need reference to adapter
        final HolderAdapter adapter = new HolderAdapter();
        //Set the recycler view's adapter
        recyclerView.setAdapter(adapter);

        //Get reference for the holderViewModel
        holderViewModel = ViewModelProviders.of(this).get(HolderViewModel.class);

        //Everytime data is changed in the table notify the adapter
        holderViewModel.getAllHolders().observe(this, new Observer<List<Holder>>() {
            @Override
            public void onChanged(List<Holder> holders) {
                adapter.setHolders(holders);
            }
        });

        //This allows for swipes
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                //These set up the left and right swipes
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            //This is not needed
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //If we do swipe, get the position in the viewholder that we touched and delete that specific element
                //since we are using live data the observer should notice this
                holderViewModel.delete(adapter.getHolderIndex(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Holder deleted",Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        //This handles the clicking for the adapter
        //This is when we are editing the entries
        adapter.setOnItemClickListener(new HolderAdapter.OnItemClickListener() {
            @Override
            //This references the holder that was clicked in the recycler view
            public void onItemClick(Holder holder) {
                //Create an intent with the values that were already in the item
                Intent intent = new Intent(MainActivity.this, AddEditHolderActivity.class);
                //Pass in all the values
                intent.putExtra(AddEditHolderActivity.ADD_ID, holder.get_id());
                intent.putExtra(AddEditHolderActivity.ADD_NAME, holder.getName());
                intent.putExtra(AddEditHolderActivity.ADD_CATEGORY, holder.getCategory());
                intent.putExtra(AddEditHolderActivity.ADD_DATE, holder.getDate());
                intent.putExtra(AddEditHolderActivity.ADD_AMOUNT, holder.getAmount());
                intent.putExtra(AddEditHolderActivity.ADD_NOTE, holder.getNote());
                startActivityForResult(intent, EDIT_HOLDER_REQ);
            }
        });
    }

    //This gets called when we get the result back with the intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //If we are adding a holder
        if(requestCode == ADD_HOLDER_REQ && resultCode == RESULT_OK){
            //Create local variables to store all the data from the intent
            String name = data.getStringExtra(AddEditHolderActivity.ADD_NAME);
            String category = data.getStringExtra(AddEditHolderActivity.ADD_CATEGORY);
            String date = data.getStringExtra(AddEditHolderActivity.ADD_DATE);
            float amount = data.getFloatExtra(AddEditHolderActivity.ADD_AMOUNT, 0);
            String note = data.getStringExtra(AddEditHolderActivity.ADD_NOTE);
            //Create the holder that actually holds these values
            Holder holder = new Holder(name,category,date,amount,note);
            //Insert the holder into the view model
            //With the observers this should update the view
            holderViewModel.insert(holder);

            Toast.makeText(this,"Entry Saved", Toast.LENGTH_SHORT).show();
        }
        //If we are editing a holder
        else if(requestCode == EDIT_HOLDER_REQ && resultCode == RESULT_OK){

            //long longID = data.getLongExtra(AddEditHolderActivity.ADD_ID, -1);
            //int id = Long.valueOf(longID).intValue();

            int id = data.getIntExtra(AddEditHolderActivity.ADD_ID, -1);
            //Check to see if there was a problem with editing the holder
            if(id == -1){
                Toast.makeText(this, "Entry cannot be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            //If there was no problem then create a bunch of local variables to hold
            //data we got from the intent
            String name = data.getStringExtra(AddEditHolderActivity.ADD_NAME);
            String category = data.getStringExtra(AddEditHolderActivity.ADD_CATEGORY);
            String date = data.getStringExtra(AddEditHolderActivity.ADD_DATE);
            float amount = data.getFloatExtra(AddEditHolderActivity.ADD_AMOUNT, 0);
            String note = data.getStringExtra(AddEditHolderActivity.ADD_NOTE);
            //Create a new holder
            Holder holder = new Holder(name, category, date, amount, note);
            holder.set_id(id);
            //Update the view model with the holder we just used
            holderViewModel.update(holder);
            Toast.makeText(this, "Entry Updated", Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(this,"Entry not saved", Toast.LENGTH_SHORT).show();
        }
    }

    //Set up the menu for the main activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //Check to see what button on the menu was selected
    //If it was the delete all button then call deleteAll in the view model
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete_all_entries:
                holderViewModel.deleteAll();
                Toast.makeText(this,"All entries deleted", Toast.LENGTH_SHORT).show();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }


    }
}
