package com.a40dayswithchrist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Alen on 8/12/2016.
 */
public class DateDatabase  {
    //TABLE NAME
    private static final String TABLE_NAME_CV="DateTable";
    //CV TABLE column name
    private static final String ID ="id";

    private static final String DATE ="date";


    private static final String[] CV_COLUMN= {ID, DATE};


    private Context mContext;
    private CvDatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public DateDatabase(Context context){
        this.mContext=context;
        this.mDbHelper=new CvDatabaseHelper(context);
        this.mDb=mDbHelper.getWritableDatabase();

    }
    public void closeDataBase(){
        if(mDb!=null){
            mDb.close();
        }
    }

    public void insertDate(String item){
        ContentValues values=new ContentValues();
        values.put(DATE,item);


        mDb.insert(TABLE_NAME_CV, null, values);//vraca id pa moze da se stavi da vraca long umesto void pa return  return  mDb.insert(TABLE_NAME_CV,"",values)

    }


    public ArrayList<String> getList_Id(){
        ArrayList<String>list =new ArrayList<>();

        String[]projection={ID, DATE};

        Cursor c=mDb.query(TABLE_NAME_CV,projection,null,null,null,null, ID + " ASC");

        if(c!=null) {

            if (c.moveToFirst()) {
                c.moveToFirst();
                do {
                    String dataCv = new String();
                    dataCv=(c.getString(c.getColumnIndex(DATE)));

                    list.add(dataCv);
                }
                while (c.moveToNext());
            }
        }
        return list;
    }



    public class CvDatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "DatabasedChristDatabase.db";
        private static final int DATABASE_VERSION = 12;

        private String CREATE_TABLE_CV_SQL = "CREATE TABLE " + TABLE_NAME_CV +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                DATE + " TEXT);";
        private String DELETE_TABLE_SQL = "DROP TABLE IF EXISTS " + TABLE_NAME_CV;

        public CvDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TABLE_CV_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DELETE_TABLE_SQL);
            onCreate(db);

        }



    }


}

