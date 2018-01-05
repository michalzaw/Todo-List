package com.mich.todolist.database;

import android.content.Context;
import android.os.AsyncTask;

import com.mich.todolist.models.TaskEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal on 05.01.2018.
 */

public class DeleteTaskAsyncTask extends AsyncTask<TaskEntity, Void, TaskEntity> {

    public interface DeleteTaskObserver {
        void onDeletedTask(TaskEntity taskEntity);
    }

    public List<DeleteTaskObserver> observers = new ArrayList<>();

    private static DeleteTaskAsyncTask instance;

    private Context context;

    private DeleteTaskAsyncTask() {

    }

    public static DeleteTaskAsyncTask getInstance(Context context) {
        if (instance == null) {
            instance = new DeleteTaskAsyncTask();
        }

        instance.context = context;
        return instance;
    }

    public void addObserver(DeleteTaskObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(DeleteTaskObserver observer) {
        observers.remove(observer);
    }

    @Override
    protected TaskEntity doInBackground(TaskEntity... taskEntities) {
        ApplicationDatabase database = ApplicationDatabase.getInstance(context);
        TaskDao dao = database.taskDao();

        if (taskEntities.length == 1) {
            dao.delete(taskEntities[0]);

            return taskEntities[0];
        }

        return null;
    }

    @Override
    protected void onPostExecute(TaskEntity task) {
        super.onPostExecute(task);

        for (DeleteTaskObserver observer : observers) {
            observer.onDeletedTask(task);
        }

        instance = new DeleteTaskAsyncTask();
        instance.observers = observers;
    }
}
