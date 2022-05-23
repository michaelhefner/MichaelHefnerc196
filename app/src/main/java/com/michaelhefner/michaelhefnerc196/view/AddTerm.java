package com.michaelhefner.michaelhefnerc196.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.michaelhefner.michaelhefnerc196.R;
import com.michaelhefner.michaelhefnerc196.controller.DBHandler;

import java.util.HashMap;
import java.util.Objects;

public class AddTerm extends AppCompatActivity {

    private DBHandler mDBHandler;
    private EditText mNameEDT;
    private EditText mTitleEDT;
    private EditText mStartDateEDT;
    private EditText mEndDateEDT;
    private Button mAddBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        mDBHandler = new DBHandler(this);
        mTitleEDT = findViewById(R.id.edtTitle);
        mStartDateEDT = findViewById(R.id.edtStartDate);
        mEndDateEDT = findViewById(R.id.edtEndDate);
        mNameEDT = findViewById(R.id.edtName);
        mAddBTN = findViewById(R.id.btnSubmit);

        mAddBTN.setOnClickListener(view -> {
            String title = mTitleEDT.getText().toString();
            String startDate = mStartDateEDT.getText().toString();
            String endDate = mEndDateEDT.getText().toString();
            String name = mNameEDT.getText().toString();
            hideSoftKeyboard(view);
            HashMap<String, String> newTerm = mDBHandler.addNewTerm(title, startDate, endDate, name);
            Snackbar snackbar = Snackbar.make(view, Objects.requireNonNull(newTerm.get("res")), Snackbar.LENGTH_SHORT);
            snackbar.show();
            startActivity(new Intent(this, MainActivity.class));

        });
    }
    public void hideSoftKeyboard(View view) {
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}