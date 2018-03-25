package com.projects.brightcreations.moviesappmvp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by ahmed on 11/12/17.
 */

public class SharedPreferenceHelper extends PreferenceActivity {

    private static SharedPreferenceHelper sharedPreferenceHelper;

    //constants
    public static String LAST_CHOICE = "lastChoice";
    public static String TOP_CURRENT_PAGE = "topCurrentPage";
    public static String POPULAR_CURRENT_PAGE = "popularCurrentPage";

    SharedPreferences sharedPreferences;

    public SharedPreferenceHelper (Context context) {
        sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void init(Context context){
        if (sharedPreferenceHelper == null){
            sharedPreferenceHelper = new SharedPreferenceHelper(context);
        }
    }

    public static SharedPreferenceHelper getInstance(){
        if (sharedPreferenceHelper == null) {
            throw new IllegalArgumentException(
                    "SharedPreferenceHelper is not initialized! Call SharedPreferenceHelper.init() first.");
        }
        return sharedPreferenceHelper;
    }

    private SharedPreferences.Editor getEditor() {
        if (sharedPreferences != null)
            return sharedPreferences.edit();
        return null;
    }

    public int getPref(String key, int defValue) {
        if (sharedPreferences == null)
            return defValue;
        return sharedPreferences.getInt(key, defValue);
    }

    public void putPref(String key, int val) {
        SharedPreferences.Editor spe = getEditor();
        if (spe == null)
            return;
        spe.putInt(key, val);
        spe.commit();
    }

    public String getPref(String key, String defValue) {
        if (sharedPreferences == null)
            return defValue;
        return sharedPreferences.getString(key, defValue);
    }

    public void putPref(String key, String val) {
        SharedPreferences.Editor spe = getEditor();
        if (spe == null)
            return;
        spe.putString(key, val);
        spe.commit();
    }

    public void removePref(String key) {
        SharedPreferences.Editor spe = getEditor();
        if (spe == null)
            return;
        spe.remove(key);
        spe.commit();
    }

    public void clear() {
        SharedPreferences.Editor spe = getEditor();
        if (spe == null)
            return;
        spe.clear();
        spe.commit();
    }

}
