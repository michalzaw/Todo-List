package com.mich.todolist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mich.todolist.R;
import com.mich.todolist.models.TaskEntity;

import java.util.Collections;
import java.util.List;

/**
 * Created by Michal on 01.11.2017.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private List<TaskEntity> tasksList = Collections.emptyList();

    public TasksAdapter(List<TaskEntity> tasksList) {
        this.tasksList = tasksList;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recyclerrow_tasks, parent, false);

        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        TaskEntity task = tasksList.get(position);

        holder.textViewTitle.setText(task.getTitle());
        holder.textViewDate.setText(task.getDate());
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewDate;

        public TasksViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textView_title);
            textViewDate = itemView.findViewById(R.id.textView_date);
        }
    }
}
