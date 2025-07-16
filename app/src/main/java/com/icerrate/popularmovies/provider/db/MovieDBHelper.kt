package com.icerrate.popularmovies.provider.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.icerrate.popularmovies.BuildConfig

/**
 * @author Ivan Cerrate.
 */
class MovieDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "${BuildConfig.APPLICATION_ID}.favorites_db"
        private const val DATABASE_VERSION = 1

        const val TABLE_FAVORITE = "favorite"
        const val ID_COLUMN = "id"
        const val TITLE_COLUMN = "title"
        const val POSTER_COLUMN = "poster"
        const val BACKDROP_COLUMN = "backdrop"
        const val SYNOPSIS_COLUMN = "synopsis"
        const val VOTE_AVERAGE_COLUMN = "vote_average"
        const val RELEASE_DATE_COLUMN = "release_date"

        private const val SCRIPT_TABLE_FAVORITE = "CREATE TABLE IF NOT EXISTS $TABLE_FAVORITE (" +
                "$ID_COLUMN INT PRIMARY KEY, " +
                "$TITLE_COLUMN TEXT NULL, " +
                "$POSTER_COLUMN TEXT NULL, " +
                "$BACKDROP_COLUMN TEXT NULL, " +
                "$SYNOPSIS_COLUMN TEXT NULL, " +
                "$VOTE_AVERAGE_COLUMN REAL NULL, " +
                "$RELEASE_DATE_COLUMN REAL NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SCRIPT_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w(
            MovieDBHelper::class.java.name,
            "Upgrading database from version $oldVersion to $newVersion, which will destroy all old data"
        )
        db.execSQL("ALTER TABLE IF EXISTS $TABLE_FAVORITE")
        onCreate(db)
    }
}