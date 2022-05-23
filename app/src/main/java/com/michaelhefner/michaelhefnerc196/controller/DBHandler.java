package com.michaelhefner.michaelhefnerc196.controller;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.michaelhefner.michaelhefnerc196.model.Assessment;
import com.michaelhefner.michaelhefnerc196.model.Course;
import com.michaelhefner.michaelhefnerc196.model.Instructor;
import com.michaelhefner.michaelhefnerc196.model.Term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "CourseDB";
    private static final int DB_VERSION = 12;
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String TITLE = "title";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String STATUS = "status";
    private static final String EMAIL_ADDRESS = "emailAddress";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String TYPE = "type";
    private static final String TERM_FK = "termID";
    private static final String COURSE_FK = "courseID";
    private static final String ASSESSMENT_FK = "assessmentID";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE courses" + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " TEXT,"
                + START_DATE + " TEXT,"
                + END_DATE + " TEXT,"
                + STATUS + " TEXT,"
                + ASSESSMENT_FK + " INTEGER,"
                + TERM_FK + " INTEGER,"
                + " FOREIGN KEY ("+ASSESSMENT_FK+") REFERENCES assessment_types(id),"
                + " FOREIGN KEY ("+TERM_FK+") REFERENCES terms(id));");
        db.execSQL("CREATE TABLE instructors" + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + EMAIL_ADDRESS + " TEXT,"
                + PHONE_NUMBER + " TEXT)");
        db.execSQL("CREATE TABLE assessment_types" + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + TYPE + " TEXT)");
        db.execSQL("CREATE TABLE terms" + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + TITLE + " TEXT,"
                + START_DATE + " TEXT,"
                + END_DATE + " TEXT)");
    }

    public HashMap<String, String> addNewCourse(String courseTitle, String startDate, String endDate, String status, String assessment, String term) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE, courseTitle);
        values.put(START_DATE, startDate);
        values.put(END_DATE, endDate);
        values.put(STATUS, status);
        values.put(TERM_FK, term);
        values.put(ASSESSMENT_FK, assessment);
        db.insert("courses", null, values);
        Log.i("test", "add new course");
        db.close();

        HashMap<String, String> result = new HashMap<>();
        result.put("res", "Course Added");
        return result;
    }

    public List<Term> termQuery(String whereString) {//String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = whereString == null ||
                whereString.isEmpty() ? null : "name = \"" + whereString + "\"";
        Cursor cursor = db.query("terms", null, where, null, null, null, null);
        List<Term> termList = new ArrayList<>();

        if (cursor != null) {
            try {
                try {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        String title = getValue(cursor, TITLE);
                        String startDate = getValue(cursor, START_DATE);
                        String endDate = getValue(cursor, END_DATE);
                        String name = getValue(cursor, NAME_COL);
                        Term term = new Term(title, startDate, endDate, name);
                        termList.add(term);
                        cursor.moveToNext();
                    }
                } finally {
                    cursor.close();
                }
            } catch (Exception e) {
                Log.e("error", e.toString());
            }
        }
        return termList;
    }

    public List<Course> courseQuery() {//String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("courses", null, null, null, null, null, null);
        List<Course> courseList = new ArrayList<>();
        try {
            try {
                Log.i("cursor count", String.valueOf(cursor.getCount()));
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String title = getValue(cursor, TITLE);
                    String startDate = getValue(cursor, START_DATE);
                    String endDate = getValue(cursor, END_DATE);
                    String status = getValue(cursor, STATUS);
                    String term = getValue(cursor, TERM_FK);
                    String assessment = getValue(cursor, ASSESSMENT_FK);
                    Log.i("cursor count", title);
                    Course course = new Course(title, startDate, endDate, status, assessment, term);
                    courseList.add(course);
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
        return courseList;
    }

    public List<Assessment> assessmentQuery(String whereString) {//String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = whereString == null ||
                whereString.isEmpty() ? null : "name = \"" + whereString + "\"";
        Cursor cursor = db.query("assessment_types", null, where, null, null, null, null);
        List<Assessment> assessmentList = new ArrayList<>();
        try {
            try {
                Log.i("cursor count", String.valueOf(cursor.getCount()));
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String name = getValue(cursor, NAME_COL);
                    String type = getValue(cursor, TYPE);
                    String id = getValue(cursor, ID_COL);
                    Assessment assessment = new Assessment(name, type);
                    assessment.setID(id);
                    assessmentList.add(assessment);
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
        return assessmentList;
    }
    public List<Instructor> instructorQuery() {//String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("instructors", null, null, null, null, null, null);
        List<Instructor> instructorList = new ArrayList<>();
        try {
            try {
                Log.i("cursor count", String.valueOf(cursor.getCount()));
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String name = getValue(cursor, NAME_COL);
                    String phoneNum = getValue(cursor, PHONE_NUMBER);
                    String email = getValue(cursor, EMAIL_ADDRESS);

                    Log.i("cursor count", name);
                    Instructor instructor = new Instructor(name,phoneNum, email);
                    instructorList.add(instructor);
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
        return instructorList;
    }
    private String getValue(Cursor cursor, String string){
        @SuppressLint("Range") String str = cursor.getString(cursor.getColumnIndex(string));
        return str;
    }

    public HashMap<String, String> addNewTerm(String courseTitle, String startDate, String endDate, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TITLE, courseTitle);
        values.put(START_DATE, startDate);
        values.put(END_DATE, endDate);
        values.put(NAME_COL, name);
        db.insert("terms", null, values);
        Log.i("test", "add new term");

        HashMap<String, String> result = new HashMap<>();
        result.put("res", "Term Added");
        db.close();

        return result;
    }

    public HashMap<String, String> addNewInstructor(String instructorName, String emailAddress, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME_COL, instructorName);
        values.put(EMAIL_ADDRESS, emailAddress);
        values.put(PHONE_NUMBER, phoneNumber);
        db.insert("instructors", null, values);
        db.close();

        HashMap<String, String> result = new HashMap<>();
        result.put("res", "Instructor Added");
        return result;
    }

    public HashMap<String, String> addNewAssessment(String name, String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME_COL, name);
        values.put(TYPE, type);
        db.insert("assessment_types", null, values);
        db.close();

        HashMap<String, String> result = new HashMap<>();
        result.put("res", "Assessment Added");
        return result;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS courses");
        db.execSQL("DROP TABLE IF EXISTS instructors");
        db.execSQL("DROP TABLE IF EXISTS assessment_types");
        db.execSQL("DROP TABLE IF EXISTS terms");
        db.execSQL("DROP TABLE IF EXISTS courses");
        onCreate(db);
    }
}
