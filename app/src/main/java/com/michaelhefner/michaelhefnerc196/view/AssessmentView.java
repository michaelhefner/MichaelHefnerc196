package com.michaelhefner.michaelhefnerc196.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.michaelhefner.michaelhefnerc196.R;
import com.michaelhefner.michaelhefnerc196.controller.DBHandler;

import java.util.HashMap;
import java.util.Objects;

public class AssessmentView extends AppCompatActivity {

    private static final String INPUT_TYPE = "INPUT_TYPE";
    private static final String INPUT_NAME = "INPUT_NAME";
    private DBHandler mDBHandler;
    private EditText mNameEDT;
    private EditText mTypeEDT;
    private EditText mStartDateEDT;
    private EditText mEndDateEDT;
    private Button mAddBTN;
    private Button mDeleteBTN;
    private String mKey;
    private TextView mHeadingTXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_view);

        mDBHandler = new DBHandler(this);
        mStartDateEDT = findViewById(R.id.edtStartDate);
        mEndDateEDT = findViewById(R.id.edtEndDate);
        mTypeEDT = findViewById(R.id.edtType);
        mNameEDT = findViewById(R.id.edtAssessmentName);
        mAddBTN = findViewById(R.id.btnSubmit);
        mHeadingTXT = findViewById(R.id.txtHeading);
        mDeleteBTN = findViewById(R.id.btnDelete);
        mKey = null;


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mHeadingTXT.setText("Edit Assessment");
            mKey = extras.getString("id");
            mTypeEDT.setText(extras.getString("type"));
            mNameEDT.setText(extras.getString("name"));
            mEndDateEDT.setText(extras.getString("endDate"));
            mStartDateEDT.setText(extras.getString("startDate"));
        }
        mAddBTN.setOnClickListener(view -> {
            String type = mTypeEDT.getText().toString();
            String name = mNameEDT.getText().toString();
            String startDate = mStartDateEDT.getText().toString();
            String endDate = mEndDateEDT.getText().toString();
            hideSoftKeyboard(view);
            HashMap<String, String> newAssessment;

            if (this.mKey == null) {
                newAssessment = mDBHandler.addAssessment(name, type, startDate, endDate);
            } else {
                newAssessment = mDBHandler.editAssessment(mKey, name, type, startDate, endDate);
            }
            Snackbar snackbar = Snackbar.make(view, Objects.requireNonNull(newAssessment.get("res")), Snackbar.LENGTH_SHORT);
            snackbar.show();
            startActivity(new Intent(this, MainActivity.class));
        });
        mDeleteBTN.setOnClickListener(view -> {
            HashMap<String, String> response = mDBHandler.deleteAssessment(mKey);
            Snackbar snackbar = Snackbar.make(view, Objects.requireNonNull(response.get("res")), Snackbar.LENGTH_SHORT);
            snackbar.show();
            startActivity(new Intent(this, MainActivity.class));
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(INPUT_TYPE, mTypeEDT.getText().toString());
        outState.putString(INPUT_NAME, mNameEDT.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        mTypeEDT.setText(savedInstanceState.getString(INPUT_TYPE));
        mNameEDT.setText(savedInstanceState.getString(INPUT_NAME));
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}