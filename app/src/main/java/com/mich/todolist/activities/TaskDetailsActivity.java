package com.mich.todolist.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mich.todolist.R;
import com.mich.todolist.models.TaskEntity;
import com.mich.todolist.utilities.IntentExtras;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskDetailsActivity extends AppCompatActivity {

    @BindView(R.id.textViewDescription)
    TextView textViewDescription;
    @BindView(R.id.textViewDate)
    TextView textViewDate;
    @BindView(R.id.textViewCategory)
    TextView textViewCategory;
    @BindView(R.id.textViewPriority)
    TextView textViewPriority;

    private TaskEntity task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            task = bundle.getParcelable(IntentExtras.TASK);

            init();
        } else {
            finish();
        }
    }

    private void init() {
        getSupportActionBar().setTitle(task.getTitle());

        textViewDescription.setText(task.getDescription());
        textViewDate.setText(task.getDate());

        String[] categories = getResources().getStringArray(R.array.task_categories);
        String[] priorities = getResources().getStringArray(R.array.task_priorities);

        textViewCategory.setText(categories[task.getCategory()]);
        textViewPriority.setText(priorities[task.getPriority()]);
    }
}
