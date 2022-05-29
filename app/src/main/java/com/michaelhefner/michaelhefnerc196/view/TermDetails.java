package com.michaelhefner.michaelhefnerc196.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.michaelhefner.michaelhefnerc196.R;
import com.michaelhefner.michaelhefnerc196.controller.DBHandler;
import com.michaelhefner.michaelhefnerc196.databinding.ActivityTermDetailsBinding;
import com.michaelhefner.michaelhefnerc196.model.Course;
import com.michaelhefner.michaelhefnerc196.model.Term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class TermDetails extends AppCompatActivity {

    private DBHandler mDBHandler = new DBHandler(this);
    private Spinner mTermList;
    private Context context;
    private Spinner mActionSpinner;
    private ListView mLSTCourseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_term_details);

        Map<String, Class> classes = new HashMap<>();
        classes.put("Add Instructor", InstructorVIew.class);
        classes.put("Add Assessment", AssessmentView.class);
        classes.put("Add Course", CourseView.class);
        classes.put("Add Term", TermView.class);
        mActionSpinner = findViewById(R.id.actionPicker);
        /**/
        populateCourses("");
        /**/
        populateTerms("");
        /**/

        /**/
        context = this;
        mTermList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<Term> termList = mDBHandler.termQuery("name = \"" + mTermList.getSelectedItem().toString() + "\"");
                populateCourses("termID = \"" + termList.get(0).getID() + "\"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mActionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("spinner change", mActionSpinner.getSelectedItem().toString());
                String selectedItem = mActionSpinner.getSelectedItem().toString();
                if (!selectedItem.isEmpty() && classes.get(selectedItem) != null) {
                    Intent intent = new Intent(context, classes.get(mActionSpinner.getSelectedItem().toString()));
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_term_details, menu);
        return true;
    }
    private void populateCourses(String whereClause) {
        mLSTCourseList = findViewById(R.id.lstCourseList);
        List<Course> courseList = mDBHandler.courseQuery(whereClause);
        List<String> stringList = new ArrayList<>();
        if (courseList.size() > 0) {
            IntStream.range(0, courseList.size()).forEach(i -> {
                stringList.add(courseList.get(i).getTitle());
            });
        }
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringList);
        mLSTCourseList.setAdapter(itemsAdapter);
        mLSTCourseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("term query", mDBHandler.termQuery("id = \"" + courseList.get(i).getTerm() + "\"").get(0).getName());
                Intent intent = new Intent(context, CourseView.class);

                intent.putExtra("courseID", courseList.get(i).getID());
                intent.putExtra("startDate", courseList.get(i).getStartDate());
                intent.putExtra("endDate", courseList.get(i).getEndDate());
                intent.putExtra("status", courseList.get(i).getStatus());
//                intent.putExtra("instructor", mDBHandler.assessmentQuery("id = \"" + courseList.get(i).get() + "\"").get(0).getName());
                intent.putExtra("assessment", mDBHandler.assessmentQuery("id = \"" + courseList.get(i).getAssessment() + "\"").get(0).getName());
                intent.putExtra("term", mDBHandler.termQuery("id = \"" + courseList.get(i).getTerm() + "\"").get(0).getName());
                intent.putExtra("title", courseList.get(i).getTitle());

                startActivity(intent);
            }
        });


        mLSTCourseList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("vv", mLSTCourseList.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void populateTerms(String whereClause) {
        mTermList = findViewById(R.id.spnTermList);
        List<Term> termList = mDBHandler.termQuery(whereClause);
        List<String> terms = new ArrayList<>();
        IntStream.range(0, termList.size()).forEach(i -> {
            terms.add(termList.get(i).getTitle());
        });
        mTermList.setAdapter(getStringArrayAdapter(terms));
    }

    @NonNull
    private ArrayAdapter<String> getStringArrayAdapter(List<String> stringList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, stringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}