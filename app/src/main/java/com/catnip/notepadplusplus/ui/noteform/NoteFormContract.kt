package com.catnip.notepadplusplus.ui.noteform

import androidx.lifecycle.MutableLiveData
import com.catnip.notepadplusplus.base.arch.BaseContract
import com.catnip.notepadplusplus.base.model.Resource
import com.catnip.notepadplusplus.data.local.room.entity.Note

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
interface NoteFormContract {
    interface View : BaseContract.BaseView {
        fun getIntentData()
        fun showToast(msg: String)
    }

    interface ViewModel : BaseContract.BaseViewModel {
        fun insertNote(note: Note)
        fun deleteNote(note: Note)
        fun updateNote(note: Note)
        fun isPasswordExist(): Boolean
        fun getNoteResultLiveData(): MutableLiveData<Pair<String, Resource<Number>>>
    }

    interface Repository : BaseContract.BaseRepository {
        suspend fun insertNote(note: Note): Long
        suspend fun deleteNote(note: Note): Int
        suspend fun updateNote(note: Note): Int
        fun isPasswordExist(): Boolean
    }
}