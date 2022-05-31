package com.michaelhefner.michaelhefnerc196.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.michaelhefner.michaelhefnerc196.R;
import com.michaelhefner.michaelhefnerc196.controller.DBHandler;
import com.michaelhefner.michaelhefnerc196.model.Course;
import com.michaelhefner.michaelhefnerc196.model.Instructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class InstructorList extends AppCompatActivity {

    private DBHandler mDBHandler;
    private ListView mListView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_list);
        context = this;
        mListView = findViewById(R.id.lvInstructorList);
        mDBHandler = new DBHandler(this);
        List<Instructor> instructorList = mDBHandler.instructorQuery("");
        List<String> stringList = new ArrayList<>();
        if (instructorList.size() > 0) {
            IntStream.range(0, instructorList.size()).forEach(i -> {
                stringList.add(instructorList.get(i).getName());
            });
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringList);
        mListView.setAdapter(itemsAdapter);
        mListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(context, InstructorView.class);
            intent.putExtra("id", instructorList.get(i).getID());
            intent.putExtra("name", instructorList.get(i).getName());
            intent.putExtra("email", instructorList.get(i).getEmailAddress());
            intent.putExtra("phone", instructorList.get(i).getPhoneNumber());
            startActivity(intent);
        });
    }
}