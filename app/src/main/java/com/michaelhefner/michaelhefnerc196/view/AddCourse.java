package com.michaelhefner.michaelhefnerc196.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.michaelhefner.michaelhefnerc196.R;
import com.michaelhefner.michaelhefnerc196.controller.DBHandler;
import com.michaelhefner.michaelhefnerc196.model.Assessment;
import com.michaelhefner.michaelhefnerc196.model.Course;
import com.michaelhefner.michaelhefnerc196.model.Instructor;
import com.michaelhefner.michaelhefnerc196.model.Term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class AddCourse extends AppCompatActivity {
    private DBHandler mDBHandler = new DBHandler(this);

    private EditText mStatusEDT;
    private EditText mTitleEDT;
    private EditText mStartDateEDT;
    private EditText mEndDateEDT;
    private Spinner mAssessmentSPN;
    private Spinner mInstructorSPN;
    private Spinner mTermSPN;
    private Button mAddBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        mDBHandler = new DBHandler(this);
        mTitleEDT = findViewById(R.id.edtTitle);
        mStartDateEDT = findViewById(R.id.edtStartDate);
        mEndDateEDT = findViewById(R.id.edtEndDate);
        mStatusEDT = findViewById(R.id.edtStatus);
        mAssessmentSPN = findViewById(R.id.spnAssessment);
        mTermSPN = findViewById(R.id.spnTermList);
        mInstructorSPN = findViewById(R.id.spnInstructor);
        mAddBTN = findViewById(R.id.btnSubmit);

        List<Assessment> assessmentList =  mDBHandler.assessmentQuery(null);
        List<String> assessList = new ArrayList<>();

        IntStream.range(0, assessmentList.size()).forEach(i -> {
//            String assessment = mAssessmentSPN.getSelectedItem().toString();
//            String term = mTermSPN.getSelectedItem().toString();
            Log.i("list", assessmentList.get(i).getName());
            assessList.add(assessmentList.get(i).getName());
        });

        mAssessmentSPN.setAdapter(getStringArrayAdapter(assessList));

        List<Instructor> instructorList =  mDBHandler.instructorQuery();
        List<String> stringList = new ArrayList<>();

        IntStream.range(0, instructorList.size()).forEach(i -> {
            Log.i("list", instructorList.get(i).getName());
            stringList.add(instructorList.get(i).getName());
        });
        populateTerms();
        mInstructorSPN.setAdapter(getStringArrayAdapter(stringList));

        mAddBTN.setOnClickListener(view -> {
            Log.i("click", mAssessmentSPN.getSelectedItem().toString());

            String title = mTitleEDT.getText().toString();
            String startDate = mStartDateEDT.getText().toString();
            String endDate = mEndDateEDT.getText().toString();
            String name = mStatusEDT.getText().toString();
            List<Assessment> assessment = mDBHandler.assessmentQuery(mAssessmentSPN.getSelectedItem().toString());
            List<Term> term = mDBHandler.termQuery(mTermSPN.getSelectedItem().toString());
//            String term = mTermSPN.getSelectedItem().toString();
            String instructor = mInstructorSPN.getSelectedItem().toString();
            Log.i("click", assessment.get(0).getID());

            hideSoftKeyboard(view);
            HashMap<String, String> newCourse = mDBHandler.addNewCourse(title, startDate, endDate, name, assessment.get(0).getID(),term.get(0).getID());
            Snackbar snackbar = Snackbar.make(view, Objects.requireNonNull(newCourse.get("res")), Snackbar.LENGTH_SHORT);
            snackbar.show();
            startActivity(new Intent(this, MainActivity.class));

        });
    }

    private void populateTerms() {
        mTermSPN = findViewById(R.id.spnTermList);
        List<Term> termList =  mDBHandler.termQuery(null);
        List<String> terms = new ArrayList<>();
        IntStream.range(0, termList.size()).forEach(i -> {
            terms.add(termList.get(i).getTitle());
        });
        mTermSPN.setAdapter(getStringArrayAdapter(terms));
    }

    @NonNull
    private ArrayAdapter<String> getStringArrayAdapter(List<String> stringList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, stringList);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}