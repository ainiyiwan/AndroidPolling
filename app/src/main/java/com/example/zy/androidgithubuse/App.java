package com.example.zy.androidgithubuse;

import android.app.Application;

import com.evernote.android.job.JobManager;

/**
 * @author rwondratschek
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        JobManager.create(this).addJobCreator(new DemoJobCreator());
    }
}

