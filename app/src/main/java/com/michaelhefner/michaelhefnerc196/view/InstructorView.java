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

public class InstructorView extends AppCompatActivity {

    private static final String EMAIL_INPUT = "email";
    private static final String PHONE_INPUT = "phone";
    private static final String NAME_INPUT = "name";
    private DBHandler mDBHandler;
    private EditText mNameEDT;
    private EditText mEmailEDT;
    private EditText mPhoneEDT;
    private Button mAddBTN;
    private Button mDeleteBTN;
    private String mKey;
    private TextView mHeadingTXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insructor_view);

        mDBHandler = new DBHandler(this);
        mEmailEDT = findViewById(R.id.edtEmailAddress);
        mNameEDT = findViewById(R.id.edtPersonName);
        mPhoneEDT = findViewById(R.id.edtPhoneNumber);
        mAddBTN = findViewById(R.id.btnSubmit);
        mHeadingTXT = findViewById(R.id.txtHeading);
        mDeleteBTN = findViewById(R.id.btnDelete);
        mKey = null;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mHeadingTXT.setText(R.string.edit_instructor);
            mKey = extras.getString("id");
            mEmailEDT.setText(extras.getString("email"));
            mNameEDT.setText(extras.getString("name"));
            mPhoneEDT.setText(extras.getString("phone"));
        }
        mAddBTN.setOnClickListener(view -> {
            String email = mEmailEDT.getText().toString();
            String name = mNameEDT.getText().toString();
            String phone = mPhoneEDT.getText().toString();
            hideSoftKeyboard(view);
            HashMap<String, String> newInstructor;

            if (this.mKey == null) {
                newInstructor = mDBHandler.addInstructor(name, email, phone);
            } else {
                newInstructor = mDBHandler.editInstructor(mKey, name, email, phone);
            }
            Snackbar snackbar = Snackbar.make(view, Objects.requireNonNull(newInstructor.get("res")), Snackbar.LENGTH_SHORT);
            snackbar.show();
            startActivity(new Intent(this, MainActivity.class));
        });

        mDeleteBTN.setOnClickListener(view -> {
            HashMap<String, String> response = mDBHandler.deleteInstructor(mKey);
            Snackbar snackbar = Snackbar.make(view, Objects.requireNonNull(response.get("res")), Snackbar.LENGTH_SHORT);
            snackbar.show();
            startActivity(new Intent(this, MainActivity.class));
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(EMAIL_INPUT, mEmailEDT.getText().toString());
        outState.putString(PHONE_INPUT, mPhoneEDT.getText().toString());
        outState.putString(NAME_INPUT, mNameEDT.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        mEmailEDT.setText(savedInstanceState.getString(EMAIL_INPUT));
        mPhoneEDT.setText(savedInstanceState.getString(PHONE_INPUT));
        mNameEDT.setText(savedInstanceState.getString(NAME_INPUT));
        super.onRestoreInstanceState(savedInstanceState);
    }
    public void hideSoftKeyboard(View view) {
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}