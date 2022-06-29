package com.michaelhefner.michaelhefnerc196.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.michaelhefner.michaelhefnerc196.AlarmIntentService;
import com.michaelhefner.michaelhefnerc196.R;
import com.michaelhefner.michaelhefnerc196.controller.DBHandler;
import com.michaelhefner.michaelhefnerc196.model.Assessment;
import com.michaelhefner.michaelhefnerc196.model.Course;
import com.michaelhefner.michaelhefnerc196.model.Term;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    private DBHandler mDBHandler = new DBHandler(this);
    private Spinner mTermList;
    private Context context;

    private ListView mLSTCourseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Map<String, Class> classes = new HashMap<>();
        classes.put("Add Instructor", InstructorView.class);
        classes.put("Add Assessment", AssessmentView.class);
        classes.put("Add Course", CourseView.class);
        classes.put("Add Term", TermView.class);
        populateCourses("");
        populateTerms("");
        context = this;
        mTermList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<Term> termList = mDBHandler.termQuery("name = \'" + mTermList.getSelectedItem().toString() + "\'");
                populateCourses("termID = \'" + termList.get(0).getID() + "\'");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        context = this;
        mDBHandler = new DBHandler(this);

        List<Assessment> assessmentList = mDBHandler.assessmentQuery("");
        List<String> assessDateList = new ArrayList<>();
        if (assessmentList.size() > 0) {
            IntStream.range(0, assessmentList.size()).forEach(i -> {

                String[] str = assessmentList.get(i).getStart().split("/");
                Log.i("date", str[2].concat("-").concat(str[0]).concat("-").concat(str[1]) + " Date");

                for (String s :
                        str) {
                    Log.i("string split", s);
                }
                Date date = new Date();
                date.setMonth(Integer.parseInt(str[0]));
                date.setDate(Integer.parseInt(str[1]));
                date.setYear(Integer.parseInt(str[2]));
//                Date date = new Date(str[2].concat("-").concat(str[0]).concat("-").concat(str[1]));
                Log.i("date", date + " Date");
                Log.i("stop", "Main Activity Stop" + (date.getTime() - System.currentTimeMillis()));

                Intent intent = new Intent(this, AlarmIntentService.class);
                intent.putExtra("DURATION", date.getTime() - System.currentTimeMillis());
                intent.putExtra("MESSAGE", "on Stop");
                AlarmManager alarmManager =
                        (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pendingIntent =
                        PendingIntent.getService(this, 1, intent,
                                PendingIntent.FLAG_IMMUTABLE);
                if (pendingIntent != null && alarmManager != null) {
                    alarmManager.cancel(pendingIntent);
                }
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        SystemClock.elapsedRealtime() + 1000, pendingIntent);
            });
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_term:
                startActivity(new Intent(this, TermView.class));
                return true;
            case R.id.add_assessment:
                startActivity(new Intent(this, AssessmentView.class));
                return true;
            case R.id.add_course:
                startActivity(new Intent(this, CourseView.class));
                return true;
            case R.id.add_instructor:
                startActivity(new Intent(this, InstructorView.class));
                return true;
            case R.id.view_term:
                startActivity(new Intent(this, TermList.class));
                return true;
            case R.id.view_assessment:
                startActivity(new Intent(this, AssessmentList.class));
                return true;
            case R.id.view_course:
                startActivity(new Intent(this, CourseList.class));
                return true;
            case R.id.view_instructor:
                startActivity(new Intent(this, InstructorList.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        mLSTCourseList.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(context, CourseView.class);
            intent.putExtra("courseID", courseList.get(i).getID());
            intent.putExtra("startDate", courseList.get(i).getStartDate());
            intent.putExtra("endDate", courseList.get(i).getEndDate());
            intent.putExtra("status", courseList.get(i).getStatus());
            intent.putExtra("assessment", courseList.get(i).getAssessment());
            intent.putExtra("term", courseList.get(i).getTerm());
            intent.putExtra("title", courseList.get(i).getTitle());
            intent.putExtra("notes", courseList.get(i).getNotes());
            startActivity(intent);
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
/*

***    6.  Include features that allow the user to do the following for each course:
        f.  Set alerts for the start and end date, that will trigger when the application is not running.
        g.  Share notes via a sharing feature (either e-mail or SMS) that automatically populates with the notes.

***    7.  Include features that allow the user to do the following for each assessment:
        c.  Set alerts for start and end dates, that will trigger when the application is not running.


*** C.  Implement a scheduler within your application from Part A and include the following elements:

        •  an application title and an icon
        •  notifications or alerts

D.  Create a storyboard to demonstrate application flow that includes each of the menus and screens from part B.
E.  Provide screen shots of generating the signed APK to demonstrate that you have created a deployment package.
    Note: Verify that all the required functions of your application are working by executing the apk file.
F.  Reflect on the creation of your mobile application by doing the following:
    1.  Explain how your application would be different if it were developed for a tablet rather than a phone, including a discussion of fragments and layouts.
    2.  Identify the minimum and target operating system your application was developed under and is compatible with.
    3.  Describe (suggested length of 1–2 paragraphs) the challenges you faced during the development of the mobile application.
    4.  Describe (suggested length of 1–2 paragraphs) how you overcame each challenge discussed in part F3.
    5.  Discuss (suggested length of 1–2 paragraphs) what you would do differently if you did the project again.
    6.  Describe how emulators are used and the pros and cons of using an emulator versus using a development device.

G.  Acknowledge sources, using APA-formatted in-text citations and references, for content that is quoted, paraphrased, or summarized.
H.  Demonstrate professional communication in the content and presentation of your submission.
 */