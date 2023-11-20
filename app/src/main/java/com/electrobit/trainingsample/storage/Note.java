package com.electrobit.trainingsample.storage;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes_table")
public class Note {
    @PrimaryKey(autoGenerate = true)
    int id;

    @NonNull
    public String title;

    public String text;

    @NonNull
    public String timeStamp;
}
