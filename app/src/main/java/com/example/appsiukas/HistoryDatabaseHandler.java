package com.example.appsiukas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.Key;
import java.util.ArrayList;

/**
 * Created by super on 10/6/2016.
 */
public class HistoryDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "leaderboard.db";

    private static String SCORES_TABLE_NAME = "scores";

    private final static String KEY_ID = "id";
    private final static String KEY_WORD = "word";
    private final static String KEY_LIFES_REMAINING = "lifesRemaining";
    private final static String KEY_DATE = "date";

    public HistoryDatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + SCORES_TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_WORD + " TEXT," +
                KEY_DATE + " TEXT," +
                KEY_LIFES_REMAINING + " TEXT" +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String query = "DROP TABLE IF EXISTS " + SCORES_TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }

    public long addEntry(HistoryEntry entry)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(KEY_WORD, entry.getWord());
        cv.put(KEY_DATE, entry.getGameDate());
        cv.put(KEY_LIFES_REMAINING, entry.getLifesRemaining());

        long id = db.insert(SCORES_TABLE_NAME, null, cv);
        db.close();
        return id;
    }

    public HistoryEntry getEntry(int id)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = null;

        cursor = db.query(SCORES_TABLE_NAME, new String[] { KEY_ID, KEY_WORD, KEY_DATE, KEY_LIFES_REMAINING}, KEY_ID + "=?", new String[] { Integer.toString(id) }, null, null, null);

        HistoryEntry entry = new HistoryEntry();
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                entry.setId(cursor.getInt(0));
                entry.setWord(cursor.getString(1));
                entry.setGameDate(cursor.getString(2));
                entry.setLifesRemaining(cursor.getString(3));
            }
        }

        cursor.close();
        db.close();

        return entry;
    }

    public ArrayList<HistoryEntry> getAllEntries()
    {
        ArrayList<HistoryEntry> entries = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + SCORES_TABLE_NAME + " ORDER BY " + KEY_ID + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    HistoryEntry entry = new HistoryEntry();
                    entry.setId(cursor.getInt(0));
                    entry.setWord(cursor.getString(1));
                    entry.setGameDate(cursor.getString(2));
                    entry.setLifesRemaining(cursor.getString(3));
                    entries.add(entry);
                } while(cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();

        return entries;
    }

    public void deleteEntry(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SCORES_TABLE_NAME, KEY_ID + "=?", new String[]{Integer.toString(id)});
        db.close();
    }

    public void updateEntry(HistoryEntry entry)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(KEY_WORD, entry.getWord());
        cv.put(KEY_LIFES_REMAINING, entry.getLifesRemaining());
        cv.put(KEY_DATE, entry.getGameDate());

        db.update(SCORES_TABLE_NAME, cv, KEY_ID + "=?", new String[] { Integer.toString(entry.getId()) });

        db.close();
    }
}
