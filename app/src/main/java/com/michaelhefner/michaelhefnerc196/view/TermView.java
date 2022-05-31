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

public class TermView extends AppCompatActivity {

    private static final String TITLE_INPUT = "title";
    private static final String NAME_INPUT = "name";
    private static final String START_INPUT = "start";
    private static final String END_INPUT = "end";
    private DBHandler mDBHandler;
    private EditText mNameEDT;
    private EditText mTitleEDT;
    private EditText mStartDateEDT;
    private EditText mEndDateEDT;
    private Button mAddBTN;
    private Button mDeleteBTN;
    private String mKey;
    private TextView mHeadingTXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_view);

        mDBHandler = new DBHandler(this);
        mTitleEDT = findViewById(R.id.edtTitle);
        mStartDateEDT = findViewById(R.id.edtStartDate);
        mEndDateEDT = findViewById(R.id.edtEndDate);
        mNameEDT = findViewById(R.id.edtName);
        mAddBTN = findViewById(R.id.btnSubmit);
        mDeleteBTN = findViewById(R.id.btnDelete);
        mHeadingTXT = findViewById(R.id.txtHeading);
        mKey = null;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mHeadingTXT.setText("Edit Term");
            mKey = extras.getString("id");
            mTitleEDT.setText(extras.getString("title"));
            mNameEDT.setText(extras.getString("name"));
            mStartDateEDT.setText(extras.getString("start"));
            mEndDateEDT.setText(extras.getString("end"));
        }

        mAddBTN.setOnClickListener(view -> {
            String title = mTitleEDT.getText().toString();
            String startDate = mStartDateEDT.getText().toString();
            String endDate = mEndDateEDT.getText().toString();
            String name = mNameEDT.getText().toString();
            hideSoftKeyboard(view);
            HashMap<String, String> newTerm;

            if (this.mKey == null) {
                newTerm = mDBHandler.addTerm(title, startDate, endDate, name);
            } else {
                newTerm = mDBHandler.editTerm(mKey, title, startDate, endDate, name);
            }
            Snackbar snackbar = Snackbar.make(view, Objects.requireNonNull(newTerm.get("res")), Snackbar.LENGTH_SHORT);
            snackbar.show();
            startActivity(new Intent(this, MainActivity.class));
        });

        mDeleteBTN.setOnClickListener(view -> {
            HashMap<String, String> response = mDBHandler.deleteTerm(mKey);
            Snackbar snackbar = Snackbar.make(view, Objects.requireNonNull(response.get("res")), Snackbar.LENGTH_SHORT);
            snackbar.show();
            startActivity(new Intent(this, MainActivity.class));
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(TITLE_INPUT, mTitleEDT.getText().toString());
        outState.putString(NAME_INPUT, mNameEDT.getText().toString());
        outState.putString(START_INPUT, mStartDateEDT.getText().toString());
        outState.putString(END_INPUT, mEndDateEDT.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        mTitleEDT.setText(savedInstanceState.getString(TITLE_INPUT));
        mNameEDT.setText(savedInstanceState.getString(NAME_INPUT));
        mStartDateEDT.setText(savedInstanceState.getString(START_INPUT));
        mEndDateEDT.setText(savedInstanceState.getString(END_INPUT));
        super.onRestoreInstanceState(savedInstanceState);
    }
    public void hideSoftKeyboard(View view) {
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}