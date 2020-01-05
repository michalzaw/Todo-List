package com.mich.todolist.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.mich.todolist.R;
import com.mich.todolist.database.TaskRepository;
import com.mich.todolist.models.TaskEntity;
import com.mich.todolist.utilities.CalendarConverter;
import com.mich.todolist.utilities.IntentExtras;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTaskActivity extends AppCompatActivity {

    @BindView(R.id.editText_name)
    EditText editTextName;
    @BindView(R.id.editText_description)
    EditText editTextDescription;
    @BindView(R.id.editText_date)
    EditText editTextDate;
    @BindView(R.id.editText_hour)
    EditText editTextHour;
    @BindView(R.id.spinner_category)
    Spinner spinnerCategory;
    @BindView(R.id.spinner_priority)
    Spinner spinnerPriority;

    private Calendar taskDate = Calendar.getInstance();

    private TaskEntity task;

    TaskRepository taskRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        ButterKnife.bind(this);

        initSpinners();

        taskRepository = new TaskRepository(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            task = bundle.getParcelable(IntentExtras.TASK);

            taskDate = CalendarConverter.stringToCalendar(task.getDate(), CalendarConverter.DATE_AND_TIME_FORMAT);

            editTextName.setText(task.getTitle());
            editTextDescription.setText(task.getDescription());
            editTextDate.setText(CalendarConverter.calendarToString(taskDate, CalendarConverter.SIMPLE_DATE_FORMAT));
            editTextHour.setText(CalendarConverter.calendarToString(taskDate, CalendarConverter.SIMPLE_TIME_FORMAT));
            spinnerCategory.setSelection(task.getCategory());
            spinnerPriority.setSelection(task.getPriority());
        } else {

        }
    }

    private void initSpinners() {
        ArrayAdapter<CharSequence> adapterCategories = ArrayAdapter.createFromResource(this,
                R.array.task_categories, R.layout.spinner_item);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);

        ArrayAdapter<CharSequence> adapterPriorities = ArrayAdapter.createFromResource(this,
                R.array.task_priorities, R.layout.spinner_item);
        adapterPriorities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapterPriorities);
    }

    private void saveTask() {
        String title = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String date = CalendarConverter.calendarToString(taskDate, CalendarConverter.DATE_AND_TIME_FORMAT);
        int category = spinnerCategory.getSelectedItemPosition();
        int priority = spinnerPriority.getSelectedItemPosition();

        if (task == null) {
            task = new TaskEntity(title, description, date, priority, category, false);

            taskRepository.addTask(task, () -> {
                finish();
                return null;
            });
        } else {
            task.setTitle(title);
            task.setDescription(description);
            task.setDate(date);
            task.setCategory(category);
            task.setPriority(priority);

            taskRepository.updateTask(task);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_task, menu);

        if (task == null) {
            menu.findItem(R.id.action_complete_task).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save_task) {
            saveTask();
        } else if (item.getItemId() == R.id.action_complete_task) {
            task.setDone(true);
            saveTask();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @OnClick(R.id.editText_date)
    void onEditTextDateClick() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (datePicker, year, month, dayOfMonth) -> {
            taskDate.set(year, month, dayOfMonth);

            editTextDate.setText(CalendarConverter.calendarToString(taskDate, CalendarConverter.SIMPLE_DATE_FORMAT));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    @OnClick(R.id.editText_hour)
    void onEditTextHourClick() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, (timePicker, hourOfDay, minute) -> {
            taskDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            taskDate.set(Calendar.MINUTE, minute);

            editTextHour.setText(CalendarConverter.calendarToString(taskDate, CalendarConverter.SIMPLE_TIME_FORMAT));
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this))
                .show();
    }
}
