package com.catnip.notepadplusplus.ui.main.notelist

import com.catnip.notepadplusplus.data.local.room.datasource.NotesDataSource
import com.catnip.notepadplusplus.data.local.room.entity.Note

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class NoteListRepository(private val notesDataSource: NotesDataSource) : NoteListContract.Repository {

    override suspend fun getAllNotes(): List<Note> {
        return notesDataSource.getAllNotes()
    }

    override suspend fun getArchivedNotes(): List<Note> {
        return notesDataSource.getArchivedNotes()
    }

}