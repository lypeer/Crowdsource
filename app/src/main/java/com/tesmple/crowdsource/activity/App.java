package com.tesmple.crowdsource.activity;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 *
 * Created by lypeer on 10/7/2015.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this , "ToU9po43RDw6nyqcjzPL57si" , "GiI6qViVwvAsCpz46SjLarm2");
    }
}
