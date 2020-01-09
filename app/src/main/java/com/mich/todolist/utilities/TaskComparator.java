package com.mich.todolist.utilities;

import com.mich.todolist.models.TaskEntity;

import java.util.Calendar;
import java.util.Comparator;

public class TaskComparator implements Comparator<TaskEntity> {

    private SortType comparisonType;

    public TaskComparator(SortType comparisonType) {
        this.comparisonType = comparisonType;
    }

    @Override
    public int compare(TaskEntity taskEntity1, TaskEntity taskEntity2) {
        switch (comparisonType) {
            case ALPHABETICAL:
                return taskEntity1.getTitle().compareToIgnoreCase(taskEntity2.getTitle());
            case ALPHABETICAL_DESC:
                return taskEntity2.getTitle().compareToIgnoreCase(taskEntity1.getTitle());
            case BY_DATE:
                Calendar date1 = CalendarConverter.stringToCalendar(taskEntity1.getDate(), CalendarConverter.DATE_AND_TIME_FORMAT);
                Calendar date2 = CalendarConverter.stringToCalendar(taskEntity2.getDate(), CalendarConverter.DATE_AND_TIME_FORMAT);
                return (int)Math.signum(date2.getTimeInMillis() - date1.getTimeInMillis());
            case BY_DATE_DESC:
                date1 = CalendarConverter.stringToCalendar(taskEntity1.getDate(), CalendarConverter.DATE_AND_TIME_FORMAT);
                date2 = CalendarConverter.stringToCalendar(taskEntity2.getDate(), CalendarConverter.DATE_AND_TIME_FORMAT);
                return (int)Math.signum(date1.getTimeInMillis() - date2.getTimeInMillis());
            case BY_PRIORITY:
                return taskEntity2.getPriority() - taskEntity1.getPriority();
            case BY_PRIORITY_DESC:
                return taskEntity1.getPriority() - taskEntity2.getPriority();
        }
        return 0;
    }
}
