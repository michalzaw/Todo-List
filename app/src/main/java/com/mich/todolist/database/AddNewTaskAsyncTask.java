package com.mich.todolist.database;

import android.content.Context;
import android.os.AsyncTask;

import com.mich.todolist.models.TaskEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal on 04.01.2018.
 */

public class AddNewTaskAsyncTask extends AsyncTask<TaskEntity, Void, TaskEntity> {

    public interface AddNewTaskObserver {
        void onAddedNewTask(TaskEntity task);
    }

    public List<AddNewTaskObserver> observers = new ArrayList<>();

    private static AddNewTaskAsyncTask instance;

    private Context context;

    private AddNewTaskAsyncTask() {

    }

    public static AddNewTaskAsyncTask getInstance(Context context) {
        if (instance == null) {
            instance = new AddNewTaskAsyncTask();
        }

        instance.context = context;
        return instance;
    }

    public void addObserver(AddNewTaskObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(AddNewTaskObserver observer) {
        observers.remove(observer);
    }

    @Override
    protected TaskEntity doInBackground(TaskEntity... taskEntities) {
        ApplicationDatabase database = ApplicationDatabase.getInstance(context);
        TaskDao dao = database.taskDao();

        if (taskEntities.length == 1) {
            dao.insert(taskEntities[0]);
            database.close();

            return taskEntities[0];
        }

        return null;
    }

    @Override
    protected void onPostExecute(TaskEntity taskEntity) {
        super.onPostExecute(taskEntity);

        for (AddNewTaskObserver observer : observers) {
            observer.onAddedNewTask(taskEntity);
        }
    }
}
