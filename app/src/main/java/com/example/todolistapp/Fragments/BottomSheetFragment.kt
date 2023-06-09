package com.example.todolistapp.Fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.todolistapp.R
import com.example.todolistapp.model.Task
import com.example.todolistapp.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.format.DateTimeFormatter
import java.util.*


class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding : FragmentBottomSheetBinding?=null
    private val binding get() = _binding!!
    private var time : String = ""
    private var auth = FirebaseAuth.getInstance()
    private var isPriority : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddTask.setOnClickListener { checkData() }
        binding.btnTimePicker.setOnClickListener { openTimePicker() }

        binding.radioG.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.rbToday ->  time = "Today"
                R.id.rbTomorrow -> time = "Tomorrow"
                R.id.rbWeekend -> time = "This weekend"
            }

        }


    }

    private fun checkData(){
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()
        isPriority = binding.cbPriority.isChecked

        if(title.isEmpty()){
            Toast("Title field cannot be empty")
        }else if(description.isEmpty()){
            Toast("Description field cannot be empty")
        }else if(time == ""){
            Toast("Choose the time") }
        else{
            val task = Task(description,isPriority,time,title)
            saveData(task)
        }


    }

    private fun saveData(task : Task) {
        val uid = UUID.randomUUID().toString()
        task.id = uid
        val ref = FirebaseDatabase.getInstance().reference
        ref.child("Tasks").child(auth.currentUser?.uid.toString()).child(uid).setValue(task).addOnCompleteListener {
            if (it.isSuccessful){
                Toast("Task successfully added")
                dismiss()
            }else{
                Toast(it.exception?.message.toString())
            }
        }
    }

    private fun openTimePicker() {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Set alarm")
            .build()
        picker.show(childFragmentManager,"TAG")

        picker.addOnPositiveButtonClickListener {
            picker.minute
            time = "${picker.hour}:${picker.minute}"
        }


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun Toast(text:String){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }

}