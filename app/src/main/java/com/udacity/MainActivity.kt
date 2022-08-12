package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private var URL = ""
    private var projectName=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))


        custom_button.setOnClickListener {

            if (rb_1.isChecked == false && rb_2.isChecked == false && rb_3.isChecked == false) {
                Toast.makeText(this, "Please select file", Toast.LENGTH_SHORT).show()
            } else {

                setDownloadURL()
                 download()
                custom_button.buttonState = ButtonState.Loading
              //  createNotifications(file.fileDescriptor.toString(), "successful")
            }

        }
    }
    private fun setDownloadURL() {
        if (rb_1.isChecked) {
            URL = "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
            projectName="Glide Project"
        }
        if (rb_2.isChecked){
            projectName="Load app project"
            URL = "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        }
        if(rb_3.isChecked) {
            projectName="Retrofit Project"
            URL = "https://github.com/square/retrofit/archive/refs/heads/master.zip"
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            custom_button.buttonState=ButtonState.Completed
            Log.d("AAAAAAAAAA",intent!!.data.toString())
            Log.d("AAAAAAAAAA",intent!!.extras.toString())


            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val query = DownloadManager.Query()
            //val file=downloadManager.openDownloadedFile(id!!)
            //Log.d("AAAAAAAAAA",file.fileDescriptor.toString())

            query.setFilterById(id!!)

            val cursor = downloadManager.query(query)

//            Log.d("AAAAAAAAAAB",cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE)))

            if(cursor.moveToFirst()){
                val status=cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if(status==DownloadManager.STATUS_FAILED){
                    createNotifications("Faild")
                }else if(status==DownloadManager.STATUS_SUCCESSFUL){
                    createNotifications("successful")
                }
            }

        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request) // enqueue puts the download request in the queue.

    }


    private fun createNotifications(status: String) {

        createChannel("channel_id", "notification_channel")

        val contentIntent = Intent(applicationContext, DetailActivity::class.java)
        contentIntent.putExtra("status",status)
        contentIntent.putExtra("file_name",projectName)
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            123,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(
            applicationContext,
            "channel_id"
        )
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
          //  .setContentIntent(contentPendingIntent)
            .setContentText("${projectName} is downloaded")
            .setAutoCancel(true)
            .addAction(
                R.drawable.abc_vector_test,
                "check the status",
                contentPendingIntent
            )

        val notificationManager = ContextCompat.getSystemService(
                applicationContext,
        NotificationManager::class.java
        ) as NotificationManager
        notificationManager.notify(123,builder.build())
   //     custom_button.buttonState=ButtonState.Completed
    }

    private fun createChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Completed"
            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }
    companion object {

        private const val CHANNEL_ID = "channelId"
    }

}
