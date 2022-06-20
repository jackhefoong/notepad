package com.example.notepadappsharedpreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Toolbar toolbar;
    static ArrayList<String> arrayList = new ArrayList<String>();
    static ArrayAdapter arrayAdapter;
    Button addNote;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addNote = findViewById(R.id.addNote);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                // no PutExtra
                startActivity(intent);
            }
        });

        // Retrieve data from Shared Preferences and populate the listview
        sharedPreferences = getApplicationContext().getSharedPreferences(
                "com.example.notepadappsharedpreferences",
                MODE_PRIVATE
        );

        HashSet<String> hashSet = (HashSet<String>) sharedPreferences.getStringSet("notes", null);
        if(hashSet != null) {
            arrayList = new ArrayList<>(hashSet);
        }

        listView = findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        // tap short to edit existing note
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String s = (String) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);
                intent.putExtra("data",position);
                startActivity(intent);
            }
        });

        // tap long to delete
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure?")
                        .setPositiveButton(
                                "Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        arrayList.remove(position);
                                        arrayAdapter.notifyDataSetChanged(); // Update Listview by removing the selected note

                                        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notepadappsharedpreferences", Context.MODE_PRIVATE);
                                        HashSet<String> hashSet = new HashSet<String>(MainActivity.arrayList);
                                        // https://developer.android.com/reference/android/content/SharedPreferences.Editor#putStringSet(java.lang.String,%20java.util.Set%3Cjava.lang.String%3E)
                                        // Set a set of String values in the preferences editor, to be written back once commit() or apply() is called.
                                        SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();
                                        sharedPreferenceEditor.putStringSet("notes",hashSet);
                                        sharedPreferenceEditor.apply();
                                        Toast.makeText(MainActivity.this, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        ).setNegativeButton("No",null).show();

                return true;
            }
        });

    }
}