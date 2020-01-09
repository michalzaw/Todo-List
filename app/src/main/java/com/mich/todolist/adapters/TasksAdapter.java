package com.mich.todolist.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mich.todolist.R;
import com.mich.todolist.models.TaskEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michal on 01.11.2017.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> implements Filterable {

    private List<TaskEntity> tasksList;
    private List<TaskEntity> tasksListOriginal;
    private OnClickListener onClickListener;

    public TasksAdapter(List<TaskEntity> tasksList) {
        this.tasksList = tasksList;
        this.tasksListOriginal = tasksList;
    }

    public void setTasks(List<TaskEntity> tasksList) {
        this.tasksList = tasksList;
        this.tasksListOriginal = tasksList;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
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

        holder.imageViewDone.setVisibility(task.isDone() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(view -> {
            if (onClickListener != null)
                onClickListener.onClick(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            final FilterResults filterResults = new FilterResults();

            if (charSequence != null) {
                String searchedString = charSequence.toString().toLowerCase();

                List<TaskEntity> results = new ArrayList<>();
                for (final TaskEntity taskEntity : tasksListOriginal) {
                    if (taskEntity.getTitle().toLowerCase().contains(searchedString)) {
                        results.add(taskEntity);
                    }
                }

                filterResults.values = results;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            tasksList = (ArrayList<TaskEntity>) filterResults.values;

            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return filter;
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
        private ImageView imageViewDone;
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
            imageViewDone = itemView.findViewById(R.id.imageViewDone);
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

    public interface OnClickListener {
        void onClick(int position);
    }
}
