package com.pkhuang.asg2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class ImageDB extends SQLiteOpenHelper {

    private static ImageDB sInstance;

    // database info
    Context ctx;
    static String DB_NAME = "DATABASE";
    static String TABLE_NAME = "DOWNLOADS";
    static int VERSION = 1;

    /**
     * Constructor is private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private ImageDB(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    // called when db is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(_id INTEGER PRIMARY KEY " +
                "AUTOINCREMENT, TITLE TEXT, IMAGE BLOB);");
    }

    // called when db needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    /**
     * Ensures only one ImageDB will exist at any given time. if sInstance has not been initialized
     * one will be created. If one has already been created then it'll be returned.
      */
    public static synchronized ImageDB getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ImageDB(context.getApplicationContext());
        }
        return sInstance;
    }

    // insert row into table
    public void insertData(String title, byte[] image){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_NAME + " VALUES (NULL, ?, ?)";

        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.clearBindings();

        // bind values to statements
        stmt.bindString(1, title);
        stmt.bindBlob(2, image);

        stmt.executeInsert();
    }

    // delete row from table
//    public  void deleteData(int id, String title) {
//        SQLiteDatabase db = getWritableDatabase();
//        String sql = "DELETE FROM " + TABLE_NAME + " WHERE _id = ? OR TITLE = ?";
//
//        SQLiteStatement statement = db.compileStatement(sql);
//        statement.clearBindings();
//
//        statement.bindDouble(1, (double)id);
//        statement.bindString(2, title);
//
//        statement.execute();
//        db.close();
//    }

    // delete row from table by ID
    public  void deleteDataById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE _id = ?";

        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();

        statement.bindDouble(1, (double)id);

        statement.execute();
        db.close();
    }

    // delete row from table by title
    public  void deleteDataByTitle(String title) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE TITLE = ?";

        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, title);

        statement.execute();
        db.close();
    }


    // get all data from the db
    public Cursor getData(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);
    }
}