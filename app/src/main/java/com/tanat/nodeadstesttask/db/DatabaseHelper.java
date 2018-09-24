package com.tanat.nodeadstesttask.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 12;
    private static final String DATA_SCHEME = "dbscheme.ddl";

    private static DatabaseHelper instance;
    private AssetManager assetManager;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        assetManager = context.getAssets();
    }

    protected static DatabaseHelper getInstance(Context context) {
        if (null == instance) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            createTablesIfNotExists(database);
        } catch (Exception e) {
            Log.e(String.valueOf(getClass()), "onCreate", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        try {
            createTablesIfNotExists(database);
        } catch (IOException e) {
            Log.e(String.valueOf(getClass()), "onUpgrade oldVersion=" + oldVersion
                    + " , newVersion=" + newVersion, e);
        }
    }

    private void createTablesIfNotExists(SQLiteDatabase database) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStreamReader source = new InputStreamReader(assetManager.open(DATA_SCHEME));
        BufferedReader reader = new BufferedReader(source);
        String line;
        List<String> commands = new ArrayList<>();
        while (true) {
            line = reader.readLine();
            if (line == null) {
                commands.add(builder.toString());
                break;
            }
            if (line.trim().length() == 0 && builder.capacity() > 0) {
                commands.add(builder.toString());
                builder = new StringBuilder();
                continue;
            }
            builder.append(line);
        }
        for (int i = 0; i < commands.size(); i++) {
            database.execSQL(commands.get(i));
        }
    }
}
