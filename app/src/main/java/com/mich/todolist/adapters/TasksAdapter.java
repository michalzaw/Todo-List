package com.mich.todolist.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    public TaskEntity getTask(int index) {
        return tasksList.get(index);
    }

    public void addTask(TaskEntity task) {
        tasksList.add(task);

        notifyItemInserted(tasksList.size() -  1);
    }

    public void removeTask(int index) {
        tasksList.remove(index);

        notifyItemRemoved(index);
    }

    public class TasksViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewDate;
        private View foregroundView;
        private ImageView imageViewDeleteLeft;
        private TextView textViewDeleteLeft;
        private ImageView imageViewDeleteRight;
        private TextView textViewDeleteRight;

        public static final int LEFT = 1;
        public static final int RIGHT = 2;

        public TasksViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textView_title);
            textViewDate = itemView.findViewById(R.id.textView_date);
            foregroundView = itemView.findViewById(R.id.foregroundView);
            imageViewDeleteLeft = itemView.findViewById(R.id.imageViewDeleteLeft);
            textViewDeleteLeft = itemView.findViewById(R.id.textViewDeleteLeft);
            imageViewDeleteRight= itemView.findViewById(R.id.imageViewDeleteRight);
            textViewDeleteRight = itemView.findViewById(R.id.textViewDeleteRight);
        }

        public View getForegroundView() {
            return foregroundView;
        }

        public void setDeleteLabelVisibility(int dir) {
            if (dir == LEFT) {
                imageViewDeleteLeft.setVisibility(View.VISIBLE);
                textViewDeleteLeft.setVisibility(View.VISIBLE);
                imageViewDeleteRight.setVisibility(View.GONE);
                textViewDeleteRight.setVisibility(View.GONE);
            } else if (dir == RIGHT) {
                imageViewDeleteRight.setVisibility(View.VISIBLE);
                textViewDeleteRight.setVisibility(View.VISIBLE);
                imageViewDeleteLeft.setVisibility(View.GONE);
                textViewDeleteLeft.setVisibility(View.GONE);

            }
        }
    }
}
