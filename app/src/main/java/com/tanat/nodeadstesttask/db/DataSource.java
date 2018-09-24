package com.tanat.nodeadstesttask.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tanat.nodeadstesttask.model.Declaration;

public class DataSource {
    public static String FAVORITES_TABLE = "favorites_table";

    public static String COLUMN_ID = "_id";
    public static String COLUMN_FIRSTNAME = "firstname";
    public static String COLUMN_LASTNAME = "lastname";
    public static String COLUMN_PLACE_OF_WORK = "placeOfWork";
    public static String COLUMN_POSITION = "position";
    public static String COLUMN_LINK_PDF = "linkPDF";
    public static String COLUMN_COMMENT = "comment";


    private static DataSource INSTANCE = null;
    private Context context;

    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private DataSource(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
        this.context = context;
    }

    public static DataSource getInstance(Context context) {
        if (null == INSTANCE) {
            INSTANCE = new DataSource(context);
            INSTANCE.open();
        }
        return INSTANCE;
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public Cursor getAllItems() {
        return database.rawQuery("SELECT * FROM " + FAVORITES_TABLE, null);
    }

    public boolean itemIsExist(String id){
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(
                    "SELECT EXISTS(" +
                            "SELECT *" +
                            "FROM " + FAVORITES_TABLE +
                            " WHERE " + COLUMN_ID + "=?" +
                            "LIMIT 1)"
                    , new String[]{id});

            while (cursor.moveToNext()) {
                if (cursor.getInt(0) == 0) {
                    return false;
                } else if (cursor.getInt(0) == 1){
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e("DataSource", e.toString());
            return false;
        } finally {
            if (null != cursor){
                cursor.close();
            }
        }
        return false;
    }

    public void addItem(Declaration declaration) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, declaration.getId());
        values.put(COLUMN_FIRSTNAME, declaration.getFirstname());
        values.put(COLUMN_LASTNAME, declaration.getLastname());
        values.put(COLUMN_PLACE_OF_WORK, declaration.getPlaceOfWork());
        values.put(COLUMN_POSITION, declaration.getPosition());
        values.put(COLUMN_LINK_PDF, declaration.getLinkPDF());
        values.put(COLUMN_COMMENT, declaration.getComment());
        database.insert(FAVORITES_TABLE, null, values);
    }

    public void updateItem(Declaration declaration) {
        ContentValues values = new ContentValues();
        if (declaration.getId() != null) values.put(COLUMN_ID, declaration.getId());
        if (declaration.getFirstname() != null) values.put(COLUMN_FIRSTNAME, declaration.getFirstname());
        if (declaration.getLastname() != null) values.put(COLUMN_LASTNAME, declaration.getLastname());
        if (declaration.getPlaceOfWork() != null) values.put(COLUMN_PLACE_OF_WORK, declaration.getPlaceOfWork());
        if (declaration.getPosition() != null) values.put(COLUMN_POSITION, declaration.getPosition());
        if (declaration.getLinkPDF() != null) values.put(COLUMN_LINK_PDF, declaration.getLinkPDF());
        if (declaration.getComment() != null) values.put(COLUMN_COMMENT, declaration.getComment());
        database.update(FAVORITES_TABLE, values, COLUMN_ID + " =?", new String[]{declaration.getId()});
    }

    public void deleteItem(String id) {
        database.delete(FAVORITES_TABLE, COLUMN_ID + " =?", new String[]{id});
    }
}
