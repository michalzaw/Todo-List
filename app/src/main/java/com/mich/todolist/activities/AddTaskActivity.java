package com.mich.todolist.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.mich.todolist.R;
import com.mich.todolist.database.TaskRepository;
import com.mich.todolist.models.TaskEntity;
import com.mich.todolist.utilities.CalendarConverter;
import com.mich.todolist.utilities.IntentExtras;
import com.mich.todolist.utilities.PermissionsChecker;

import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTaskActivity extends AppCompatActivity {

    private static final int PICK_PHOTO_REQUEST_CODE = 0;

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
    @BindView(R.id.imageViewAttachment)
    ImageView imageViewAttachment;
    @BindView(R.id.imageButtonRemoveAttachment)
    ImageButton imageButtonRemoveAttachment;

    private Calendar taskDate = Calendar.getInstance();
    private Uri attachementUri;

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

            String attachment = task.getAttachments();
            if (attachment != null) {
                attachementUri = Uri.parse(attachment);
                showAttachment();
            }
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

        String attachments = attachementUri != null ? attachementUri.toString() : null;
        if (task == null) {
            task = new TaskEntity(title, description, date, priority, category, false, attachments);

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
            task.setAttachments(attachments);

            taskRepository.updateTask(task);
            finish();
        }
    }

    private void showAttachment() {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), attachementUri);
            imageViewAttachment.setImageBitmap(bitmap);
            imageViewAttachment.setVisibility(View.VISIBLE);
            imageButtonRemoveAttachment.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addAttachmentClick() {
        PermissionsChecker.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, () -> {
            Intent pickPhotoIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            pickPhotoIntent.setType("image/*");
            pickPhotoIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            startActivityForResult(pickPhotoIntent, PICK_PHOTO_REQUEST_CODE);

            return null;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsChecker.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PHOTO_REQUEST_CODE) {
                if (data != null) {
                    attachementUri = data.getData();
                    getContentResolver().takePersistableUriPermission(attachementUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    showAttachment();
                }
            }
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
        } else if (item.getItemId() == R.id.action_add_attachment) {
            addAttachmentClick();
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

    @OnClick(R.id.imageButtonRemoveAttachment)
    void onButtonRemoveAttachmentClick() {
        imageViewAttachment.setVisibility(View.GONE);
        imageButtonRemoveAttachment.setVisibility(View.GONE);

        attachementUri = null;

        if (task != null) {
            task.setAttachments(null);
        }
    }
}
