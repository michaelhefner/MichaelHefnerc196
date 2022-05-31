package com.michaelhefner.michaelhefnerc196.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.michaelhefner.michaelhefnerc196.R;
import com.michaelhefner.michaelhefnerc196.controller.DBHandler;
import com.michaelhefner.michaelhefnerc196.model.Course;
import com.michaelhefner.michaelhefnerc196.model.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TermList extends AppCompatActivity {

    private DBHandler mDBHandler;
    private ListView mListView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        context = this;
        mListView = findViewById(R.id.lvTermList);
        mDBHandler = new DBHandler(this);
        List<Term> termList = mDBHandler.termQuery("");
        List<String> stringList = new ArrayList<>();
        if (termList.size() > 0) {
            IntStream.range(0, termList.size()).forEach(i -> {
                stringList.add(termList.get(i).getTitle());
            });
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringList);
        mListView.setAdapter(itemsAdapter);
        mListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(context, TermView.class);
            intent.putExtra("id", termList.get(i).getID());
            intent.putExtra("name", termList.get(i).getName());
            intent.putExtra("end", termList.get(i).getEnd());
            intent.putExtra("start", termList.get(i).getStart());
            intent.putExtra("title", termList.get(i).getTitle());
            startActivity(intent);
        });
    }
}