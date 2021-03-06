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
import com.mich.todolist.database.ApplicationDatabase;
import com.mich.todolist.database.DeleteTaskAsyncTask;
import com.mich.todolist.database.LoadTasksListAsyncTask;
import com.mich.todolist.database.TaskDao;
import com.mich.todolist.models.TaskEntity;
import com.mich.todolist.utilities.IntentExtras;
import com.mich.todolist.utilities.RecyclerItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TasksListActivity extends AppCompatActivity
        implements LoadTasksListAsyncTask.LoadTasksListObserver, RecyclerItemTouchHelperCallback.RecyclerItemTouchHelperListener,
        DeleteTaskAsyncTask.DeleteTaskObserver {

    private static final int REQUEST_CODE_ADD_TASK = 0;

    @BindView(R.id.recycerView_tasks)
    RecyclerView recyclerViewTasks;

    private List<TaskEntity> tasks;
    private TasksAdapter tasksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);

        ButterKnife.bind(this);

        LoadTasksListAsyncTask.getInstance(getApplicationContext()).addObserver(this);
        DeleteTaskAsyncTask.getInstance(getApplicationContext()).addObserver(this);

        loadTasks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LoadTasksListAsyncTask.getInstance(getApplicationContext()).removeObserver(this);
        DeleteTaskAsyncTask.getInstance(getApplicationContext()).removeObserver(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_TASK && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                TaskEntity task = extras.getParcelable(IntentExtras.NEW_TASK);

                tasksAdapter.addTask(task);
            }
        }
    }

    private void initRecycler() {
        tasksAdapter = new TasksAdapter(tasks);
        recyclerViewTasks.setAdapter(tasksAdapter);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTasks.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelperCallback(
                this, 0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT));
        itemTouchHelper.attachToRecyclerView(recyclerViewTasks);
    }

    private void loadTasks() {
        LoadTasksListAsyncTask.getInstance(getApplicationContext()).execute();
    }

    @Override
    public void onTasksLoaded(List<TaskEntity> tasks) {
        this.tasks = tasks;

        initRecycler();
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
        startActivityForResult(intent, REQUEST_CODE_ADD_TASK);
    }
}
