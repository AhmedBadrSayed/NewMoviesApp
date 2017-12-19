package com.projects.brightcreations.moviesappmvp.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ahmed Badr on 10/11/2016.
 */
public class MovieObject implements Parcelable {

    public String movieID;
    public String movieTittle;
    public String movieOverview;
    public String movieVoteAverage;
    public String moviePoster;
    public String movieBackdrop;
    public String movieDate;

    public MovieObject(String movieID, String movieTittle, String movieOverview, String movieVoteAverage, String moviePoster, String movieBackdrop, String movieDate) {
        this.movieID = movieID;
        this.movieTittle = movieTittle;
        this.movieOverview = movieOverview;
        this.movieVoteAverage = movieVoteAverage;
        this.moviePoster = moviePoster;
        this.movieBackdrop = movieBackdrop;
        this.movieDate = movieDate;
    }

    protected MovieObject(Parcel in) {
        movieID = in.readString();
        movieTittle = in.readString();
        movieOverview = in.readString();
        movieVoteAverage = in.readString();
        moviePoster = in.readString();
        movieBackdrop = in.readString();
        movieDate = in.readString();
    }

    public static final Creator<MovieObject> CREATOR = new Creator<MovieObject>() {
        @Override
        public MovieObject createFromParcel(Parcel in) {
            return new MovieObject(in);
        }

        @Override
        public MovieObject[] newArray(int size) {
            return new MovieObject[size];
        }
    };

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getMovieTittle() {
        return movieTittle;
    }

    public void setMovieTittle(String movieTittle) {
        this.movieTittle = movieTittle;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public String getMovieVoteAverage() {
        return movieVoteAverage;
    }

    public void setMovieVoteAverage(String movieVoteAverage) {
        this.movieVoteAverage = movieVoteAverage;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMovieBackdrop() {
        return movieBackdrop;
    }

    public void setMovieBackdrop(String movieBackdrop) {
        this.movieBackdrop = movieBackdrop;
    }

    public String getMovieDate() {
        return movieDate;
    }

    public void setMovieDate(String movieDate) {
        this.movieDate = movieDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieID);
        dest.writeString(movieTittle);
        dest.writeString(movieOverview);
        dest.writeString(movieVoteAverage);
        dest.writeString(moviePoster);
        dest.writeString(movieBackdrop);
        dest.writeString(movieDate);
    }
}
