package com.catnip.notepadplusplus.ui.main.notelist.adapter;

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.catnip.notepadplusplus.data.local.room.entity.Note
import com.catnip.notepadplusplus.databinding.ActivityMainBinding.inflate
import com.catnip.notepadplusplus.databinding.ItemLockedNoteBinding
import com.catnip.notepadplusplus.databinding.ItemNoteBinding
import com.catnip.notepadplusplus.utils.CommonFunction
import com.catnip.notepadplusplus.utils.dpToPixels

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class NotesAdapter(private val itemClick: (Note) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_TYPE_NOTES = 0
        const val ITEM_TYPE_PROTECTED_NOTES = 1
    }

    private var items: MutableList<Note> = mutableListOf()

    fun setItems(items: List<Note>) {
        clearItems()
        addItems(items)
        notifyDataSetChanged()
    }

    fun addItems(items: List<Note>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clearItems() {
        this.items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return return if(viewType == ITEM_TYPE_PROTECTED_NOTES){
            val binding = ItemLockedNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ProtectedNoteViewHolder(binding, itemClick)
        }else{
            val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            NoteViewHolder(binding, itemClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //attach the data
        if(holder is ProtectedNoteViewHolder){
            holder.bindView(items[position])
        }else if(holder is NoteViewHolder){
            holder.bindView(items[position])
        }
    }

    override fun getItemCount(): Int = items.size


    override fun getItemViewType(position: Int): Int =
        if (items[position].isProtected == true)
            ITEM_TYPE_PROTECTED_NOTES
        else
            ITEM_TYPE_NOTES


    class NoteViewHolder(private val binding: ItemNoteBinding, val itemClick: (Note) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Note) {
            with(item) {
                binding.tvTitleNote.text = item.title
                binding.tvBodyNote.text = item.body
                binding.root.background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = CommonFunction.dpToPixels(binding.root.context, 8).toFloat()
                    setColor(Color.parseColor(item.hexCardColor))
                    mutate()
                }
                itemView.setOnClickListener { itemClick(this) }
            }

        }
    }

    class ProtectedNoteViewHolder(
        private val binding: ItemLockedNoteBinding,
        val itemClick: (Note) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Note) {
            with(item) {
                binding.tvTitleNote.text = item.title
                binding.root.background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = 8.dpToPixels(itemView.context).toFloat()
                    setColor(Color.parseColor(item.hexCardColor))
                    mutate()
                }
                itemView.setOnClickListener { itemClick(this) }
            }

        }
    }


}