package com.iot.a20220711_sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import static com.iot.a20220711_sqlite.DBOpenHelper.TABLE_NAME;
import static com.iot.a20220711_sqlite.DBOpenHelper.TIME;
import static com.iot.a20220711_sqlite.DBOpenHelper.TITLE;
import static com.iot.a20220711_sqlite.DBOpenHelper._ID;

public class MainActivity extends AppCompatActivity {

    private static final String DB_NAME = "MyDB";
    private static final int DB_VERSION = 1;
    private DBOpenHelper openHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openHelper = new DBOpenHelper(this, DB_NAME, null, DB_VERSION);
        writeDB("Hello World!");
        Cursor cursor = readDB();
        displyDB(cursor);
        openHelper.close();
    }

    private void displyDB(Cursor cursor) {
        StringBuilder builder = new StringBuilder("Saved DB:\n");
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            long time = cursor.getLong(1);
            String title = cursor.getString(2);
            builder.append(id).append(": ");
            builder.append(time).append(": ");
            builder.append(title).append("\n");
        }
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(builder);
    }

    private Cursor readDB() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        String[] from = {_ID,TIME,TITLE,};
        Cursor cursor = db.query(TABLE_NAME, from, null,null,null,null,TIME+" "+"DESC");
        startManagingCursor(cursor);
        return cursor;
    }

    private void writeDB(String title) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME, System.currentTimeMillis());
        values.put(TITLE, title);
        db.insertOrThrow(TABLE_NAME,null,values);
    }
}