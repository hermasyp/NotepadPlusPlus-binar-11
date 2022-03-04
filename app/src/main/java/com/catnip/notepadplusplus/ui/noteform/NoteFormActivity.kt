package com.catnip.notepadplusplus.ui.noteform

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.catnip.notepadplusplus.R
import com.catnip.notepadplusplus.base.arch.BaseActivity
import com.catnip.notepadplusplus.base.arch.GenericViewModelFactory
import com.catnip.notepadplusplus.base.model.Resource
import com.catnip.notepadplusplus.data.local.preference.UserPreference
import com.catnip.notepadplusplus.data.local.preference.datasource.PreferenceDataSourceImpl
import com.catnip.notepadplusplus.data.local.room.NotesDatabase
import com.catnip.notepadplusplus.data.local.room.datasource.NotesDataSourceImpl
import com.catnip.notepadplusplus.data.local.room.entity.Note
import com.catnip.notepadplusplus.databinding.ActivityNoteFormBinding
import com.catnip.notepadplusplus.utils.CommonConstant
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class NoteFormActivity :
    BaseActivity<ActivityNoteFormBinding, NoteFormViewModel>(ActivityNoteFormBinding::inflate),
    NoteFormContract.View {

    private var formMode = FORM_MODE_INSERT
    private var note: Note? = null
    private var pickedCardColor = DEFAULT_CARD_COLOR


    companion object {
        const val FORM_MODE_INSERT = 0
        const val FORM_MODE_EDIT = 1
        const val INTENT_FORM_MODE = "INTENT_FORM_MODE"
        const val INTENT_NOTE_DATA = "INTENT_NOTE_DATA"
        private const val DEFAULT_CARD_COLOR = "#d3bdff"

        @JvmStatic
        fun startActivity(context: Context?, formMode: Int, note: Note? = null) {
            val intent = Intent(context, NoteFormActivity::class.java)
            intent.putExtra(INTENT_FORM_MODE, formMode)
            note?.let {
                intent.putExtra(INTENT_NOTE_DATA, note)
            }
            context?.startActivity(intent)
        }
    }

    override fun getIntentData() {
        formMode = intent.getIntExtra(INTENT_FORM_MODE, FORM_MODE_INSERT)
        note = intent.getParcelableExtra(INTENT_NOTE_DATA)
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun initView() {
        getIntentData()
        initializeForm()
        setClickListeners()
        setPasswordToggleEnabled(getViewModel().isPasswordExist())
    }

    override fun initViewModel(): NoteFormViewModel {
        val repository = NoteFormRepository(
            NotesDataSourceImpl(NotesDatabase.getInstance(this).notesDao()),
            PreferenceDataSourceImpl(UserPreference(this))
        )
        return GenericViewModelFactory(NoteFormViewModel(repository)).create(
            NoteFormViewModel::class.java
        )
    }

    override fun observeData() {
        super.observeData()
        getViewModel().getNoteResultLiveData().observe(this) {
            when (it.first) {
                CommonConstant.ACTION_INSERT -> {
                    if (it.second is Resource.Success) {
                        showToast(getString(R.string.insert_success))
                    }
                    else{
                        showToast(getString(R.string.insert_failed))
                    }
                }
                CommonConstant.ACTION_DELETE -> {
                    if (it.second is Resource.Success) {
                        showToast(getString(R.string.delete_success))
                    }
                    else{
                        showToast(getString(R.string.delete_failed))
                    }
                }
                CommonConstant.ACTION_UPDATE -> {
                    if (it.second is Resource.Success) {
                        showToast(getString(R.string.update_success))
                    }
                    else{
                        showToast(getString(R.string.update_failed))
                    }
                }
            }

            finish()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_form_note, menu)
        val menuDelete = menu?.findItem(R.id.menu_delete)
        menuDelete?.isVisible = formMode == FORM_MODE_EDIT
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_save -> {
                saveNote()
                true
            }
            R.id.menu_delete -> {
                deleteNote()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                true
            }
        }
    }

    private fun validateForm(): Boolean {
        val title = getViewBinding().etNoteTitle.text.toString()
        var isFormValid = true
        //for checking is title empty
        if (title.isEmpty()) {
            isFormValid = false
            getViewBinding().tilNoteTitle.isErrorEnabled = true
            getViewBinding().tilNoteTitle.error = getString(R.string.error_form_note_title_empty)
        } else {
            getViewBinding().tilNoteTitle.isErrorEnabled = false
        }
        return isFormValid
    }

    private fun deleteNote() {
        if (formMode == FORM_MODE_EDIT) {
            note?.let {
                getViewModel().deleteNote(it)
            }
        }
    }

    private fun saveNote() {
        if (validateForm()) {
            if (formMode == FORM_MODE_EDIT) {
                // do edit
                note = note?.copy()?.apply {
                    title = getViewBinding().etNoteTitle.text.toString()
                    body = getViewBinding().etNoteBody.text.toString()
                    isArchived = getViewBinding().swArchiveNote.isChecked
                    isProtected = getViewBinding().swProtectNote.isChecked
                    hexCardColor = pickedCardColor
                }
                note?.let { getViewModel().updateNote(it) }
            } else {
                //do insert
                note = Note(
                    title = getViewBinding().etNoteTitle.text.toString(),
                    body = getViewBinding().etNoteBody.text.toString(),
                    isArchived = getViewBinding().swArchiveNote.isChecked,
                    isProtected = getViewBinding().swProtectNote.isChecked,
                    hexCardColor = pickedCardColor
                )
                note?.let { getViewModel().insertNote(it) }
            }
        }
    }

    private fun setPasswordToggleEnabled(isEnabled: Boolean) {
        getViewBinding().swProtectNote.isEnabled = isEnabled
    }

    private fun initializeForm() {
        if (formMode == FORM_MODE_EDIT) {
            note?.let {
                getViewBinding().etNoteTitle.setText(it.title)
                getViewBinding().etNoteBody.setText(it.body)
                getViewBinding().swArchiveNote.isChecked = it.isArchived ?: false
                getViewBinding().swProtectNote.isChecked = it.isProtected ?: false
                pickedCardColor = it.hexCardColor ?: DEFAULT_CARD_COLOR
            }
            //"Edit Data"
            supportActionBar?.title = getString(R.string.text_title_edit_note_form_activity)
        } else {
            supportActionBar?.title = getString(R.string.text_title_insert_note_form_activity)
        }
        getViewBinding().ivColorPreview.setBackgroundColor(Color.parseColor(pickedCardColor))
    }

    private fun showColorPickerDialog() {
        MaterialColorPickerDialog
            .Builder(this)                            // Pass Activity Instance
            .setTitle(getString(R.string.text_color_picker_title))                // Default "Choose Color"
            .setColorShape(ColorShape.SQAURE)    // Default ColorShape.CIRCLE
            .setColorSwatch(ColorSwatch._300)    // Default ColorSwatch._500
            .setDefaultColor(pickedCardColor)        // Pass Default Color
            .setColorListener { _, colorHex ->
                // Handle Color Selection
                pickedCardColor = colorHex
                getViewBinding().ivColorPreview.setBackgroundColor(Color.parseColor(pickedCardColor))
            }
            .show()
    }

    private fun setClickListeners() {
        getViewBinding().llCardColor.setOnClickListener {
            showColorPickerDialog()
        }
    }

}