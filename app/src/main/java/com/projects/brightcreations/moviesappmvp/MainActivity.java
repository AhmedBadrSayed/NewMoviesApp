package com.projects.brightcreations.moviesappmvp;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import com.projects.brightcreations.moviesappmvp.models.MovieRealmObject;
import com.projects.brightcreations.moviesappmvp.screens.movies_posters.MoviesActivity;
import com.projects.brightcreations.moviesappmvp.interfaces.ActivityController;
import com.projects.brightcreations.moviesappmvp.utils.SharedPreferenceHelper;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements ActivityController {

    private Realm realm;

    @Override
    public void init() {
        startActivity(new Intent(this, MoviesActivity.class));
        SharedPreferenceHelper.init(this);
        SharedPreferenceHelper.getInstance().putPref(SharedPreferenceHelper.TOP_CURRENT_PAGE,1);
        SharedPreferenceHelper.getInstance().putPref(SharedPreferenceHelper.POPULAR_CURRENT_PAGE,1);
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(MovieRealmObject.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        realm.beginTransaction();
    }
}
