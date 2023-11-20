package com.electrobit.trainingsample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.electrobit.trainingsample.storage.AppDatabase;
import com.electrobit.trainingsample.storage.Note;
import com.electrobit.trainingsample.storage.NotesDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class NotesTakingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taking);

        EditText title = (EditText) findViewById(R.id.notesTitle);
        EditText description = (EditText) findViewById(R.id.notesDescription);
        Button saveNoteButton = (Button) findViewById(R.id.saveNoteButton);
        Button showSavedNotes = (Button) findViewById(R.id.getAllNoteButton);

        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNote(title.getText().toString(), description.getText().toString());
            }
        });

        showSavedNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSavedNotes();
            }
        });
    }


    public void showSavedNotes(){
        NotesDao dao = AppDatabase.getDatabase(this).notesDao();
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<Note> notes = dao.getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder out = new StringBuilder();
                        if(notes.isEmpty()){
                            out.append("No saved notes found");
                        }else{
                            for(Note note : notes){
                                out.append("Title : " + note.title).append("\n");
                                out.append("Body : " + note.text).append("\n");
                                out.append("\n");
                            }
                        }
                        TextView showSavedNotes = (TextView) findViewById(R.id.saveNotesTextView);
                        showSavedNotes.setText(out.toString());
                    }
                });
            }
        });
    }

    private void insertNote(String title, String description){
        Note note = new Note();
        note.title = title;
        note.text = description;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        note.timeStamp = formatter.format(calendar.getTime());

        NotesDao dao = AppDatabase.getDatabase(this).notesDao();
        AppDatabase.databaseWriteExecutor.execute(() -> dao.insertNote(note));
    }
}