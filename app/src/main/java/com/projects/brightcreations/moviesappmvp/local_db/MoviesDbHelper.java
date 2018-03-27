package com.projects.brightcreations.moviesappmvp.local_db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ahmed Badr for MoviesApp on 6/4/2016.
 */
class MoviesDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movies.db";

    MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + MoviesContract.TABLE_NAME + " (" +
                MoviesContract.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY," +
                MoviesContract.COLUMN_MOVIE_TITTLE + " TEXT NOT NULL, " +
                MoviesContract.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MoviesContract.COLUMN_MOVIE_VOTE_AVERAGE + " REAL NOT NULL, " +
                MoviesContract.COLUMN_MOVIE_DATE + " INTEGER NOT NULL, " +
                MoviesContract.COLUMN_MOVIE_POSTERPATH + " INTEGER NOT NULL, " +
                MoviesContract.COLUMN_MOVIE_BACKDROP_PATH + " INTEGER NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
}

