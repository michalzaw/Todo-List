package com.mich.todolist.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.mich.todolist.models.TaskEntity;

/**
 * Created by Michal on 30.11.2017.
 */

@Database(entities = {TaskEntity.class}, version = 2)
public abstract class ApplicationDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "MainDatabase";

    private static ApplicationDatabase instance;

    public abstract TaskDao taskDao();

    public static ApplicationDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room
                    .databaseBuilder(context, ApplicationDatabase.class, DATABASE_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }

        return instance;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE task ADD COLUMN done INTEGER NOT NULL DEFAULT 0");
        }
    };
}
