package com.mich.todolist.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.mich.todolist.models.TaskEntity;

/**
 * Created by Michal on 30.11.2017.
 */

@Database(entities = {TaskEntity.class}, version = 1)
public abstract class ApplicationDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "MainDatabase";

    private static ApplicationDatabase instance;

    public abstract TaskDao taskDao();

    public static ApplicationDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context, ApplicationDatabase.class,
                    DATABASE_NAME).build();
        }

        return instance;
    }
}
