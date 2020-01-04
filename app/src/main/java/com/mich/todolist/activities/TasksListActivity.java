package com.mich.todolist.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.mich.todolist.R;
import com.mich.todolist.adapters.TasksAdapter;
import com.mich.todolist.database.DeleteTaskAsyncTask;
import com.mich.todolist.database.LoadTasksListAsyncTask;
import com.mich.todolist.models.TaskEntity;
import com.mich.todolist.utilities.IntentExtras;
import com.mich.todolist.utilities.RecyclerItemTouchHelperCallback;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TasksListActivity extends AppCompatActivity
        implements LoadTasksListAsyncTask.LoadTasksListObserver, RecyclerItemTouchHelperCallback.RecyclerItemTouchHelperListener,
        DeleteTaskAsyncTask.DeleteTaskObserver {

    @BindView(R.id.recycerView_tasks)
    RecyclerView recyclerViewTasks;

    private List<TaskEntity> tasks = Collections.emptyList();
    private TasksAdapter tasksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);

        ButterKnife.bind(this);

        LoadTasksListAsyncTask.getInstance(getApplicationContext()).addObserver(this);
        DeleteTaskAsyncTask.getInstance(getApplicationContext()).addObserver(this);

        initRecycler();
        //loadTasks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LoadTasksListAsyncTask.getInstance(getApplicationContext()).removeObserver(this);
        DeleteTaskAsyncTask.getInstance(getApplicationContext()).removeObserver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadTasks();
    }

    private void initRecycler() {
        tasksAdapter = new TasksAdapter(tasks);
        recyclerViewTasks.setAdapter(tasksAdapter);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTasks.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelperCallback(
                this, 0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT));
        itemTouchHelper.attachToRecyclerView(recyclerViewTasks);

        tasksAdapter.setOnClickListener(position -> openTaskDetails(tasksAdapter.getTask(position)));
    }

    private void loadTasks() {
        LoadTasksListAsyncTask.getInstance(getApplicationContext()).execute();
    }

    private void openTaskDetails(TaskEntity task) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra(IntentExtras.TASK, task);
        startActivity(intent);
    }

    @Override
    public void onTasksLoaded(List<TaskEntity> tasks) {
        this.tasks = tasks;

        tasksAdapter.setTasks(tasks);
        tasksAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (DeleteTaskAsyncTask.getInstance(getApplicationContext()).getStatus() != AsyncTask.Status.RUNNING) {
            DeleteTaskAsyncTask.getInstance(getApplicationContext())
                    .execute(tasksAdapter.getTask(viewHolder.getAdapterPosition()));

            tasksAdapter.removeTask(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onDeletedTask(TaskEntity taskEntity) {

    }

    @OnClick(R.id.floatingActionButton_addTask)
    void onFloatingActionButtonAddTaskClick() {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }
}
