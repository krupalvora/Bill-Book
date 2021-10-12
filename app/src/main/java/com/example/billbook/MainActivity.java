package com.example.billbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public DBHandler db;
    private static final String TAG = "";
    Button scanBtn;
    Button button, ok, menu;
    ListView listview;
    private ArrayAdapter<String> adapter;
    TextView textView;
    EditText editTextNumber;
    EditText editTextName;
    int sum = 0;
     ArrayList<String> x = new ArrayList<String>();
    ArrayList<Integer> x1 = new ArrayList<Integer>();
    ArrayList<String> x2 = new ArrayList<String>();
    ArrayList<String> code1= new ArrayList<String>();
    ArrayList<Integer> price= new ArrayList<Integer>();
    ArrayList<String> name= new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHandler(this);
        Cursor pdetails = db.fetchDetails();
        if (pdetails.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (pdetails.moveToNext()) {
                String c=pdetails.getString(0);
                Integer p= Integer.valueOf(pdetails.getString(1));
                String b=pdetails.getString(2);
                name.add(c);
                price.add(p);
                code1.add(b);
            }
        }
            setContentView(R.layout.activity_main);
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Log.e(TAG,"---------------------------------"+name);
            textView = (TextView) findViewById(R.id.textView2);
            button = (Button) findViewById(R.id.button);
            ok = (Button) findViewById(R.id.ok);
            editTextName = (EditText) findViewById(R.id.editTextName);
            editTextNumber = (EditText) findViewById(R.id.editTextNumber);
            menu = (Button) findViewById(R.id.menu);
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, User.class);
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
            scanBtn = findViewById(R.id.scanBtn);
            scanBtn.setOnClickListener(this);
        }
        private void additem () {
            String newitem = editTextName.getText().toString();
            int newprice = Integer.parseInt(editTextNumber.getText().toString());
            x.add(newitem);
            x1.add(newprice);
            x2.add("    "+newitem + "   -  " + newprice + " /-");
            sum = 0;
            ListView listView = findViewById(R.id.listView);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, x2) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);

                    // Set the text size 25 dip for ListView each item
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);

                    // Return the view
                    return view;
                }
            };
            listView.setAdapter(adapter);
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final int which_item = position;
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
                                    sum = 0;
                                    for (int i : x1)
                                        sum += i;
                                    textView.setText("Total : " + String.valueOf(new Integer(sum)) + "/-");
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                    return true;
                }
            });
            sum = 0;
            for (int i : x1)
                sum += i;
            textView.setText("Total : " + String.valueOf(new Integer(sum)) + "/-");
        }
        private void screenshot () {
            Intent intent = new Intent(MainActivity.this, Activity2.class);
            intent.putExtra("keyitems", x2);
            String total = Integer.toString(sum);
            intent.putExtra("keysum", total);
            startActivity(intent);
            x.clear();
            x1.clear();
            x2.clear();
            int sum = 0;
        }
        @Override
        public void onClick (View v){
            scanCode();
        }
        private void scanCode () {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setCaptureActivity(CaptureAct.class);
            integrator.setOrientationLocked(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("scanning");
            integrator.initiateScan();
        }
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (result != null) {
                if (result.getContents() != null) {
                    sum = 0;
                    String txt = result.getContents().toString();
                    if (code1.contains(txt)) {
                        int count = code1.indexOf(txt);
                        x.add(name.get(count));
                        x1.add(price.get(count));
                        x2.add("    "+name.get(count) + "   -   " + price.get(count) + " /-");
                        ListView listView = findViewById(R.id.listView);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, x2);
                        listView.setAdapter(adapter);
                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                final int which_item = position;
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
                                                sum = 0;
                                                for (int i : x1)
                                                    sum += i;
                                                textView.setText("Total : " + String.valueOf(new Integer(sum)) + "/-");
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                                return true;
                            }
                        });
                    }
                    else {
                        Toast.makeText(this, "Product Not Found", Toast.LENGTH_LONG).show();
                    }

                    for (int i : x1)
                        sum += i;
                    textView.setText("Total : " + String.valueOf(new Integer(sum)) + "/-");
                } else {
                    Toast.makeText(this, "no result", Toast.LENGTH_LONG).show();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }