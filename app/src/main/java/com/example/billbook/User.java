package com.example.billbook;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class User extends AppCompatActivity {
    public Button save;
    public EditText sname,email,contact;
    public DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        sname= findViewById(R.id.x_sname);
        contact= findViewById(R.id.x_contact);
        email= findViewById(R.id.x_email);
        save=findViewById(R.id.save);
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
                email.setText(""); }
        });
    }
}