package com.projects.brightcreations.moviesappmvp.utils;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Ahmed Badr on 6/7/2017.
 */

public class BusProvider {

    public static Bus mBUS;

    /**
     * Returns an instance of Bus class
     */
    public static Bus getInstance(){

        if (mBUS == null) {
            mBUS = new Bus(ThreadEnforcer.ANY);
        }
        return mBUS;

    }
    private BusProvider(){

    }
}
