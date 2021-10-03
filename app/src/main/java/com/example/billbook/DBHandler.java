package com.example.billbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBHandler extends SQLiteOpenHelper{
    private static final String DB_NAME = "billbook";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "user";
    private static final String SNAME_COL = "sname";
    private static final String CONTACT_COL = "contact";
    private static final String EMAIL_COL = "email";
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + SNAME_COL + " TEXT DEFAULT 'NOT SET' ,"
                + CONTACT_COL + " TEXT DEFAULT 0,"
                + EMAIL_COL + " TEXT)";
        db.execSQL(query);
        db.execSQL("insert into user values('Null','Null','Null')");
    }
    public void addNewCourse(String sname, String contact, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SNAME_COL, sname);
        values.put(CONTACT_COL, contact);
        values.put(EMAIL_COL, email);
        db.update("user", values, "1=1",new String[]{});
        db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public Cursor fetchAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user limit 1", null);
        return cursor;
    }
}
