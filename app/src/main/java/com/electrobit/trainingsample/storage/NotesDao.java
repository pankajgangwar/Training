package com.electrobit.trainingsample.storage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotesDao {
    @Query("SELECT * FROM notes_table ")
    List<Note> getAll();

    @Query("SELECT * FROM notes_table WHERE title LIKE :searchQuery LIMIT 1")
    Note findNote(String searchQuery);

    @Insert
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

}
