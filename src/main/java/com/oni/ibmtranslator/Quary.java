package com.oni.ibmtranslator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Quary extends AppCompatActivity {

    DatabaseHelper DB;
    Button btnAdd, btnView, btnTranslator;
    EditText mEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quary);

        mEditText = (EditText) findViewById(R.id.editable_item);
        btnAdd = (Button) findViewById(R.id.add);
        btnView = (Button) findViewById(R.id.view);
        btnTranslator = (Button) findViewById(R.id.btnTranslator);
        DB = new DatabaseHelper(this);

        btnTranslator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Quary.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = mEditText.getText().toString();
                if(mEditText.length()!= 0){
                    AddData(newEntry);
                    mEditText.setText("");
                }else{
                    Toast.makeText(Quary.this, "put something in the text field", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Quary.this, ViewContent.class);
                startActivity(intent);
            }
        });


    }

    public void AddData(String newEntry) {

        boolean insertData = DB.addData(newEntry);

        if(insertData==true){
            Toast.makeText(this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
        }
    }
}

