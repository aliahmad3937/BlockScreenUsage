package com.cc.blockscreenusage

import android.annotation.SuppressLint
import android.app.*
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import java.util.*
import java.util.concurrent.TimeUnit


class BackgroundServices : Service() {
    lateinit var sp: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private val CHANNEL_ID = "14"
    private val ONGOING_NOTIFICATION_ID = 15
    private val CHANNEL_DEFAULT_IMPORTANCE = "BlockScreenUsage"



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        createNotificationChannel()


        val notification: Notification =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("AppTimer")
                .setContentText("AppTimer is running.")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setTicker("AppTimer is running")
                .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            startForeground(ONGOING_NOTIFICATION_ID, notification)
        } else
            startService(intent)



//        sp = getSharedPreferences(getString(R.string.app_duration), MODE_PRIVATE)
//        editor = sp.edit()
//
//
////        val installApps = getNonSystemAppsList()
////        installApps.values.forEach {
////            Log.v( "APP" , "install app :$it" )
////        }
//
//        val beginTime = System.currentTimeMillis()
//
//        val detectApp = object : TimerTask() {
//            @SuppressLint("NewApi")
//            @RequiresApi(Build.VERSION_CODES.O)
//            override fun run() {
//                sp = getSharedPreferences(getString(R.string.app_duration), MODE_PRIVATE)
//                val powerManager = getSystemService(POWER_SERVICE) as PowerManager
//                editor = sp.edit()
//                var mUsageStatsManager = getSystemService(
//                    USAGE_STATS_SERVICE
//                ) as UsageStatsManager
//                val endTime = System.currentTimeMillis()
//                val beginTime = endTime - (1000 * 60 * 10)
//
//
//                val usageStats: List<UsageStats> = mUsageStatsManager.queryUsageStats(
//                    UsageStatsManager.INTERVAL_BEST,
//                    beginTime,
//                    endTime
//                )
//                GlobalData.list = usageStats
//
//                //  saveArrayList(usageStats,"STATS")
//
////                val total = Duration.ofMillis(usageStats.map {
////
////
////
////                    Log.v("TAG6","name :${it.packageName}")
////                    it.totalTimeInForeground
////                }.sum())
////                Log.e("TAG6", "detecting app ${usageStats.size}:${total.toMinutes()}")
//
//                usageStats.forEach {
//
//                    Log.e("TAG6", "package name :${it.packageName}")
//                    if (packageManager.getLaunchIntentForPackage(it.packageName) != null && powerManager.isInteractive
//                        && it.packageName != applicationContext.packageName
//                    ){
//                        Log.v("TAG6","increase time now :${it.packageName}")
//                    }
//                }
//                Log.v("TAG6", "one loop completed!")
//
//
////                usageStats.forEach {
////                    Log.v("TAG6", "detecting app ::totalTimeForeground :${it.totalTimeInForeground} totalTimeVisible :${it.totalTimeVisible} lastTimeVisible :${it.lastTimeVisible}  lastTimeUsed :${it.lastTimeUsed}  firstTimeStamp :${it.firstTimeStamp}  total time foreground :${it.totalTimeInForeground}")
////                }
////
//
//                // get next extraction time from settings
//
//                // get next extraction time from settings
//                val next_extract_time = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
//                //   val endTime = System.currentTimeMillis()
//
////                val usageEvents: UsageEvents = mUsageStatsManager.queryEvents(  beginTime, endTime )
////                val usageEvent = UsageEvents.Event()
////                var sum = 0L
////                val items = HashMap<String, Long>()
////                while ( usageEvents.hasNextEvent() ) {
////                    usageEvents.getNextEvent( usageEvent )
////                    Log.e( "APP" , "package name :${usageEvent.packageName}  timeStamp:${usageEvent.timeStamp}" )
////                    sum += usageEvent.timeStamp
////                    if (packageManager.getLaunchIntentForPackage(usageEvent.packageName) != null && powerManager.isInteractive
////                        && usageEvent.packageName != applicationContext.packageName
////                    ){
////                        Log.v("TAG6","increase time now :${usageEvent.packageName}")
////                    }
////
////                    items[usageEvent.packageName] = usageEvent.timeStamp
////                }
//////                for ((k, v) in items) {
//////                    if (packageManager.getLaunchIntentForPackage(k) != null && powerManager.isInteractive
//////                        && k != applicationContext.packageName
//////                    ){
//////                        Log.v("TAG6","increase time now :${k}")
//////                    }
//////                     Log.v( "APP" , "inside map  :$k = $v" )
//////                }
////                Log.v( "APP" , "one loop completed time  " )
//
//                //  val total = Duration.ofMillis(sum)
//                // Log.v( "APP" , "sum :$sum  ::one loop completed time  :${total.toMinutes()}" )
//
////                val start = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
////                val end = ZonedDateTime.now().toInstant().toEpochMilli()
////
////                val stats = mUsageStatsManager.queryAndAggregateUsageStats(startTime, endTime)
////
////                val total = Duration.ofMillis(stats.values.map { it.totalTimeInForeground }.sum())
////                val strin:String = "YOU SPENT ${total.toMinutes()} mins."
////                editor.putString("TASK", strin).apply()
//
//
//                //    Log.v("TAG6", "detecting app :$strin")
////                if (usageStats != null) {
////
////                    //Set the values
////                   saveArrayList(usageStats,"STATS")
//
////                    usageStats.forEach { usageStat ->
////                     //   Log.v("TAG6","package name :${usageStat.packageName}")
////                        if (usageStat.packageName.toLowerCase().contains("com.whatsapp")) {
////                         editor.putLong(MainActivity.WHATSAPP_COUNTER, usageStat.totalTimeInForeground).apply()
////                            Log.v("TAG6","package name :${usageStat.packageName}  used time :${usageStat.totalTimeInForeground}")
////                        }
////                        if (usageStat.packageName.toLowerCase().contains("com.facebook.katana")) {
////                            editor.putLong(MainActivity.FACEBOOK_COUNTER, usageStat.totalTimeInForeground).apply()
////                            Log.v("TAG6","package name :${usageStat.packageName}  used time :${usageStat.totalTimeInForeground}")
////                        }
////
////                    }
//
//
////                // Getting the instance of ActivityManager
////                val mActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
////
////                // Getting the tasks in the form of a list
////                val mRecentTasks: List<ActivityManager.RunningTaskInfo> = Objects.requireNonNull(mActivityManager).getRunningTasks(Int.MAX_VALUE)
////
////                var mString = ""
////
////                // Creating a string of each index of the list
////                for (i in mRecentTasks.indices){
////                    mString = mString + " " + mRecentTasks[i].baseActivity?.toShortString() + " " + mRecentTasks[i].id + "\n\n"
////                }
////
////                Log.v("TAG6",mString)
//
////                val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
////                val recentTasks = activityManager.runningAppProcesses
////
////                for (i in recentTasks.indices) {
////                    if (recentTasks[i].processName.equals("com.whatsapp"))   // make sure your package address of that specific app.
////                    {
////                        //Another App is running
////                        Log.v("TAG6", "whatApp is running...")
////                    }
////                }
//
//            }
//        }
//
//
//
//        Timer().scheduleAtFixedRate(detectApp, 0, 3000)

//        return super.onStartCommand(intent, flags, startId)
        return START_STICKY

    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    fun saveArrayList(list: List<UsageStats>, key: String?) {
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(key, json)
        editor.apply() // This line is IMPORTANT !!!


    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val description = "AppTimer notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_DEFAULT_IMPORTANCE,
                importance
            )
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Filtering user-installed apps
    private fun getNonSystemAppsList(): Map<String, String> {
        val appInfos = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val appInfoMap = HashMap<String, String>()
        for (appInfo in appInfos) {
            if (appInfo.flags != ApplicationInfo.FLAG_SYSTEM) {
                appInfoMap[appInfo.packageName] =
                    packageManager.getApplicationLabel(appInfo).toString()
            }
        }
        return appInfoMap
    }

//    fun isRunning(ctx: Context): Boolean {
//        val activityManager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        val tasks = activityManager.getRunningTasks(Int.MAX_VALUE)
//        for (task in tasks) {
//            if (ctx.packageName.equalsIgnoreCase(task.baseActivity!!.packageName)) return true
//        }
//        return false
//    }


    private fun isAppRunning(packageName: String): Boolean {


        val services =
            (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).runningAppProcesses
        return services.firstOrNull { it.processName.equals(packageName, true) } != null
    }
}