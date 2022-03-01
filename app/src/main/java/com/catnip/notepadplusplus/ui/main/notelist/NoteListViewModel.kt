package com.catnip.notepadplusplus.ui.main.notelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.catnip.notepadplusplus.base.arch.BaseViewModelImpl
import com.catnip.notepadplusplus.base.model.Resource
import com.catnip.notepadplusplus.data.local.room.entity.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class NoteListViewModel(private val repository: NoteListRepository) : BaseViewModelImpl(),
    NoteListContract.ViewModel {

    private val notesData = MutableLiveData<Resource<List<Note>>>()

    override fun getAllNotes() {
        //mainthread
        notesData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            //io thread
            try {
                delay(2000)
                val notes = repository.getAllNotes()
                viewModelScope.launch(Dispatchers.Main) {
                    //main thread
                    //set the data
                    notesData.value = Resource.Success(notes)
                }
            }catch (e : Exception){
                viewModelScope.launch(Dispatchers.Main) {
                    //set the error message
                    notesData.value = Resource.Error(e.message.orEmpty())
                }
            }
        }
    }

    override fun getArchivedNotes() {
        //mainthread
        notesData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            //io thread
            try {
                delay(2000)
                val notes = repository.getArchivedNotes()
                viewModelScope.launch(Dispatchers.Main) {
                    //main thread
                    //set the data
                    notesData.value = Resource.Success(notes)
                }
            }catch (e : Exception){
                viewModelScope.launch(Dispatchers.Main) {
                    //set the error message
                    notesData.value = Resource.Error(e.message.orEmpty())
                }
            }
        }
    }

    override fun getNotesLiveData(): MutableLiveData<Resource<List<Note>>> = notesData

}