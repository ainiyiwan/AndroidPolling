package com.example.zy.androidgithubuse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.evernote.android.job.JobApi;
import com.evernote.android.job.JobConfig;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.demo.R;
import com.evernote.android.job.util.support.PersistableBundleCompat;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

/**
 * @author rwondratschek
 */
public class MainActivity extends Activity {

    private static final String LAST_JOB_ID = "LAST_JOB_ID";

    private int mLastJobId;

    private CompoundButton mRequiresCharging;
    private CompoundButton mRequiresDeviceIdle;
    private Spinner mNetworkTypeSpinner;

    private JobManager mJobManager;

    //have seen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mJobManager = JobManager.instance();

        //have seen
        if (savedInstanceState != null) {
            mLastJobId = savedInstanceState.getInt(LAST_JOB_ID, 0);
        }

        //have seen
        CompoundButton enableGcm = findViewById(R.id.enable_gcm);
        mRequiresCharging = findViewById(R.id.check_requires_charging);
        mRequiresDeviceIdle = findViewById(R.id.check_requires_device_idle);
        mNetworkTypeSpinner = findViewById(R.id.spinner_network_type);

        //have seen
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getNetworkTypesAsString());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNetworkTypeSpinner.setAdapter(adapter);

        //have seen
        enableGcm.setChecked(JobConfig.isApiEnabled(JobApi.GCM));
        enableGcm.setEnabled(JobApi.GCM.isSupported(this));
        enableGcm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                JobConfig.setApiEnabled(JobApi.GCM, isChecked);
            }
        });
    }

    //have seen
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LAST_JOB_ID, mLastJobId);
    }

    //have seen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    //have seen
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (JobApi.V_26.isSupported(this)) {
            menu.findItem(R.id.action_force_26).setChecked(false);
        } else {
            menu.findItem(R.id.action_force_26).setVisible(false);
        }
        if (JobApi.V_24.isSupported(this)) {
            menu.findItem(R.id.action_force_24).setChecked(false);
        } else {
            menu.findItem(R.id.action_force_24).setVisible(false);
        }
        if (JobApi.V_21.isSupported(this)) {
            menu.findItem(R.id.action_force_21).setChecked(false);
        } else {
            menu.findItem(R.id.action_force_21).setVisible(false);
        }
        if (JobApi.V_19.isSupported(this)) {
            menu.findItem(R.id.action_force_19).setChecked(false);
        } else {
            menu.findItem(R.id.action_force_19).setVisible(false);
        }
        if (JobApi.V_14.isSupported(this)) {
            menu.findItem(R.id.action_force_14).setChecked(false);
        } else {
            menu.findItem(R.id.action_force_14).setVisible(false);
        }
        if (JobApi.GCM.isSupported(this)) {
            menu.findItem(R.id.action_force_gcm).setChecked(false);
        } else {
            menu.findItem(R.id.action_force_gcm).setVisible(false);
        }

        switch (JobApi.getDefault(this)) {
            case V_26:
                menu.findItem(R.id.action_force_26).setChecked(true);
                break;
            case V_24:
                menu.findItem(R.id.action_force_24).setChecked(true);
                break;
            case V_21:
                menu.findItem(R.id.action_force_21).setChecked(true);
                break;
            case V_19:
                menu.findItem(R.id.action_force_19).setChecked(true);
                break;
            case V_14:
                menu.findItem(R.id.action_force_14).setChecked(true);
                break;
            case GCM:
                menu.findItem(R.id.action_force_gcm).setChecked(true);
                break;
            default:
                throw new IllegalStateException("not implemented");
        }

        return true;
    }

    //have seen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_force_26:
                JobConfig.forceApi(JobApi.V_26);
                return true;
            case R.id.action_force_24:
                JobConfig.forceApi(JobApi.V_24);
                return true;
            case R.id.action_force_21:
                JobConfig.forceApi(JobApi.V_21);
                return true;
            case R.id.action_force_19:
                JobConfig.forceApi(JobApi.V_19);
                return true;
            case R.id.action_force_14:
                JobConfig.forceApi(JobApi.V_14);
                return true;
            case R.id.action_force_gcm:
                JobConfig.forceApi(JobApi.GCM);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //have seen
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_simple:
                testSimple();
                break;

            case R.id.button_all_impl:
                testAllImpl();
                break;

            case R.id.button_periodic:
                testPeriodic();
                break;

            case R.id.button_cancel_last:
                testCancelLast();
                break;

            case R.id.button_cancel_all:
                testCancelAll();
                break;

            case R.id.button_exact:
                testExact();
                break;

            case R.id.button_sync_history:
                startActivity(new Intent(this, SyncHistoryActivity.class));
                break;
        }
    }

    private void testSimple() {
        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putString("key", "Hello world");

        mLastJobId = new JobRequest.Builder(DemoSyncJob.TAG)
                //设置展示时间3-4秒，
                .setExecutionWindow(3_000L, 4_000L)
                //设置失败重试时间，默认30s,backoff = numFailures * initial_backoff.线性增长
                .setBackoffCriteria(5_000L, JobRequest.BackoffPolicy.LINEAR)
                //设置任务执行时是否要求充电状态
                .setRequiresCharging(mRequiresCharging.isChecked())
                //手机是否处于闲置状态，这时候可以进行比较繁重任务，默认false
                .setRequiresDeviceIdle(mRequiresDeviceIdle.isChecked())
                //要求网络类型
                .setRequiredNetworkType(JobRequest.NetworkType.values()[mNetworkTypeSpinner.getSelectedItemPosition()])
                .setExtras(extras)
                .setRequirementsEnforced(true)
                .build()
                .schedule();
        Logger.d("testSimple() 执行MainActivity的testSimple()方法\n"+"时间："+DateUtil.getDateToString());
    }

    private void testAllImpl() {
        for (JobApi api : JobApi.values()) {
            if (api.isSupported(this)) {
                JobConfig.forceApi(api);
                testSimple();
            } else {
                Logger.t("testAllImpl").d("testAllImpl() testAllImpl()方法\n"+"时间："+DateUtil.getDateToString()+"\n===========================\n"+String.format("%s is not supported", api));
                Log.w("Demo", String.format("%s is not supported", api));
            }
        }

        JobConfig.reset();
    }

    private void testPeriodic() {
        JobConfig.setAllowSmallerIntervalsForMarshmallow(true);
        Logger.d("时间间隔===="+TimeUnit.SECONDS.toMillis(10));
        Logger.d("testPeriodic() 执行MainActivity的testPeriodic()方法\n"+"时间："+DateUtil.getDateToString());
        mLastJobId = new JobRequest.Builder(DemoSyncJob.TAG)
                .setPeriodic(TimeUnit.SECONDS.toMillis(60), TimeUnit.SECONDS.toMillis(30))
                .setRequiresCharging(mRequiresCharging.isChecked())
                .setRequiresDeviceIdle(mRequiresDeviceIdle.isChecked())
                .setRequiredNetworkType(JobRequest.NetworkType.values()[mNetworkTypeSpinner.getSelectedItemPosition()])
                .build()
                .schedule();

    }

    private void testExact() {
        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putString("key", "Hello world");

        mLastJobId = new JobRequest.Builder(DemoSyncJob.TAG)
                .setBackoffCriteria(5_000L, JobRequest.BackoffPolicy.EXPONENTIAL)
                .setExtras(extras)
                .setExact(10_000L)
                .build()
                .schedule();
    }

    private void testCancelLast() {
        mJobManager.cancel(mLastJobId);
    }

    private void testCancelAll() {
        mJobManager.cancelAll();
    }

    //have seen
    private String[] getNetworkTypesAsString() {
        String[] result = new String[JobRequest.NetworkType.values().length];
        for (int i = 0; i < JobRequest.NetworkType.values().length; i++) {
            result[i] = JobRequest.NetworkType.values()[i].toString();
        }
        return result;
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        testCancelAll();
//    }
}
