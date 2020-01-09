package com.mich.todolist.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.mich.todolist.R;
import com.mich.todolist.adapters.TasksAdapter;
import com.mich.todolist.database.DeleteTaskAsyncTask;
import com.mich.todolist.database.LoadTasksListAsyncTask;
import com.mich.todolist.models.TaskEntity;
import com.mich.todolist.utilities.IntentExtras;
import com.mich.todolist.utilities.RecyclerItemTouchHelperCallback;
import com.mich.todolist.utilities.SortType;
import com.mich.todolist.utilities.TaskComparator;

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

    private SortType sortType = SortType.ALPHABETICAL;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tasks_list, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                tasksAdapter.getFilter().filter(s);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            showSortingOptionsDialog();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void showSortingOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(getResources().getString(R.string.sorting))
                .setSingleChoiceItems(getResources().getStringArray(R.array.sorting_array), sortType.getValue(), (dialogInterface, i) -> {
                    sortType = SortType.fromValue(i);
                    Collections.sort(tasks, new TaskComparator(sortType));
                    tasksAdapter.notifyDataSetChanged();

                    dialogInterface.dismiss();
                })
                .create()
                .show();

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
        Collections.sort(tasks, new TaskComparator(sortType));
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
