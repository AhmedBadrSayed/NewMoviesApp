package com.projects.brightcreations.moviesappmvp;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.projects.brightcreations.moviesappmvp.Models.RealmMovie;
import com.projects.brightcreations.moviesappmvp.Screens.MoviesPosters.MoviesActivity;
import com.projects.brightcreations.moviesappmvp.Utilities.SharedPreferenceHelper;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private SharedPreferenceHelper sharedPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, MoviesActivity.class));
        sharedPreferenceHelper = new SharedPreferenceHelper(this);
        sharedPreferenceHelper.putPref(SharedPreferenceHelper.TOP_CURRENT_PAGE,1);
        sharedPreferenceHelper.putPref(SharedPreferenceHelper.POPULAR_CURRENT_PAGE,1);
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(RealmMovie.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        realm.beginTransaction();
    }
}
