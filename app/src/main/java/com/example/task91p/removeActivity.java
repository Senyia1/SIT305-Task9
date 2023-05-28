package com.example.task91p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class removeActivity extends AppCompatActivity {

    TextView itemname, itemdate, itemlocation;
    Database DB;
    Button remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DB = new Database(this);
        setContentView(R.layout.activity_remove);
        itemname = findViewById(R.id.removename);
        itemdate = findViewById(R.id.removedate);
        itemlocation = findViewById(R.id.removelocation);
        remove = findViewById(R.id.remove);
        int position = 1;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            position = extras.getInt("key");
        }
        itemname.setText(DB.getname(position));
        itemdate.setText(DB.getdate(position));
        itemlocation.setText(DB.getlocation(position));

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.delete(itemname.getText().toString());
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);



            }
        });

    }
}