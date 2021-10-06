package com.example.billbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class User extends AppCompatActivity {
    public Button save,add,delete;
    public EditText sname,email,contact,pname,pprice,pbarcode;
    public DBHandler dbHandler;
    public TextView about1;
    String s="About \nHow to use:\n Step1: Add details\n    Step2: Add product details\n";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        sname= findViewById(R.id.x_sname);
        contact= findViewById(R.id.x_contact);
        email= findViewById(R.id.x_email);
        pname = findViewById(R.id.pname);
        pprice = findViewById(R.id.pprice);
        pbarcode = findViewById(R.id.pbarcode);
        delete = findViewById(R.id.delete);
        about1=findViewById(R.id.about);
        about1.setText(s);
        save=findViewById(R.id.save);
        add = findViewById(R.id.add);
        dbHandler = new DBHandler(User.this);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String j_sname = sname.getText().toString();
                String j_contact = contact.getText().toString();
                String j_email = email.getText().toString();

                if (j_sname.isEmpty() && j_contact.isEmpty() && j_email.isEmpty()) {
                    Toast.makeText(User.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHandler.addNewCourse(j_sname, j_contact, j_email);

                Toast.makeText(User.this, "Details has been added.", Toast.LENGTH_SHORT).show();
                sname.setText("");
                contact.setText("");
                email.setText("");
                Intent intent = new Intent(User.this, MainActivity.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String j_sname = pname.getText().toString();
                String j_contact = pprice.getText().toString();
                String j_email = pbarcode.getText().toString();

                if (j_sname.isEmpty() && j_contact.isEmpty() && j_email.isEmpty()) {
                    Toast.makeText(User.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbHandler.addItem(j_sname, j_contact, j_email);
                Toast.makeText(User.this, "Product has been added.", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(User.this, MainActivity.class);
                startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String j_sname = pname.getText().toString();

                if (j_sname.isEmpty()) {
                    Toast.makeText(User.this, "Please enter Product Name..", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbHandler.delItem(j_sname);
                Toast.makeText(User.this, "Product has been Deleted.", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(User.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}