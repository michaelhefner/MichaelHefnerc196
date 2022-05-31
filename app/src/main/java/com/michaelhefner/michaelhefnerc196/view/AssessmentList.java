package com.michaelhefner.michaelhefnerc196.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

public class AssessmentList extends AppCompatActivity {
    private DBHandler mDBHandler;
    private ListView mListView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        context = this;
        mListView = findViewById(R.id.lvAssessmentList);
        mDBHandler = new DBHandler(this);

        List<Assessment> assessmentList = mDBHandler.assessmentQuery("");
        List<String> stringList = new ArrayList<>();
        if (assessmentList.size() > 0) {
            IntStream.range(0, assessmentList.size()).forEach(i -> {
                stringList.add(assessmentList.get(i).getName());
            });
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringList);
        mListView.setAdapter(itemsAdapter);
        mListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(context, AssessmentView.class);
            intent.putExtra("id", assessmentList.get(i).getID());
            intent.putExtra("startDate", assessmentList.get(i).getStart());
            intent.putExtra("endDate", assessmentList.get(i).getEnd());
            intent.putExtra("name", assessmentList.get(i).getName());
            intent.putExtra("type", assessmentList.get(i).getType());
            startActivity(intent);
        });
    }
}