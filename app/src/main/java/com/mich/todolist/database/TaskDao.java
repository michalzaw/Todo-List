package com.mich.todolist.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.mich.todolist.models.TaskEntity;

import java.util.List;

/**
 * Created by Michal on 30.11.2017.
 */

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    List<TaskEntity> getAllTasks();

    @Insert
    void insert(TaskEntity task);

    @Delete
    void delete(TaskEntity taskEntity);
}
