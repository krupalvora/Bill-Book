package com.example.billbook;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {

    public TextView about_text;
    public VideoView videoview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        String s="Steps-\n1: Click on > button\n" +
                "2:  Set your username, email, contact number\n" +
                "3:  In that same section add your product details .\n" +
                "4:  To delete product details just add product name\n" +
                "5:  To generate bill scan each product.\n" +
                "6:  If scanner dont work you can add product manually.\n" +
                "7:  By long press you can delete item\n" +
                "8:  After scanning all products click on done button.\n" +
                "9:  Enter customer name & email and click on send\n     button.\n" +
                "10:  For details view video given below";
        about_text = findViewById(R.id.about_text);
        about_text.setText(s);

    }

}
