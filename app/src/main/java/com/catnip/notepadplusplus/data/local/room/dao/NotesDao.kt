package com.catnip.notepadplusplus.data.local.room.dao

import androidx.room.*
import com.catnip.notepadplusplus.data.local.room.entity.Note

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/

@Dao // Data Access Object
interface NotesDao {
    @Query("SELECT * FROM NOTES WHERE IS_ARCHIVED == 0")
    suspend fun getAllNotes() : List<Note>

    @Query("SELECT * FROM NOTES WHERE IS_ARCHIVED == 1")
    suspend fun getArchivedNotes() : List<Note>

    @Query("SELECT * FROM NOTES WHERE ID == :id")
    suspend fun getNoteById(id : Int) : List<Note>

    @Insert
    suspend fun insertNote(note : Note) : Long

    @Delete
    suspend fun deleteNote(note : Note) : Int

    @Update
    suspend fun updateNote(note : Note) : Int
}