package com.example.task91p;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class showitemActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Database DB;
    ArrayList<String> name;
    Button back;
    private Myadapter.RecyclerViewClickListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = new ArrayList<>();
        setContentView(R.layout.activity_showitem);
        recyclerView = findViewById(R.id.re);
        back = findViewById(R.id.back);
        DB = new Database(this);
        if(DB.getAlldata()==null){}
        else {
            name = DB.getAlldata();}
        listener = new Myadapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(showitemActivity.this, "Test", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),removeActivity.class);
                intent.putExtra("key",name.get(position));
                startActivity(intent);


            }
        };


        Myadapter adapter = new Myadapter(this,name,listener);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

            }
        });

       /* listener = new MyAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(),removeActivity.class);
                intent.putExtra("key",name.get(position));
                startActivity(intent);

            }
        };*/


    }
}