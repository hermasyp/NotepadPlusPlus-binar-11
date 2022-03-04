package com.catnip.notepadplusplus.ui.main.notelist

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.catnip.notepadplusplus.R
import com.catnip.notepadplusplus.base.arch.BaseFragment
import com.catnip.notepadplusplus.base.arch.GenericViewModelFactory
import com.catnip.notepadplusplus.base.model.Resource
import com.catnip.notepadplusplus.data.local.room.NotesDatabase
import com.catnip.notepadplusplus.data.local.room.datasource.NotesDataSourceImpl
import com.catnip.notepadplusplus.data.local.room.entity.Note
import com.catnip.notepadplusplus.databinding.FragmentNoteListBinding
import com.catnip.notepadplusplus.ui.enterpassword.EnterPasswordBottomSheet
import com.catnip.notepadplusplus.ui.main.notelist.adapter.NotesAdapter
import com.catnip.notepadplusplus.ui.noteform.NoteFormActivity
import com.catnip.notepadplusplus.utils.CommonFunction
import com.catnip.notepadplusplus.utils.SpacesItemDecoration

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class NoteListFragment(private val isArchiveOnly: Boolean = false) :
    BaseFragment<FragmentNoteListBinding, NoteListViewModel>(FragmentNoteListBinding::inflate),
    NoteListContract.View {

    private lateinit var adapter: NotesAdapter

    override fun initView() {
        setupRecyclerView()
        setupSwipeRefresh()
        getData()
    }

    override fun initViewModel(): NoteListViewModel {
        val dataSource = NotesDataSourceImpl(NotesDatabase.getInstance(requireContext()).notesDao())
        val repository = NoteListRepository(dataSource)
        return GenericViewModelFactory(NoteListViewModel(repository)).create(NoteListViewModel::class.java)
    }

    override fun setupRecyclerView() {
        adapter = NotesAdapter {
            if (it.isProtected == true) {
                showDialogPassword(it)
            } else {
                NoteFormActivity.startActivity(context, NoteFormActivity.FORM_MODE_EDIT, it)
            }
        }
        getViewBinding().rvNotes.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            addItemDecoration(SpacesItemDecoration(CommonFunction.dpToPixels(requireContext(), 8)))
            adapter = this@NoteListFragment.adapter
        }
    }

    override fun setupSwipeRefresh() {
        getViewBinding().srlNotes.setOnRefreshListener {
            getData()
            getViewBinding().srlNotes.isRefreshing = false
        }
    }

    override fun setListData(data: List<Note>) {
        adapter.setItems(data)
    }

    override fun getData() {
        if (isArchiveOnly) {
            getViewModel().getArchivedNotes()
        } else {
            getViewModel().getAllNotes()
        }
    }

    override fun showDialogPassword(note: Note) {
        EnterPasswordBottomSheet { isPasswordConfirmed ->
            if (isPasswordConfirmed) {
                NoteFormActivity.startActivity(context, NoteFormActivity.FORM_MODE_EDIT, note)
            } else {
                Toast.makeText(context, getString(R.string.text_wrong_password), Toast.LENGTH_SHORT)
                    .show()
            }
        }.show(childFragmentManager, null)
    }



    override fun showLoading(isLoading: Boolean) {
        super.showLoading(isLoading)
        getViewBinding().layoutScenario.progressBar.isVisible = isLoading
    }

    override fun showContent(isContentVisible: Boolean) {
        super.showContent(isContentVisible)
        getViewBinding().rvNotes.isVisible = isContentVisible
    }

    override fun showError(isErrorEnabled: Boolean, msg: String?) {
        getViewBinding().layoutScenario.tvMessage.isVisible = isErrorEnabled
        getViewBinding().layoutScenario.tvMessage.text = msg
    }

    override fun observeData() {
        getViewModel().getNotesLiveData().observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                    showError(false, null)
                    showContent(false)
                }
                is Resource.Success -> {
                    showLoading(false)
                    it.data?.let { notes ->
                        if (notes.isEmpty()) {
                            showError(true, getString(R.string.text_empty_notes))
                            showContent(false)
                        } else {
                            showError(false, null)
                            showContent(true)
                            setListData(notes)
                        }
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                    showError(true, it.message)
                    showContent(false)
                }
            }
        }
    }
}