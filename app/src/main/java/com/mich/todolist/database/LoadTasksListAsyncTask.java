package com.mich.todolist.database;

import android.content.Context;
import android.os.AsyncTask;

import com.mich.todolist.models.TaskEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal on 04.01.2018.
 */

public class LoadTasksListAsyncTask extends AsyncTask<Void, Void, List<TaskEntity>> {

    public interface LoadTasksListObserver {
        void onTasksLoaded(List<TaskEntity> tasks);
    }

    public List<LoadTasksListObserver> observers = new ArrayList<>();

    private static LoadTasksListAsyncTask instance;

    private Context context;

    private LoadTasksListAsyncTask() {

    }

    public static LoadTasksListAsyncTask getInstance(Context context) {
        if (instance == null) {
            instance = new LoadTasksListAsyncTask();
        }

        instance.context = context;
        return instance;
    }

    public void addObserver(LoadTasksListObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(LoadTasksListObserver observer) {
        observers.remove(observer);
    }

    @Override
    protected List<TaskEntity> doInBackground(Void... voids) {
        ApplicationDatabase database = ApplicationDatabase.getInstance(context);
        TaskDao dao = database.taskDao();

        List<TaskEntity> tasks = dao.getAllTasks();

        return tasks;
    }

    @Override
    protected void onPostExecute(List<TaskEntity> taskEntities) {
        for (LoadTasksListObserver observer : observers) {
            observer.onTasksLoaded(taskEntities);
        }

        instance = new LoadTasksListAsyncTask();
        instance.observers = observers;
    }
}
