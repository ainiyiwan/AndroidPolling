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

