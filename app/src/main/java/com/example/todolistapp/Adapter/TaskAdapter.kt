package com.example.todolistapp.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.model.Task
import com.example.todolistapp.databinding.CardItemBinding

class TaskAdapter(var taskList : ArrayList<Task>) : RecyclerView.Adapter<TaskAdapter.taskHolder>() {

    inner class taskHolder(val binding : CardItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): taskHolder {
        val design = CardItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return taskHolder(design)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }


    override fun onBindViewHolder(holder: taskHolder, position: Int) {
        val task = taskList[position]
        holder.binding.task = task

        if(task.priority!!){
            holder.binding.ivPriority.visibility = View.VISIBLE
        }

        val isVisible = task.visibility
        holder.binding.clExpand.visibility = if(isVisible)  View.VISIBLE  else  View.GONE


        holder.binding.tvTitle.setOnClickListener {
            task.visibility = !task.visibility
            notifyItemChanged(position)
        }


    }




}