package com.michaelhefner.michaelhefnerc196.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.michaelhefner.michaelhefnerc196.R;
import com.michaelhefner.michaelhefnerc196.controller.DBHandler;
import com.michaelhefner.michaelhefnerc196.model.Assessment;
import com.michaelhefner.michaelhefnerc196.model.Instructor;
import com.michaelhefner.michaelhefnerc196.model.Term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class CourseView extends AppCompatActivity {
    private static final String TITLE_INPUT = "title";
    private static final String STATUS_INPUT = "status";
    private static final String ASSESSMENT_INPUT = "assessment";
    private static final String TERM_INPUT = "term";
    private static final String INSTRUCTOR_INPUT = "instructor";
    private DBHandler mDBHandler = new DBHandler(this);
    private TextView mHeadingTXT;
    private EditText mStatusEDT;
    private EditText mTitleEDT;
    private TextView mDatesTXT;
    private Spinner mAssessmentSPN;
    private Spinner mInstructorSPN;
    private Spinner mTermSPN;
    private Button mAddBTN;
    private Button mDeleteBTN;
    private String mKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);

        mDBHandler = new DBHandler(this);
        mTitleEDT = findViewById(R.id.edtTitle);
        mDatesTXT = findViewById(R.id.txtDates);
        mStatusEDT = findViewById(R.id.edtStatus);
        mAssessmentSPN = findViewById(R.id.spnAssessment);
        mTermSPN = findViewById(R.id.spnTermList);
        mInstructorSPN = findViewById(R.id.spnInstructor);
        mAddBTN = findViewById(R.id.btnSubmit);
        mHeadingTXT = findViewById(R.id.txtHeading);
        mDeleteBTN = findViewById(R.id.btnDelete);
        mKey = null;

        List<Assessment> assessmentList = mDBHandler.assessmentQuery("");
        List<String> assessList = new ArrayList<>();
        IntStream.range(0, assessmentList.size()).forEach(i -> {
            assessList.add(assessmentList.get(i).getName());
        });
        mAssessmentSPN.setAdapter(getStringArrayAdapter(assessList));

        List<Instructor> instructorList = mDBHandler.instructorQuery("");
        List<String> stringList = new ArrayList<>();
        IntStream.range(0, instructorList.size()).forEach(i -> {
            stringList.add(instructorList.get(i).getName());
        });

        mTermSPN = findViewById(R.id.spnTermList);
        List<Term> termList = mDBHandler.termQuery("");
        List<String> terms = new ArrayList<>();
        IntStream.range(0, termList.size()).forEach(i -> {
            terms.add(termList.get(i).getTitle());
        });
        mTermSPN.setAdapter(getStringArrayAdapter(terms));

        mInstructorSPN.setAdapter(getStringArrayAdapter(stringList));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mHeadingTXT.setText("Edit Instructor");
            mKey = extras.getString("courseID");
            mTitleEDT.setText(extras.getString("title"));
            mStatusEDT.setText(extras.getString("status"));
            Term t = mDBHandler.termQuery(
                            "id = \'" + extras.getString("term") + "\'")
                    .get(0);
            mDatesTXT.setText("Course Duration: " + t.getStart() + " to " + t.getEnd());
            mHeadingTXT.setText("Edit Course");
            Log.i("assessment", "type" + mDBHandler.assessmentQuery(
                    "id = '" + extras.getString("assessment") + "'").get(0).getName());
            mAssessmentSPN.setSelection(assessList.indexOf(
                    mDBHandler.assessmentQuery(
                            "id = '" + extras.getString("assessment") + "'").get(0).getName())
            );
            mTermSPN.setSelection(terms.indexOf(mDBHandler.termQuery("id = '" + extras.getString("term") + "'").get(0).getName()));
        }

        mAddBTN.setOnClickListener(view -> {
            String title = mTitleEDT.getText().toString();
            String name = mStatusEDT.getText().toString();
            List<Assessment> assessment = mDBHandler.assessmentQuery(//"");
                    "type = \"" + mAssessmentSPN.getSelectedItem().toString() + "\""
            );

            List<Term> term = mDBHandler.termQuery(
                    "name = \'" + mTermSPN.getSelectedItem().toString() + "\'"
            );
            List<Instructor> instructor = mDBHandler.instructorQuery(
                    "name = \'" + mInstructorSPN.getSelectedItem().toString() + "\'"
            );
            hideSoftKeyboard(view);
            HashMap<String, String> newCourse;
            if (this.mKey == null) {
                newCourse =
                        mDBHandler.addCourse(
                                title,
                                term.get(0).getStart(),
                                term.get(0).getEnd(),
                                name,
                                assessment.get(0).getID(),
                                term.get(0).getID(),
                                instructor.get(0).getID()
                        );
            } else {
                newCourse =
                        mDBHandler.editCourse(
                                this.mKey,
                                title,
                                term.get(0).getStart(),
                                term.get(0).getEnd(),
                                name,
                                assessment.get(0).getID(),
                                term.get(0).getID(),
                                instructor.get(0).getID()
                        );
            }
            Snackbar snackbar = Snackbar.make(
                    view, Objects.requireNonNull(newCourse.get("res")), Snackbar.LENGTH_SHORT
            );
            snackbar.show();
            startActivity(new Intent(this, MainActivity.class));
        });

        mDeleteBTN.setOnClickListener(view -> {
            HashMap<String, String> response = mDBHandler.deleteCourse(mKey);
            Snackbar snackbar = Snackbar.make(view, Objects.requireNonNull(response.get("res")), Snackbar.LENGTH_SHORT);
            snackbar.show();
            startActivity(new Intent(this, MainActivity.class));
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(TITLE_INPUT, mTitleEDT.getText().toString());
        outState.putString(STATUS_INPUT, mStatusEDT.getText().toString());
        outState.putInt(ASSESSMENT_INPUT, mAssessmentSPN.getSelectedItemPosition());
        outState.putInt(TERM_INPUT, mTermSPN.getSelectedItemPosition());
        outState.putInt(INSTRUCTOR_INPUT, mInstructorSPN.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        mTitleEDT.setText(savedInstanceState.getString(TITLE_INPUT));
        mStatusEDT.setText(savedInstanceState.getString(STATUS_INPUT));
        mAssessmentSPN.setSelection(savedInstanceState.getInt(ASSESSMENT_INPUT));
        mTermSPN.setSelection(savedInstanceState.getInt(TERM_INPUT));
        mInstructorSPN.setSelection(savedInstanceState.getInt(INSTRUCTOR_INPUT));
        super.onRestoreInstanceState(savedInstanceState);
    }
    @NonNull
    private ArrayAdapter<String> getStringArrayAdapter(List<String> stringList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, stringList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}