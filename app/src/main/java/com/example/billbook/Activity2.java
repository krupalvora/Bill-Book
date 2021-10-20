package com.example.billbook;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Activity2 extends AppCompatActivity {
    public TextView fname, semail, scontact, total;
    public Button mail;
    public String t,s_email,s_contact;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    public RecyclerView.Adapter adapter;
    EditText EmailAddress, cname;
    Date date = new Date();
    public DBHandler db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        db=new DBHandler(this);
        Cursor cursor=db.fetchAllData();

        getWindow().setStatusBarColor(ContextCompat.getColor(Activity2.this,R.color.black));

        if(cursor.getCount()==0){
            Toast.makeText(getApplicationContext(),"No username",Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()) {
                fname = findViewById(R.id.fname);
                String name = cursor.getString(0  );
                fname.setText(name);
                //semail = findViewById(R.id.semail);
                 s_email = cursor.getString(2);
                t=cursor.getString(2);
                //semail.setText(email);
                //scontact = findViewById(R.id.scontact);
                 s_contact = cursor.getString(1);
                //scontact.setText(contact);
            }
        }
        ArrayList<String> items = (ArrayList<String>) getIntent().getSerializableExtra("keyitems");
        recyclerView = (RecyclerView) findViewById(R.id.x);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new MainAdapter(items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        total = findViewById(R.id.total);
        String stotal = "Total : "+getIntent().getStringExtra("keysum")+"/-";
        total.setText(stotal);
        cname = findViewById(R.id.cname);
        EmailAddress = findViewById(R.id.EmailAddress);


        mail = findViewById(R.id.mail);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"________________hhhioh______"+cname.getText().toString()+"$$$"+cname.getText().toString().length());
                if (cname.getText().toString().length()==0) {
                    new AlertDialog.Builder(Activity2.this)
                            .setIcon(android.R.drawable.ic_delete)
                            .setTitle("Invalid Entry")
                            .setMessage("Please enter all input fields")
                            .setNegativeButton("OK", null)
                            .show();
                    return;
                }
                final String username = "*********";
                final String password = "*********";
                String x="";
                for (int j = 0; j < items.size(); j++) {
                    x=x+items.get(j)+"\n";
                    Log.e(TAG,"==============================="+j+x);
                }
                //Log.e(TAG,"===============================",x);
                String messageToSend = "Dear "+cname.getText().toString()+","+"\nDetails of invoice from  "
                        +fname.getText().toString()+" Of your Purchase on "+date
                        +" is \n"+x +"\n Total Amount is: "+total.getText().toString()+
                        "\n -For bill related query \n           "+s_email+"\n " +
                        "          "+s_contact+"\n\n\n\n -Powered By BillBook.com";
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
                    String Emails= EmailAddress.getText().toString()+","+s_email;
                    message1.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Emails));
                    message1.setSubject("Payment Reciept From BillBook");
                    message1.setText(messageToSend);
                    Transport.send(message1);
                    Toast.makeText(getApplicationContext(), "email sent successfully",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Activity2.this, MainActivity.class);
                    startActivity(intent);
                }catch (MessagingException e){
                    new AlertDialog.Builder(Activity2.this)
                            .setIcon(android.R.drawable.ic_delete)
                            .setTitle("Invalid Email Address")
                            .setMessage("Set your correct gmail address or check customer email address")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Activity2.this, User.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            };
        });
        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


}
