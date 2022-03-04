package com.catnip.notepadplusplus.ui.noteform

import com.catnip.notepadplusplus.data.local.preference.datasource.PreferenceDataSource
import com.catnip.notepadplusplus.data.local.room.datasource.NotesDataSource
import com.catnip.notepadplusplus.data.local.room.entity.Note

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class NoteFormRepository(
    private val notesDataSource: NotesDataSource,
    private val preferenceDataSource: PreferenceDataSource
) :
    NoteFormContract.Repository {
    override suspend fun insertNote(note: Note): Long {
        return notesDataSource.insertNote(note)
    }

    override suspend fun deleteNote(note: Note): Int {
        return notesDataSource.deleteNote(note)
    }

    override suspend fun updateNote(note: Note): Int {
        return notesDataSource.updateNote(note)
    }

    override fun isPasswordExist(): Boolean {
        return preferenceDataSource.isPasswordExist()
    }
}