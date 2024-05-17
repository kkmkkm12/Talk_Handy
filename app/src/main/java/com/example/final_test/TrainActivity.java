package com.example.final_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TrainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private String son[] = {"ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
    private String mom[] = {"ㅏ", "ㅑ", "ㅓ", "ㅕ", "ㅗ", "ㅛ", "ㅜ", "ㅠ", "ㅡ", "ㅣ", "ㅐ", "ㅒ", "ㅔ", "ㅖ", "ㅚ", "ㅟ", "ㅢ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        listView = findViewById(R.id.listView);

        arrayList = new ArrayList<String>();
        for(int i = 0; i < son.length; i++) arrayList.add(son[i]);
        for(int i = 0; i < mom.length; i++) arrayList.add(mom[i]);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = arrayList.get(position);          //현재 선택된 리스트의 string 값을 받아 selectedItem에 들어감

                // 다른 액티비티로 넘어가기 위해 인텐트를 생성
                Intent intent = new Intent(TrainActivity.this, StudyActivity.class);

                // 선택된 항목을 인텐트에 추가
                intent.putExtra("selectedItem", selectedItem);

                // 액티비티 시작
                startActivity(intent);
            }
        });
    }
}