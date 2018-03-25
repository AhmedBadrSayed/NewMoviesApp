package com.projects.brightcreations.moviesappmvp.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.projects.brightcreations.moviesappmvp.interfaces.ActivityController;

/**
 * @author ahmed on 22/03/18.
 */

public class Callbacks implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity instanceof ActivityController){
            activity.setContentView(layoutId(activity));
            init(activity);
        }
    }

    private int layoutId(Activity activity){
        return ((ActivityController) activity).getLayoutID();
    }

    private void init(Activity activity){
        ((ActivityController) activity).init();
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
