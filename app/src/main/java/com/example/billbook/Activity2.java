package com.example.billbook;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class Activity2<firebaseDatabase, databaseReference> extends AppCompatActivity {
    private TextView fname, semail, scontact, x, total;
    Button mail;
    EditText EmailAddress, cname;
    Date date = new Date();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("sname");
        fname = findViewById(R.id.fname);
        getdata();

        //String name = getIntent().getStringExtra("keyname");

        semail = findViewById(R.id.semail);
        String email = getIntent().getStringExtra("keysemail");
        semail.setText(email);

        scontact = findViewById(R.id.scontact);
        String contact = getIntent().getStringExtra("keyscontact");
        scontact.setText(contact);

        x = findViewById(R.id.x);
        ArrayList<String> items = (ArrayList<String>) getIntent().getSerializableExtra("keyitems");
        x.setText(String.valueOf(items));

        total = findViewById(R.id.total);
        String stotal = getIntent().getStringExtra("keysum");
        total.setText(stotal);

        cname = findViewById(R.id.cname);

        EmailAddress = findViewById(R.id.EmailAddress);
        mail = findViewById(R.id.mail);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = "mg9417054@gmail.com";
                final String password = "eHi3mohwier";
                String messageToSend = "Dear "+cname.getText().toString()+" Details of invoice from  "+fname.getText().toString()+" Of your Purchase on "+date+" is \n"+x.getText().toString()+"\n Total Amount is: "+total.getText().toString()+"\n -For bill related query \n           "+semail.getText().toString()+"\n           "+scontact.getText().toString()+"\n\n\n\n -Powered By BillBook.com";
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                Session session=Session.getInstance(props,
                        new javax.mail.Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                //new PasswordAuthentication(username1, password);
                                return new PasswordAuthentication(username, password);
                            }
                        });
                try{
                    Message message1= new MimeMessage(session);
                    message1.setFrom(new InternetAddress(username));
                    String Emails= EmailAddress.getText().toString()+","+semail.getText().toString();
                    message1.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Emails));
                    message1.setSubject("Payment Reciept From BillBook");
                    message1.setText(messageToSend);
                    Transport.send(message1);
                    Toast.makeText(getApplicationContext(), "email sent successfully",Toast.LENGTH_LONG).show();

                }catch (MessagingException e){
                    throw new RuntimeException(e);
                }

            };
        });

        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void getdata() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.getValue(String.class);
                Log.d("------------------------------------------------------",name);
                fname.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("-----------------------------------------------------------","abc");
                Toast.makeText(Activity2.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}