package com.oni.ibmtranslator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditContent extends AppCompatActivity {

    private Button btnSave,btnDelete, btnTranslator2;
    private EditText editable_item;

    DatabaseHelper DB;

    private String selectedWord;
    private int selectedID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_content);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        editable_item = (EditText) findViewById(R.id.editable_item);
        btnTranslator2 = (Button) findViewById(R.id.btnTranslator2);
        DB = new DatabaseHelper(this);

        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //get the itemID
        selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        //get the name
        selectedWord = receivedIntent.getStringExtra("word");

        //set the text to show the current selected word
        editable_item.setText(selectedWord);

        btnTranslator2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditContent.this, MainActivity.class);
                startActivity(intent);
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = editable_item.getText().toString();
                if(!item.equals("")){
                    DB.updatePhrase(item,selectedID,selectedWord);
                    Intent intent = new Intent(EditContent.this, ViewContent.class);
                    startActivity(intent);
                }else{
                    toastMessage("You must enter a Word");
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.deleteWord(selectedID,selectedWord);
                editable_item.setText("");
                toastMessage("removed from database");
                Intent intent = new Intent(EditContent.this, ViewContent.class);
                startActivity(intent);
            }
        });

    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
