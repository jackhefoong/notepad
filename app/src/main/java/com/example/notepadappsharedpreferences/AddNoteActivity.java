package com.example.notepadappsharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashSet;

public class AddNoteActivity extends AppCompatActivity {
    int index;
    EditText et_note;
    Button buttonAddNote;
    Button buttonBack;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        index = getIntent().getIntExtra("data", -1);
        // if option == -1, Add Note; else Edit Note
        et_note = findViewById(R.id.note);

        if(index == -1) {  // Add
            MainActivity.arrayList.add(""); // add a new element to the end of the arrayList
            index = MainActivity.arrayList.size()-1;
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }
        else {  // Edit
            et_note.setText(MainActivity.arrayList.get(index)); // option = listview index
        }

        buttonAddNote = findViewById(R.id.buttonAddNote);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {

                 MainActivity.arrayList.set(index,et_note.getText().toString());
                 MainActivity.arrayAdapter.notifyDataSetChanged();

                 sharedPreferences = getApplicationContext().getSharedPreferences(
                         "com.example.notepadappsharedpreferences",
                         MODE_PRIVATE
                 );

                 HashSet<String> hashSet = new HashSet<String>(MainActivity.arrayList);
                 SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                 sharedPreferencesEditor.putStringSet("notes",hashSet);
                 sharedPreferencesEditor.apply();

                 Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
                 startActivity(intent);
                 finish();
             }
        });

        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
    }




}