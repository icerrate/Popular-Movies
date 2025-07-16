package com.icerrate.popularmovies.provider.db

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import com.icerrate.popularmovies.provider.db.MovieDBHelper.Companion.TABLE_FAVORITE

/**
 * @author Ivan Cerrate.
 */
class MovieContentProvider : ContentProvider() {

    companion object {
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    }

    private lateinit var movieDBHelper: MovieDBHelper

    override fun onCreate(): Boolean {
        movieDBHelper = MovieDBHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val db: SQLiteDatabase = movieDBHelper.readableDatabase
        return db.query(TABLE_FAVORITE, projection, selection, selectionArgs, null, null, sortOrder)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db: SQLiteDatabase = movieDBHelper.writableDatabase
        val id = db.insert(TABLE_FAVORITE, null, values)
        return Uri.parse("$TABLE_FAVORITE/$id")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db: SQLiteDatabase = movieDBHelper.writableDatabase
        return db.delete(TABLE_FAVORITE, selection, selectionArgs)
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val db: SQLiteDatabase = movieDBHelper.writableDatabase
        val id = uri.lastPathSegment
        return db.update(TABLE_FAVORITE, values, "_id=?", arrayOf(id!!))
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}