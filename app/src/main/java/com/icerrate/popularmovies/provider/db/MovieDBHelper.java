package com.icerrate.popularmovies.provider.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.icerrate.popularmovies.BuildConfig;

/**
 * @author Ivan Cerrate.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = BuildConfig.APPLICATION_ID +".favorites_db";

    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_FAVORITE = "favorite";

    public static final String ID_COLUMN = "id";

    public static final String TITLE_COLUMN = "title";

    public static final String POSTER_COLUMN = "poster";

    public static final String BACKDROP_COLUMN = "backdrop";

    public static final String SYNOPSIS_COLUMN = "synopsis";

    public static final String VOTE_AVERAGE_COLUMN = "vote_average";

    public static final String RELEASE_DATE_COLUMN = "release_date";

    private static final String SCRIPT_TABLE_FAVORITE = "CREATE TABLE IF NOT EXISTS "+TABLE_FAVORITE+" ("
            + ID_COLUMN +" INT PRIMARY KEY, "
            + TITLE_COLUMN +" TEXT NULL, "
            + POSTER_COLUMN +" TEXT NULL, "
            + BACKDROP_COLUMN +" TEXT NULL, "
            + SYNOPSIS_COLUMN +" TEXT NULL, "
            + VOTE_AVERAGE_COLUMN +" REAL NULL, "
            + RELEASE_DATE_COLUMN +" REAL NULL)";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MovieDBHelper.class.getName(),
                "Upgrading database from version "
                        + oldVersion + " to " + newVersion
                        + ", which will destroy all old data");
        db.execSQL("ALTER TABLE IF EXISTS " + TABLE_FAVORITE);
        onCreate(db);
    }
}
