package com.buffersolve.jutloader

import android.app.Notification
import android.content.Context
import java.io.File


//class Service : DownloadService(
//    1,
//    1,
//) {
//
//    val JOB_ID = 1
//
//    val databaseProvider = StandaloneDatabaseProvider(this)
//
//    val downloadContentDirectory = File(getDownloadDirectory(this), DOWNLOAD_CONTENT_DIRECTORY)
//    val downloadCache = SimpleCache(
//        downloadContentDirectory,
//        NoOpCacheEvictor(),
//        databaseProvider
//    )
//    val dataSourceFactory = DefaultHttpDataSource.Factory()
//
//    val downloadExecutor = Runnable::run
//
//    override fun getDownloadManager(): DownloadManager {
//        return DownloadManager(
//            this,
//            databaseProvider,
//            downloadCache,
//            dataSourceFactory,
//            downloadExecutor
//        )
//
//    }
//
//    override fun getScheduler(): Scheduler? {
//        return if (Util.SDK_INT >= 21) PlatformScheduler(this, JOB_ID) else null
//    }
//
//    override fun getForegroundNotification(
//        downloads: MutableList<Download>,
//        notMetRequirements: Int
//    ): Notification {
//        return DownloadNotificationHelper(this, DOWNLOAD_NOTIFICATION_CHANNEL_ID)
//            .buildProgressNotification(
//                this,
//                R.drawable.ic_launcher_foreground,
//                null,
//                null,
//                downloads,
//                notMetRequirements
//
//            )
//
//    }
//
//}