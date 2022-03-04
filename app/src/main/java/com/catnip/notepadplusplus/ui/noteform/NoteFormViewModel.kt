package com.catnip.notepadplusplus.ui.noteform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.catnip.notepadplusplus.base.arch.BaseViewModelImpl
import com.catnip.notepadplusplus.base.model.Resource
import com.catnip.notepadplusplus.data.local.room.entity.Note
import com.catnip.notepadplusplus.utils.CommonConstant.ACTION_DELETE
import com.catnip.notepadplusplus.utils.CommonConstant.ACTION_INSERT
import com.catnip.notepadplusplus.utils.CommonConstant.ACTION_UPDATE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/

class NoteFormViewModel(private val repository: NoteFormRepository) : BaseViewModelImpl(),
    NoteFormContract.ViewModel {

    private val noteResultLiveData = MutableLiveData<Pair<String, Resource<Number>>>()

    override fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val insertedRowId = repository.insertNote(note)
                viewModelScope.launch(Dispatchers.Main) {
                    if (insertedRowId > 0) {
                        noteResultLiveData.value =
                            Pair(ACTION_INSERT, Resource.Success(insertedRowId))
                    } else {
                        noteResultLiveData.value =
                            Pair(ACTION_INSERT, Resource.Error("", insertedRowId))
                    }
                }
            } catch (exception: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    noteResultLiveData.value =
                        Pair(ACTION_INSERT, Resource.Error(exception.message.orEmpty()))
                }
            }
        }
    }

    override fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val affectedRow = repository.deleteNote(note)
                viewModelScope.launch(Dispatchers.Main) {
                    if (affectedRow > 0) {
                        noteResultLiveData.value =
                            Pair(ACTION_DELETE, Resource.Success(affectedRow))
                    } else {
                        noteResultLiveData.value =
                            Pair(ACTION_DELETE, Resource.Error("", affectedRow))
                    }
                }
            } catch (exception: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    noteResultLiveData.value =
                        Pair(ACTION_DELETE, Resource.Error(exception.message.orEmpty()))
                }
            }
        }
    }

    override fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val affectedRow = repository.updateNote(note)
                viewModelScope.launch(Dispatchers.Main) {
                    if (affectedRow > 0) {
                        noteResultLiveData.value =
                            Pair(ACTION_UPDATE, Resource.Success(affectedRow))
                    } else {
                        noteResultLiveData.value =
                            Pair(ACTION_UPDATE, Resource.Error("", affectedRow))
                    }
                }
            } catch (exception: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    noteResultLiveData.value =
                        Pair(ACTION_UPDATE, Resource.Error(exception.message.orEmpty()))
                }
            }
        }
    }


    override fun isPasswordExist(): Boolean = repository.isPasswordExist()

    override fun getNoteResultLiveData(): MutableLiveData<Pair<String, Resource<Number>>> =
        noteResultLiveData


}