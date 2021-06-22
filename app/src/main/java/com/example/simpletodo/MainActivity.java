package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Sets up a list to store tasks
    List<String> items;

    //Add button
    Button btnAdd;

    //Space to add new items
    EditText etItem;

    //List of items
    RecyclerView rvItems;

    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        //Initializes list from data file
        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //Delete the item from the model
                items.remove(position);
                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                // Notify the user
                Toast.makeText(getApplicationContext(), "Item has been removed", Toast.LENGTH_SHORT);
                saveItems();
            }
        };

        //Create an item adapter
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        //Set the adapter to the Recycler View
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get text from text spot to add items
                String todoItem = etItem.getText().toString();
                // Add item to the model
                items.add(todoItem);
                // Notify the adaptper that an item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                // Clear the edit text spot
                etItem.setText("");
                // Notify user that an item was added through a Toast
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT)
                        .show();
                saveItems();
            }
        });
    }

    // Returns the stored data file with list items
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // Loads items by reading every line of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    // Saves items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }

    }
}