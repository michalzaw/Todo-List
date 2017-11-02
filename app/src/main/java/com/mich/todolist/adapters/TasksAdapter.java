package com.mich.todolist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mich.todolist.R;
import com.mich.todolist.models.Task;

import java.util.List;

/**
 * Created by Michal on 01.11.2017.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private List<Task> tasksList;

    public TasksAdapter(List<Task> tasksList) {
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
        Task task = tasksList.get(position);

        holder.textViewTitle.setText(task.getTitle());
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;

        public TasksViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textView_title);
        }
    }
}
