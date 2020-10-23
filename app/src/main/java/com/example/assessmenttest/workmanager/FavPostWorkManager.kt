package com.example.assessmenttest.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.assessmenttest.BuildConfig
import com.example.assessmenttest.R
import com.example.assessmenttest.apis.PostsApis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class FavPostWorkManager(
    private val mContext: Context,
    workerParameters: WorkerParameters
) :
    Worker(mContext, workerParameters) {

    private val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun doWork(): Result {
        Log.d("AndroidVille", Thread.currentThread().toString())
        displayNotification("getting comments")
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
//                postsApis.getComments(1.toString())
                var response = provideUseApi().getComments("1")
                if (response.isSuccessful) {
                    displayNotification("comments downloaded")
                    Thread.sleep(5000L)
                    notificationManager.cancel(notificationId)
                }
                Log.d("WorkManager", "Worked")
            }
        }


        return Result.success()
    }

    private val notificationId: Int = 500
    private val notificationChannel: String = "AssessmentTest"

    private fun displayNotification(workType: String) {
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
        remoteView.setTextViewText(R.id.tv_notif_work, workType)


        notificationBuilder
            .setContent(remoteView)
            .setSmallIcon(R.mipmap.ic_launcher)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    override fun onStopped() {
        super.onStopped()
        notificationManager.cancel(notificationId)
    }


    val connectTimeout: Long = 40// 20s
    val readTimeout: Long = 40 // 20s

    fun provideUseApi(): PostsApis {
        var retrofit = provideRetrofit()
        return retrofit.create(PostsApis::class.java)
    }

    fun provideHttpClient(): OkHttpClient {
//        val okHttpClientBuilder = OkHttpClient.Builder()
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        okHttpClientBuilder.build()
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(): Retrofit {
        var client = provideHttpClient()
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }
}