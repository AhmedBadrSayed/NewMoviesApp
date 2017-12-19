package com.projects.brightcreations.moviesappmvp;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Abubaker on 08/12/2016.
 */

public class Application extends android.app.Application {
    public static final String NAME = "Movies_realm";
    public static final int VERSION = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(NAME)
                .schemaVersion(VERSION).deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
    }

}
