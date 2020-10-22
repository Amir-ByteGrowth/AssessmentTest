package com.example.assessmenttest.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.assessmenttest.R
import com.example.assessmenttest.apis.PostsApis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavPostWorkManager(
    private val mContext: Context,
    workerParameters: WorkerParameters
) :
    Worker(mContext, workerParameters) {

    private val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun doWork(): Result {
        Log.d("AndroidVille", Thread.currentThread().toString())
        displayNotification()
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
//                postsApis.getComments(1.toString())
                Log.d("WorkManager","Worked")
            }
        }


        return Result.success()
    }

    private val notificationId: Int = 500
    private val notificationChannel: String = "AssessmentTest"

    private fun displayNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannel,
                notificationChannel,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(false)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, notificationChannel)

        val remoteView = RemoteViews(applicationContext.packageName, R.layout.custom_notif)
        remoteView.setImageViewResource(R.id.iv_notif, R.mipmap.ic_launcher)


        notificationBuilder
            .setContent(remoteView)
            .setSmallIcon(R.mipmap.ic_launcher)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    override fun onStopped() {
        super.onStopped()
        notificationManager.cancel(notificationId)
    }
}