package com.projects.brightcreations.moviesappmvp.local_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class MoviesDB {

    private SQLiteDatabase db;

    public MoviesDB(Context context){
        MoviesDbHelper helper = new MoviesDbHelper(context);
        db = helper.getWritableDatabase();
    }

    public void addMovie(String id, String tittle, String over_view , String vote_average, String date,
                         String poster_path, String backdrop_path) {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.COLUMN_MOVIE_ID, id);
        values.put(MoviesContract.COLUMN_MOVIE_TITTLE, tittle);
        values.put(MoviesContract.COLUMN_MOVIE_OVERVIEW, over_view);
        values.put(MoviesContract.COLUMN_MOVIE_VOTE_AVERAGE, vote_average);
        values.put(MoviesContract.COLUMN_MOVIE_DATE, date);
        values.put(MoviesContract.COLUMN_MOVIE_POSTERPATH, poster_path);
        values.put(MoviesContract.COLUMN_MOVIE_BACKDROP_PATH, backdrop_path);

        // ask the database object to insert the new data
        try{
            db.insert(MoviesContract.TABLE_NAME, null, values);
            Log.e("DB SUCCESS:  ", "success");
        } catch(Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public ArrayList<Object> getOneMovie(long ID) {
        ArrayList<Object> movieArray = new ArrayList<Object>();
        Cursor cursor;

        cursor = db.query(
                MoviesContract.TABLE_NAME,
                new String[] {MoviesContract.COLUMN_MOVIE_ID, MoviesContract.COLUMN_MOVIE_TITTLE,
                        MoviesContract.COLUMN_MOVIE_OVERVIEW, MoviesContract.COLUMN_MOVIE_VOTE_AVERAGE,
                        MoviesContract.COLUMN_MOVIE_DATE, MoviesContract.COLUMN_MOVIE_POSTERPATH,
                        MoviesContract.COLUMN_MOVIE_BACKDROP_PATH},
                        MoviesContract.COLUMN_MOVIE_ID + "=" + ID,
                null, null, null, null, null
        );

        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            do {
                movieArray.add(cursor.getLong(0));
                movieArray.add(cursor.getString(1));
                movieArray.add(cursor.getString(2));
                movieArray.add(cursor.getString(3));
                movieArray.add(cursor.getString(4));
                movieArray.add(cursor.getString(5));
                movieArray.add(cursor.getString(6));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return movieArray;
    }

    public ArrayList<ArrayList<Object>> getAllMovies() {
        ArrayList<ArrayList<Object>> AllMovies = new ArrayList<>();

        Cursor cursor;

        cursor = db.query(
                MoviesContract.TABLE_NAME,
                new String[] {MoviesContract.COLUMN_MOVIE_ID, MoviesContract.COLUMN_MOVIE_TITTLE,
                        MoviesContract.COLUMN_MOVIE_OVERVIEW, MoviesContract.COLUMN_MOVIE_VOTE_AVERAGE,
                        MoviesContract.COLUMN_MOVIE_DATE, MoviesContract.COLUMN_MOVIE_POSTERPATH,
                        MoviesContract.COLUMN_MOVIE_BACKDROP_PATH},
                null, null, null, null, null
        );

        cursor.moveToFirst();

        if (!cursor.isAfterLast()){
            do {
                ArrayList<Object> Movie = new ArrayList<Object>();
                Movie.add(cursor.getString(0));
                Movie.add(cursor.getString(1));
                Movie.add(cursor.getString(2));
                Movie.add(cursor.getString(3));
                Movie.add(cursor.getString(4));
                Movie.add(cursor.getString(5));
                Movie.add(cursor.getString(6));
                AllMovies.add(Movie);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return AllMovies;
    }

    public boolean isInDataBase(String movieName){
        ArrayList<ArrayList<Object>> AllMovies = new ArrayList<ArrayList<Object>>();

        Cursor cursor;

        cursor = db.query(
                MoviesContract.TABLE_NAME,
                new String[] {MoviesContract.COLUMN_MOVIE_ID, MoviesContract.COLUMN_MOVIE_TITTLE,
                        MoviesContract.COLUMN_MOVIE_OVERVIEW, MoviesContract.COLUMN_MOVIE_VOTE_AVERAGE,
                        MoviesContract.COLUMN_MOVIE_DATE, MoviesContract.COLUMN_MOVIE_POSTERPATH,
                        MoviesContract.COLUMN_MOVIE_BACKDROP_PATH},
                null, null, null, null, null
        );

        cursor.moveToFirst();

        if (!cursor.isAfterLast()){
            do {
                ArrayList<Object> Movie = new ArrayList<Object>();
                Movie.add(cursor.getLong(0));
                Movie.add(cursor.getString(1));
                Movie.add(cursor.getString(2));
                Movie.add(cursor.getString(3));
                Movie.add(cursor.getString(4));
                Movie.add(cursor.getString(5));
                AllMovies.add(Movie);
            }
            while (cursor.moveToNext());
        }

        for(int i=0 ; i<AllMovies.size() ; i++){
            if ( AllMovies.get(i).get(1).equals(movieName) ){
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public void deleteMovie(String  movieName){

        String where = MoviesContract.COLUMN_MOVIE_TITTLE +"='"+movieName+"'";
        db.delete(MoviesContract.TABLE_NAME, where, null);
    }

}
