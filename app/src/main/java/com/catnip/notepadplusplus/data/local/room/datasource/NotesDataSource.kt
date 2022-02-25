package com.catnip.notepadplusplus.data.local.room.datasource

import com.catnip.notepadplusplus.data.local.room.entity.Note

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
interface NotesDataSource {
    suspend fun getAllNotes() : List<Note>

    suspend fun getArchivedNotes() : List<Note>

    suspend fun getNoteById(id : Int) : List<Note>

    suspend fun insertNote(note : Note) : Long

    suspend fun deleteNote(note : Note) : Int

    suspend fun updateNote(note : Note) : Int
}