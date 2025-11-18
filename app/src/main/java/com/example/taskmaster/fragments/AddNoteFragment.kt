package com.example.taskmaster.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.taskmaster.MainActivity
import com.example.taskmaster.R
import com.example.taskmaster.databinding.FragmentAddNoteBinding
import com.example.taskmaster.model.Note
import com.example.taskmaster.viewmodel.NoteViewModel
import java.util.*

class AddNoteFragment : Fragment(), MenuProvider {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var addNoteView: View

    private var selectedTimeMillis: Long = 0
    private var selectedPriority: Int = 2   // Default: Medium

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        notesViewModel = (activity as MainActivity).noteViewModel
        addNoteView = view

        setupPriorityDropdown()
        setupDateTimePicker()
    }

    /** ---------------- PRIORITY DROPDOWN ---------------- **/
    private fun setupPriorityDropdown() {

        val items = listOf("High", "Medium", "Low")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        binding.prioritySpinner.adapter = adapter

        binding.prioritySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedPriority = position + 1   // 1=High, 2=Medium, 3=Low
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }


    /** ---------------- DATE & TIME PICKER ---------------- **/
    private fun setupDateTimePicker() {

        binding.taskTimeField.setOnClickListener {

            val calendar = Calendar.getInstance()

            DatePickerDialog(requireContext(), { _, y, m, d ->

                TimePickerDialog(requireContext(), { _, hour, min ->

                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(y, m, d, hour, min, 0)

                    selectedTimeMillis = selectedCalendar.timeInMillis

                    binding.taskTimeField.setText(
                        "$d-${m + 1}-$y   ${String.format("%02d:%02d", hour, min)}"
                    )

                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show()
        }
    }


    /** ---------------- SAVE TASK TO DATABASE ---------------- **/
    private fun saveNote(view: View) {
        val noteTitle = binding.addNoteTitle.text.toString().trim()
        val noteDesc = binding.addNoteDesc.text.toString().trim()

        if (noteTitle.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter task title", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedTimeMillis == 0L) {
            Toast.makeText(requireContext(), "Please select task time", Toast.LENGTH_SHORT).show()
            return
        }

        val note = Note(
            id = 0,
            noteTitle = noteTitle,
            noteDesc = noteDesc,
            taskTime = selectedTimeMillis,
            priority = selectedPriority
        )

        notesViewModel.addNote(note)

        Toast.makeText(requireContext(), "Task Saved", Toast.LENGTH_SHORT).show()
        view.findNavController().popBackStack(R.id.homeFragment, false)
    }


    /** ---------------- MENU CONFIG ---------------- **/
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.saveMenu -> {
                saveNote(addNoteView)
                true
            }
            else -> false
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
