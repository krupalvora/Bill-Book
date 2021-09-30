package com.example.billbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class MainActivity<firebaseDatabase, databaseReference> extends AppCompatActivity implements View.OnClickListener{

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private static final String TAG = "";
    Button scanBtn;
    Button button,ok,menu;
    ListView listview;
    private ArrayAdapter<String> adapter;
    TextView textView;
    EditText editTextNumber;
    EditText editTextName;
    int sum=0;
    private ArrayList<String> x=new ArrayList<String>();
    ArrayList<Integer> x1=new ArrayList<Integer>();
    ArrayList<String> x2=new ArrayList<String>();
    String[] code1={"H201","9788189725624","KH40","EM216640885IN","123456789012","8906108610832"};
    String[] name={"hing","book","Masala khari","SpeedPost","Sample_Product","Zebronics Keyboard"};
    int[] price={20,100,40,90,100,250};
    String fname="Z-MART";
    String semail="vadhiyavivek56@gmail.com";
    String scontact="1234567890";
    List<String> code=new ArrayList<>(Arrays.asList(code1));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("sname");
        getdata();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        textView=(TextView)findViewById(R.id.textView2);
        button= (Button)findViewById(R.id.button);
        ok=(Button)findViewById(R.id.ok);
        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextNumber=(EditText)findViewById(R.id.editTextNumber);
        menu=(Button)findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,User.class);
                startActivity(intent);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additem();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenshot();
            }
        });
        scanBtn=findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);
    }

    private void getdata() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void additem(){
        String newitem=editTextName.getText().toString();
        int newprice=Integer.parseInt(editTextNumber.getText().toString());
        x.add(newitem);
        x1.add(newprice);
        x2.add(newitem+" -> "+newprice+" /-");
        sum=0;

        ListView listView=findViewById(R.id.listView);
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,x2);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int which_item = position;
                Log.d(TAG,"???????????????????????????????????????????"+ which_item);

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure ?")
                        .setMessage("Do you want to delete this item")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                x2.remove(which_item);
                                x1.remove(which_item);
                                x.remove(which_item);
                                adapter.notifyDataSetChanged();
                                sum=0;
                                for (int i:x1)
                                    sum += i;
                                textView.setText("Total : "+String.valueOf(new Integer(sum))+"/-");
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
        Log.d(TAG,"???????????????????????????????????????????"+ x+x1+x2);
        sum=0;
            for (int i : x1)
                sum += i;
            textView.setText("Total : " + String.valueOf(new Integer(sum)) + "/-");

    }
    private void screenshot() {
        Intent intent= new Intent(MainActivity.this,Activity2.class);
        intent.putExtra("keyname",fname);
        intent.putExtra("keysemail",semail);
        intent.putExtra("keyscontact",scontact);
        intent.putExtra("keyitems",x2);
        String total=Integer.toString(sum);
        intent.putExtra("keysum",total);
        startActivity(intent);
        x.clear();
        x1.clear();
        x2.clear();
        int sum=0;

    }
    @Override
    public void onClick(View v) {
        scanCode();
    }
    private void scanCode(){
        IntentIntegrator integrator=new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("scanning");
        integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()!=null){
                sum=0;
                String txt=result.getContents().toString();
                if (code.contains(txt)){
                    int count=code.indexOf(txt);
                    x.add(name[count]);
                    x1.add(price[count]);
                    x2.add(name[count]+" -> "+price[count]+" /-");
                    Log.d(TAG,"???????????????????????????????????????????"+ x2);
                    ListView listView=findViewById(R.id.listView);
                    ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,x2);
                    listView.setAdapter(adapter);
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                            final int which_item = position;
                            Log.d(TAG,"???????????????????????????????????????????"+ which_item);

                            new AlertDialog.Builder(MainActivity.this)
                                    .setIcon(android.R.drawable.ic_delete)
                                    .setTitle("Are you sure ?")
                                    .setMessage("Do you want to delete this item")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            x2.remove(which_item);
                                            x1.remove(which_item);
                                            x.remove(which_item);
                                            adapter.notifyDataSetChanged();
                                            sum=0;
                                            for (int i:x1)
                                                sum += i;
                                            textView.setText("Total : "+String.valueOf(new Integer(sum))+"/-");
                                        }
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                            return true;
                        }
                    });


                }
                for (int i:x1)
                    sum += i;
                textView.setText("Total : "+String.valueOf(new Integer(sum))+"/-");
            }
            else{
                Toast.makeText(this,"no result", Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}