package com.michaelhefner.michaelhefnerc196.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.michaelhefner.michaelhefnerc196.R;
import com.michaelhefner.michaelhefnerc196.controller.DBHandler;
import com.michaelhefner.michaelhefnerc196.model.Assessment;
import com.michaelhefner.michaelhefnerc196.model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class CourseList extends AppCompatActivity {

    private DBHandler mDBHandler;
    private ListView mListView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        context = this;
        mListView = findViewById(R.id.lvCourseList);
        mDBHandler = new DBHandler(this);
        List<Course> courseList = mDBHandler.courseQuery("");
        List<String> stringList = new ArrayList<>();
        if (courseList.size() > 0) {
            IntStream.range(0, courseList.size()).forEach(i -> {
                stringList.add(courseList.get(i).getTitle());
            });
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringList);
        mListView.setAdapter(itemsAdapter);

        mListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(context, CourseView.class);
            intent.putExtra("courseID", courseList.get(i).getID());
            intent.putExtra("startDate", courseList.get(i).getStartDate());
            intent.putExtra("endDate", courseList.get(i).getEndDate());
            intent.putExtra("status", courseList.get(i).getStatus());
            intent.putExtra("assessment",courseList.get(i).getAssessment());
            intent.putExtra("term", courseList.get(i).getTerm());
            intent.putExtra("title", courseList.get(i).getTitle());
            intent.putExtra("notes", courseList.get(i).getNotes());
            startActivity(intent);
        });
    }
}