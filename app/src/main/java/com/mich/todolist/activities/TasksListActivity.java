package com.mich.todolist.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;

import com.mich.todolist.R;
import com.mich.todolist.adapters.TasksAdapter;
import com.mich.todolist.database.DeleteTaskAsyncTask;
import com.mich.todolist.database.LoadTasksListAsyncTask;
import com.mich.todolist.models.TaskEntity;
import com.mich.todolist.utilities.AlarmReceiver;
import com.mich.todolist.utilities.AppConstants;
import com.mich.todolist.utilities.IntentExtras;
import com.mich.todolist.utilities.PermissionsChecker;
import com.mich.todolist.utilities.RecyclerItemTouchHelperCallback;
import com.mich.todolist.utilities.SortType;
import com.mich.todolist.utilities.TaskComparator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
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

        startNotificationAlarm();
    }

    private void startNotificationAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction("com.mich.todolist.ALARM");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), AppConstants.FIFTEEN_MINUTES_IN_MILIS, pendingIntent);

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
        } else if (item.getItemId() == R.id.action_export_to_file) {
            showExportTasksToFileDialog();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsChecker.onRequestPermissionResult(requestCode, permissions, grantResults);
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

    private void showExportTasksToFileDialog() {
        final EditText editText = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(R.string.export)
                .setMessage(R.string.enter_file_name)
                .setView(editText)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    exportTasksToFile(editText.getText().toString());
                })
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }

    private void exportTasksToFile(String fileName) {
        PermissionsChecker.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, () -> {
            try {
                File file = new File("mnt/sdcard/" + fileName);
                file.createNewFile();

                PrintWriter printWriter = new PrintWriter(new FileWriter(file));

                for (TaskEntity task : tasks) {
                    printWriter.println("* " + task.getTitle() + " (" + task.getDate() + ")");
                    printWriter.println(task.getDescription());
                }

                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        });
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
