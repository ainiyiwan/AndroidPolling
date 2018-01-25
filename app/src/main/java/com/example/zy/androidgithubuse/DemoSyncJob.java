package com.example.zy.androidgithubuse;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.demo.R;
import com.evernote.android.job.util.support.PersistableBundleCompat;
import com.orhanobut.logger.Logger;

import java.util.Random;

/**
 * @author rwondratschek
 */
public class DemoSyncJob extends Job {

    public static final String TAG = "job_demo_tag";

    @Override
    @NonNull
    protected Result onRunJob(@NonNull final Params params) {
        boolean success = new DemoSyncEngine(getContext()).sync();
        Logger.d("DemoSyncJob.onRunJob() 执行DemoSyncJob的onRunJob方法\n"+"时间："+DateUtil.getDateToString());
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), MainActivity.class), 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(TAG, "Job Demo", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("Job demo job");
            getContext().getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(getContext(), TAG)
                .setContentTitle("ID " + params.getId())
                .setContentText("Job ran, exact " + params.isExact() + " , periodic " + params.isPeriodic() + ", transient " + params.isTransient())
                .setAutoCancel(true)
                .setChannelId(TAG)
                .setSound(null)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notification)
                .setShowWhen(true)
                .setColor(Color.GREEN)
                .setLocalOnly(true)
                .build();

        NotificationManagerCompat.from(getContext()).notify(new Random().nextInt(), notification);

        Logger.d("DemoSyncJob.onRunJob() 执行DemoSyncJob的onRunJob方法成功？=== "+success+"\n"+"时间："+DateUtil.getDateToString());

        Logger.d("DemoSyncJob.onRunJob() 下一轮结束喽--------------\n"+"时间："+DateUtil.getDateToString());

        Logger.d("DemoSyncJob.onRunJob() 下一轮开始喽++++++++++++++\n"+"时间："+DateUtil.getDateToString());

        return success ? Result.SUCCESS : Result.FAILURE;
    }

    private void testSimple() {
        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putString("key", "Hello world");

        int mLastJobId = new JobRequest.Builder(DemoSyncJob.TAG)
                //设置展示时间3-4秒，
                .setExecutionWindow(3_000L, 4_000L)
                //设置失败重试时间，默认30s,backoff = numFailures * initial_backoff.线性增长
                .setBackoffCriteria(5_000L, JobRequest.BackoffPolicy.LINEAR)
                //设置任务执行时是否要求充电状态
                .setRequiresCharging(false)
                //手机是否处于闲置状态，这时候可以进行比较繁重任务，默认false
                .setRequiresDeviceIdle(false)
                //要求网络类型
                .setRequiredNetworkType(JobRequest.NetworkType.ANY)
                .setExtras(extras)
                .setRequirementsEnforced(true)
                .build()
                .schedule();
        Logger.d("testSimple() 执行DemoSyncJob的testSimple()方法\n"+"时间："+DateUtil.getDateToString());
    }
}
