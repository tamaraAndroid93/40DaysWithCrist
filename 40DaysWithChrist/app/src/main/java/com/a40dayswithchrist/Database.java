package com.a40dayswithchrist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Alen on 8/11/2016.
 */
public class Database {
    //TABLE NAME
    private static final String TABLE_NAME_CV="ChristTable";
    //CV TABLE column name
    private static final String CV_ID="id";

    private static final String PATH="path";


    private static final String[] CV_COLUMN= {CV_ID,PATH};


    private Context mContext;
    private CvDatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public Database(Context context){
        this.mContext=context;
        this.mDbHelper=new CvDatabaseHelper(context);
        this.mDb=mDbHelper.getWritableDatabase();

    }
    public void closeDataBase(){
        if(mDb!=null){
            mDb.close();
        }
    }

    public void insertCv(Record itemRecord){
        ContentValues values=new ContentValues();
        values.put(PATH,itemRecord.getPath());


        mDb.insert(TABLE_NAME_CV, null, values);//vraca id pa moze da se stavi da vraca long umesto void pa return  return  mDb.insert(TABLE_NAME_CV,"",values)

    }


    public ArrayList<Record> getCvList_Id(){
        ArrayList<Record>list =new ArrayList<>();

        String[]projection={CV_ID,PATH};

        Cursor c=mDb.query(TABLE_NAME_CV,projection,null,null,null,null,CV_ID+ " ASC");

        if(c!=null) {

            if (c.moveToFirst()) {
                c.moveToFirst();
                do {
                    Record dataCv = new Record();
                    dataCv.setPath(c.getString(c.getColumnIndex(PATH)));

                    list.add(dataCv);
                }
                while (c.moveToNext());
            }
        }
        return list;
    }
    public boolean deleteCv(Record dataItem){
        int rowCount=0;

        String arg[]={String.valueOf(dataItem.getId())};
        rowCount=mDb.delete(TABLE_NAME_CV,CV_ID+" =?",arg);

        return rowCount>0;
    }


    public class CvDatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "DatabaseChrist.db";
        private static final int DATABASE_VERSION = 1;

        private String CREATE_TABLE_CV_SQL = "CREATE TABLE " + TABLE_NAME_CV +
                " (" + CV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                PATH + " TEXT);";
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
