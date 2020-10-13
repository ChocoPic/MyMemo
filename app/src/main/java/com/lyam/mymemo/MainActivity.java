package com.lyam.mymemo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button newBtn, saveBtn, closeBtn, colorPicker;
    private EditText newMemoText;
    private LinearLayout newMemo_layout;
    private RecyclerView recyclerView;
    private ArrayList<MyMemo> items;
    private RecyclerAdapter adapter;
    private SQLdbHelper helper;
    private int color;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newMemo_layout = findViewById(R.id.new_memo_layout);
        recyclerView = findViewById(R.id.memo_list);
        newBtn = findViewById(R.id.newBtn);
        saveBtn = findViewById(R.id.saveBtn);
        closeBtn = findViewById(R.id.closeBtn);
        newMemoText = findViewById(R.id.memoText);
        colorPicker = findViewById(R.id.colorPicker);

        helper = new SQLdbHelper(this);
        newMemo_layout.setVisibility(View.GONE);

        items = new ArrayList<>();
        items = loadDataList();

        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setReverseLayout(true);  //역순
        layout.setStackFromEnd(true);   //역순
        recyclerView.setLayoutManager(layout);

        adapter = new RecyclerAdapter(items, this);
        recyclerView.setAdapter(adapter);


        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMemo_layout.setVisibility(View.VISIBLE);
                newMemoText.setText("");
            }
        });
        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //컬러피커띄우기
                ColorPicker colorPicker = new ColorPicker(MainActivity.this, new ColorPicker.DialogListener() {
                    @Override
                    public void Listener(int colorValue) {
                        color = colorValue;
                    }
                });
                colorPicker.callDialog();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memo = newMemoText.getText().toString();
                MyMemo myMemo = new MyMemo(memo, color);
                helper.insertMemo(memo, color);
                items.add(myMemo);  //첫번째에 삽입
                adapter.notifyItemInserted(items.size()-1);  //첫번째에 삽입
                newMemo_layout.setVisibility(View.GONE);

            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMemo_layout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    //읽어오기
    public ArrayList<MyMemo> loadDataList() {
        ArrayList<MyMemo> memos = new ArrayList<>();;
        int cnt = 0;
        try {
            helper = new SQLdbHelper(MainActivity.this);
            Cursor c = helper.getAllData();
            if (c.moveToFirst()) {
                do {
                    String data = c.getString(1);
                    int color = c.getInt(2);
                    memos.add(new MyMemo(data, color));
                    System.out.println(cnt + " 번째: " + memos.get(cnt).getData() + " 색 " + memos.get(cnt).getColor());
                    cnt++;
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return memos;
    }
}
