package com.example.taskmaster.fragments

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.taskmaster.MainActivity
import com.example.taskmaster.R
import com.example.taskmaster.databinding.FragmentEditNoteBinding
import com.example.taskmaster.model.Note
import com.example.taskmaster.notification.AlarmReceiver
import com.example.taskmaster.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*

class EditNoteFragment : Fragment(), MenuProvider {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var currentNote: Note

    private val args: EditNoteFragmentArgs by navArgs()

    private var selectedTimeMillis: Long = 0
    private var selectedPriority: Int = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        notesViewModel = (activity as MainActivity).noteViewModel

        args.note?.let { currentNote = it } ?: return

        selectedTimeMillis = currentNote.taskTime
        selectedPriority = currentNote.priority

        setupUI()
        setupPriorityDropdown()
        setupDateTimePicker()

        binding.editNoteFab.setOnClickListener { updateTask(view) }
    }


    /** ------- PREFILL UI ------- **/
    private fun setupUI() {
        binding.editNoteTitle.setText(currentNote.noteTitle)
        binding.editNoteDesc.setText(currentNote.noteDesc)

        val formatter = SimpleDateFormat("dd MMM yyyy - hh:mm a", Locale.getDefault())
        binding.editTaskTimeField.setText(formatter.format(Date(currentNote.taskTime)))
    }


    /** ------- PRIORITY SPINNER ------- **/
    private fun setupPriorityDropdown() {
        val items = listOf("High", "Medium", "Low")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        binding.editPrioritySpinner.adapter = adapter
        binding.editPrioritySpinner.setSelection(currentNote.priority - 1)

        binding.editPrioritySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                    selectedPriority = pos + 1
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }


    /** ------- DATE + TIME PICKER ------- **/
    private fun setupDateTimePicker() {
        binding.editTaskTimeField.setOnClickListener {

            val cal = Calendar.getInstance()

            DatePickerDialog(requireContext(), { _, y, m, d ->

                TimePickerDialog(requireContext(), { _, h, min ->

                    val newCal = Calendar.getInstance()
                    newCal.set(y, m, d, h, min, 0)

                    selectedTimeMillis = newCal.timeInMillis

                    binding.editTaskTimeField.setText(
                        String.format("%02d-%02d-%d   %02d:%02d", d, m + 1, y, h, min)
                    )

                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show()
        }
    }


    /** ------- UPDATE TASK + NOTIFICATION ------- **/
    private fun updateTask(view: View) {

        val title = binding.editNoteTitle.text.toString().trim()
        val desc = binding.editNoteDesc.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter title", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedNote = Note(
            id = currentNote.id,
            noteTitle = title,
            noteDesc = desc,
            taskTime = selectedTimeMillis,
            priority = selectedPriority
        )

        cancelNotification(currentNote.id)          // Remove old alarm
        scheduleNotification(updatedNote)           // Add new one
        notesViewModel.updateNote(updatedNote)

        Toast.makeText(requireContext(), "Task Updated", Toast.LENGTH_SHORT).show()
        view.findNavController().popBackStack(R.id.homeFragment, false)
    }


    /** ------- DELETE TASK + STOP NOTIFICATION ------- **/
    private fun deleteNote() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Task")
            .setMessage("Do you want to delete this task?")
            .setPositiveButton("Delete") { _, _ ->

                cancelNotification(currentNote.id)
                notesViewModel.deleteNote(currentNote)

                Toast.makeText(requireContext(), "Task Deleted", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    /** ------- SCHEDULE ALARM ------- **/
    private fun scheduleNotification(note: Note) {
        val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
            putExtra("TASK_TITLE", note.noteTitle)
            putExtra("TASK_DESC", note.noteDesc)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            note.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            note.taskTime,
            pendingIntent
        )
    }


    /** ------- CANCEL ALARM ------- **/
    private fun cancelNotification(noteId: Int) {
        val intent = Intent(requireContext(), AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            noteId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }


    /** ------- MENU CONFIG ------- **/
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.deleteMenu -> {
                deleteNote()
                true
            }
            else -> false
        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
