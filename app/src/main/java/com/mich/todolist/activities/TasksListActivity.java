package com.mich.todolist.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mich.todolist.R;
import com.mich.todolist.adapters.TasksAdapter;
import com.mich.todolist.models.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TasksListActivity extends AppCompatActivity {

    @BindView(R.id.recycerView_tasks) RecyclerView recyclerViewTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);

        ButterKnife.bind(this);

        initRecycler();
    }

    private void initRecycler() {
        TasksAdapter adapter = new TasksAdapter(getTasks());
        recyclerViewTasks.setAdapter(adapter);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTasks.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();

        for (int i = 0; i < 50; ++i) {
            tasks.add(new Task(i, "Task " + i, "", Calendar.getInstance(), 0,0));
        }

        return tasks;
    }

    @OnClick(R.id.floatingActionButton_addTask)
    void onFloatingActionButtonAddTaskClick() {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }
}
