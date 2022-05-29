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

public class InstructorVIew extends AppCompatActivity {

    private DBHandler mDBHandler;
    private EditText mNameEDT;
    private EditText mEmailEDT;
    private EditText mPhoneEDT;
    private Button mAddBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_insructor);

        mDBHandler = new DBHandler(this);
        mEmailEDT = findViewById(R.id.edtEmailAddress);
        mNameEDT = findViewById(R.id.edtPersonName);
        mPhoneEDT = findViewById(R.id.edtPhoneNumber);
        mAddBTN = findViewById(R.id.btnSubmit);


        mAddBTN.setOnClickListener(view -> {
            String email = mEmailEDT.getText().toString();
            String name = mNameEDT.getText().toString();
            String phone = mPhoneEDT.getText().toString();
            hideSoftKeyboard(view);
            HashMap<String, String> newInstructor = mDBHandler.addNewInstructor(name, email, phone);
            Snackbar snackbar = Snackbar.make(view, Objects.requireNonNull(newInstructor.get("res")), Snackbar.LENGTH_SHORT);
            snackbar.show();
            startActivity(new Intent(this, MainActivity.class));

        });
    }
    public void hideSoftKeyboard(View view) {
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}