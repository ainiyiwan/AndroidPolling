# AndroidPolling
Android轮询最佳实践

## 代码来源 https://github.com/evernote/android-job
### 为什么选择这个项目，请看[这里](https://github.com/firebase/firebase-jobdispatcher-android#user-content-firebase-jobdispatcher-)

### Comparison to other libraries

Library                    | Minimum API | Requires Google Play   | Service API<sup>[1](#fn1)</sup> | Custom retry strategies
-------------------------- | ----------- | ---------------------- | ------------------------------- | -----------------------
Framework [JobScheduler][] | 21          | No                     | JobScheduler                    | Yes
Firebase JobDispatcher     | 14          | Yes                    | JobScheduler                    | Yes
[evernote/android-job][]   | 14          | No<sup>[2](#fn2)</sup> | Custom                          | Yes

<sup><a name="fn1">1</a></sup> Refers to the methods that need to be implemented in the
Service subclass.<br>
<sup><a name="fn2">2</a></sup> Uses AlarmManager to support API levels <= 21 if Google
Play services is unavailable.<br>
### 一些注释，只写自己认为重要的，详细注释请下载源码

```java
public enum NetworkType {
        /**
         * Network does not have to be connected.联网与否都可以
         */
        ANY,
        /**
         * Network must be connected.必须联网
         */
        CONNECTED,
        /**
         * Network must be connected and unmetered.不被计量，比如WiFi
         */
        UNMETERED,
        /**
         * Network must be connected and not roaming, but can be metered.不是漫游
         */
        NOT_ROAMING,
        /**
         * This job requires metered connectivity such as most cellular data networks.计量网络，3G,4G等蜂窝数据网络
         */
        METERED
    }
```
#### setRequirementsEnforced(true)

##### Why does my job run while the device is offline, although I've requested a network connection?
 
 That's expected. The job should run once during a period or within the specified execution window. The timing is a higher requirement than the network type, which is more like a hint when it's best to run your job.To make sure that all requirements are met, you can call `.setRequirementsEnforced(true)`. This will make sure that your job won't run, if one check fails, e.g.
 
 ```java
 new JobRequest.Builder(DemoSyncJob.TAG)
         .setExecutionWindow(60_000L, 90_000L)
         .setRequiresCharging(true)
         .setRequiredNetworkType(JobRequest.NetworkType.UNMETERED)
         .setRequirementsEnforced(true)
         .build()
         .schedule();
 ```
 上面内容来自[这里](https://github.com/evernote/android-job/wiki/FAQ)
 
 问题：尽管我设置了要求网络连接，为什么我的job在没网的情况下运行？
 
 回答的大致就是，到时间就运行，这是预期的，对于是否运行job,时间这个节点设置比网络类型更重要，如果说你要求所有的执行按照你的设置进行，那么设置.setRequirementsEnforced(true)