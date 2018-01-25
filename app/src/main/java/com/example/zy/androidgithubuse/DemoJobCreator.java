package com.example.zy.androidgithubuse;

import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;
import com.orhanobut.logger.Logger;

/**
 * @author rwondratschek
 */
public class DemoJobCreator implements JobCreator {

    public DemoJobCreator() {
        Logger.d("DemoJobCreator() 创建DemoJobCreator\n"+"时间："+DateUtil.getDateToString());
    }

    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case DemoSyncJob.TAG:
                Logger.d("DemoJobCreator.create() 执行DemoJobCreator的create方法\n"+"时间："+DateUtil.getDateToString());
                return new DemoSyncJob();
            default:
                return null;
        }
    }

    public static final class AddReceiver extends AddJobCreatorReceiver {
        @Override
        protected void addJobCreator(@NonNull Context context, @NonNull JobManager manager) {
            // manager.addJobCreator(new DemoJobCreator());
        }
    }
}
