package com.tanat.nodeadstesttask.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tanat.nodeadstesttask.db.DataSource;
import com.tanat.nodeadstesttask.model.Declaration;

import java.util.ArrayList;
import java.util.List;

public class FavoritesLoader extends AbstractLoader<List<Declaration>> {
    private DataSource dataSource;

    public FavoritesLoader(Context context, Bundle args) {
        super(context);
        dataSource = DataSource.getInstance(getContext());
    }

    @Nullable
    @Override
    public List<Declaration> loadInBackground() {
        Cursor cursor = dataSource.getAllItems();
        List<Declaration> list = new ArrayList<>();
        if (null != cursor) {
            int columnId = cursor.getColumnIndex(DataSource.COLUMN_ID);
            int columnName = cursor.getColumnIndex(DataSource.COLUMN_FIRSTNAME);
            int columnLastName = cursor.getColumnIndex(DataSource.COLUMN_LASTNAME);
            int columnPlace = cursor.getColumnIndex(DataSource.COLUMN_PLACE_OF_WORK);
            int columnPos = cursor.getColumnIndex(DataSource.COLUMN_POSITION);
            int columnPdf = cursor.getColumnIndex(DataSource.COLUMN_LINK_PDF);
            int columnComment = cursor.getColumnIndex(DataSource.COLUMN_COMMENT);

            while (cursor.moveToNext()) {
                String id = cursor.getString(columnId);
                String name = cursor.getString(columnName);
                String lastName = cursor.getString(columnLastName);
                String place = cursor.getString(columnPlace);
                String pos = cursor.getString(columnPos);
                String pdf = cursor.getString(columnPdf);
                String comment = cursor.getString(columnComment);

                list.add(new Declaration(id, name, lastName, place, pos, pdf, comment));
            }
        }
        return list;
    }
}