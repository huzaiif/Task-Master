package com.example.taskmaster.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.databinding.NoteLayoutBinding
import com.example.taskmaster.fragments.HomeFragmentDirections
import com.example.taskmaster.model.Note
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val binding: NoteLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = differ.currentList[position]

        holder.binding.apply {

            noteTitle.text = note.noteTitle
            noteDesc.text = note.noteDesc

            // Format Time
            val formatter = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
            noteTime.text = formatter.format(Date(note.taskTime))

            // Priority Badge Coloring
            when (note.priority) {
                1 -> {
                    priorityBadge.text = "HIGH"
                    priorityBadge.setBackgroundColor(Color.parseColor("#FF3B30")) // Red
                }
                2 -> {
                    priorityBadge.text = "MEDIUM"
                    priorityBadge.setBackgroundColor(Color.parseColor("#FF9500")) // Orange
                }
                3 -> {
                    priorityBadge.text = "LOW"
                    priorityBadge.setBackgroundColor(Color.parseColor("#34C759")) // Green
                }
            }

            root.setOnClickListener {
                val direction = HomeFragmentDirections
                    .actionHomeFragmentToEditNoteFragment(note)

                it.findNavController().navigate(direction)
            }
        }
    }
}
