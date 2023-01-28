package com.cc.blockscreenusage

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.cc.blockscreenusage.SavedPreference.getDeviceLockStatus
import com.cc.blockscreenusage.SavedPreference.setDeviceLockStatus
import com.cc.blockscreenusage.databinding.ActivityMainBinding
import com.rvalerio.fgchecker.Utils.hasUsageStatsPermission
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
   // lateinit var mUsageStatsManager: UsageStatsManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: MyAdapter
    private var usageStats: MutableList<UsageStats> = arrayListOf()

//
//    companion object {
//        val FACEBOOK_COUNTER = "Facebook counter"
//        val WHATSAPP_COUNTER = "WhatsApp counter"
//    }

//    lateinit var facebook_view: TextView
//    lateinit var whatsapp_view: TextView

//    val requestPermissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted: Boolean ->
//            if (isGranted) {
//                // Permission is granted. Continue the action or workflow in your
//                // app.
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                    startForegroundService(Intent(application, FloatService::class.java))
//                } else {
//                    startService(Intent(application, FloatService::class.java))
//                }
//
//            } else {
//                // Explain to the user that the feature is unavailable because the
//                // features requires a permission that the user has denied. At the
//                // same time, respect the user's decision. Don't link to system
//                // settings in an effort to convince the user to change their
//                // decision.
//                Toast.makeText(this, "please give access", Toast.LENGTH_SHORT).show()
//
//            }
//        }


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        // setAdapter()

        if (!hasUsageStatsPermission(this)) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Permissions Needed")
                .setMessage("UnHook Requires Usage Data Access Permission. Grant these permissions on the next screen.")
                .setPositiveButton("OK") { dialog, id ->
                    dialog.dismiss()
                    startActivityForResult(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), 1)
                }
            builder.show()
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Permissions Needed")
                .setMessage("UnHook requires Screen Overlay Permission. Grant these permissions on the next screen.")
                .setPositiveButton("OK") { dialog, id ->
                    dialog.dismiss()
                    startActivityForResult(
                        Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION),
                        2
                    )
                }
            builder.show()
        } else {
            if(!isMyServiceRunning(FloatService::class.java)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    startForegroundService(Intent(application, FloatService::class.java))
                } else {
                    startService(Intent(application, FloatService::class.java))
                }
            }
        }

//        if (checkUsageStatsPermission()) {
//            // Implement further app logic here ...
//            Toast.makeText(this, "permission allowed", Toast.LENGTH_SHORT).show()
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                startForegroundService(Intent(application, FloatService::class.java))
//            } else {
//                startService(Intent(application, FloatService::class.java))
//            }
//        } else {
//            Toast.makeText(this, "permission not allowed", Toast.LENGTH_SHORT).show()
//            // Navigate the user to the permission settings
//             requestOverlayPermissionDialog()
//
//            if (checkUsageStatsPermission()) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                    startForegroundService(Intent(application, FloatService::class.java))
//                } else {
//                    startService(Intent(application, FloatService::class.java))
//                }
//            } else {
//                Toast.makeText(this, "please give access", Toast.LENGTH_SHORT).show()
//            }
//
//        }




        binding.button.setOnClickListener {
            val targetUsage = (TimeUnit.HOURS.toMillis((0).toLong())
                    + TimeUnit.MINUTES.toMillis((1).toLong()))
            binding.blockTime.text = "UnHook Time 1 Minute!"
            SavedPreference.setTargetTime(this, targetUsage)
            binding.button.setBackgroundColor(getColor(R.color.purple_700))

            binding.button2.setBackgroundColor(getColor(R.color.purple_500))
            binding.button3.setBackgroundColor(getColor(R.color.purple_500))

           finishAffinity()
        }
        binding.button2.setOnClickListener {
            val targetUsage = (TimeUnit.HOURS.toMillis((0).toLong())
                    + TimeUnit.MINUTES.toMillis((10).toLong()))
            binding.blockTime.text = "UnHook Time 5 Minute!"
            SavedPreference.setTargetTime(this, targetUsage)
            binding.button2.setBackgroundColor(getColor(R.color.purple_700))

            binding.button.setBackgroundColor(getColor(R.color.purple_500))
            binding.button3.setBackgroundColor(getColor(R.color.purple_500))

            finishAffinity()
        }
        binding.button3.setOnClickListener {
            val targetUsage = (TimeUnit.HOURS.toMillis((0).toLong())
                    + TimeUnit.MINUTES.toMillis((45).toLong()))
            binding.blockTime.text = "UnHook Time 10 Minute!"
            SavedPreference.setTargetTime(this, targetUsage)
            binding.button3.setBackgroundColor(getColor(R.color.purple_700))

            binding.button2.setBackgroundColor(getColor(R.color.purple_500))
            binding.button.setBackgroundColor(getColor(R.color.purple_500))

            finishAffinity()
        }

        binding.blockTime.setOnClickListener {
            SavedPreference.clearPreferences(this@MainActivity)
        }

//        val targetTime = SavedPreference.getTargetTime(this)
//        if(targetTime!!.toInt() != 0){
//            if(targetTime.toInt() == 10000){
//
//            }else
//        }


//        val updateView = object : TimerTask() {
//            @RequiresApi(Build.VERSION_CODES.O)
//            override fun run() {
//                runOnUiThread {
//                    //Retrieve the values
//
////                    GlobalData.list?.let {
////                        val total = Duration.ofMillis(it.map { it.totalTimeInForeground }.sum())
////                        Log.v("TAG6", "main app :${total.toMinutes()}")
////                        binding.totalUsageTime.text = "YOU SPENT ${total.toMinutes()} mins."
////                        mAdapter.updateAdapter(it as ArrayList<UsageStats>)
////                    }
//                }
//            }
//
//        }
//
//        Timer().scheduleAtFixedRate(updateView, 0, 3000)


    }


    // The `PACKAGE_USAGE_STATS` permission is a not a runtime permission and hence cannot be
// requested directly using `ActivityCompat.requestPermissions`. All special permissions
// are handled by `AppOpsManager`.
    private fun checkUsageStatsPermission(): Boolean {
        val appOpsManager = getSystemService(AppCompatActivity.APP_OPS_SERVICE) as AppOpsManager
        // `AppOpsManager.checkOpNoThrow` is deprecated from Android Q
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOpsManager.unsafeCheckOpNoThrow(
                "android:get_usage_stats",
                android.os.Process.myUid(), packageName
            )
        } else {
            appOpsManager.checkOpNoThrow(
                "android:get_usage_stats",
                android.os.Process.myUid(), packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (!hasUsageStatsPermission(this)) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Permissions Needed")
                    .setMessage("AppTimer has not been granted permissions and will now exit.")
                    .setPositiveButton(
                        "Exit"
                    ) { dialog, id -> finishAndRemoveTask() }
                builder.show()
            } else {
                recreate()
            }
        }
        if (requestCode == 2) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Permissions Needed")
                    .setMessage("AppTimer has not been granted permissions and will now exit.")
                    .setPositiveButton(
                        "Exit"
                    ) { dialog, id -> finishAndRemoveTask() }
                builder.show()
            } else {
                recreate()
            }
        }
    }

//    private fun setAdapter() {
//        mAdapter = MyAdapter(this, usageStats)
//        binding.recycler.layoutManager = LinearLayoutManager(this)
//        binding.recycler.adapter = mAdapter
//    }


    override fun onDestroy() {
//        stopService(Intent(application, FloatService::class.java))
//        SavedPreference.clearPreferences(this@MainActivity)
        super.onDestroy()
    }

    fun requestOverlayPermissionDialog() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage("To Use This App You have to provide Screen Overlay Permission!")
        dialogBuilder.setIcon(R.drawable.ic_notifications_24)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(this)
                }
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                finish()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("UnHook!")
        // show alert dialog
        alert.show()
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    @SuppressLint("ResourceAsColor")
    override fun onResume() {
        super.onResume()

        var targetTime = SavedPreference.getTargetTime(this@MainActivity)

        Log.e("TAG6", "target time :$targetTime")
        if (targetTime!!.toInt() != 0) {
            if(targetTime!!.toInt() ==  60000){
                binding.button.setBackgroundColor(getColor(R.color.purple_700))
                Log.e("TAG6", "target time 1:$targetTime")
            }else if(targetTime.toInt() == 600000){
                binding.button2.setBackgroundColor(getColor(R.color.purple_700))
                Log.e("TAG6", "target time 2:$targetTime")
            }else if(targetTime.toInt() == 2700000){
                binding.button3.setBackgroundColor(getColor(R.color.purple_700))
                Log.e("TAG6", "target time 3:$targetTime")
            }
//            binding.blockTime.text = "UnHook Time ${targetTime / 60000} Minute!"
//            if (SavedPreference.getUsageTime(application)!! >= targetTime) {
//                FloatService.totalUsageTime = 0;
//                SavedPreference.clearPreferences(this@MainActivity)
//                binding.blockTime.text = "UnHook Time is Completed,Set Time Again!"
//                if(targetTime!!.toInt() == 1 ){
//                    binding.button.setBackgroundColor(getColor(R.color.purple_700))
//                }else if(targetTime.toInt() == 10){
//                    binding.button2.setBackgroundColor(getColor(R.color.purple_700))
//                }else if(targetTime.toInt() == 45){
//                    binding.button3.setBackgroundColor(getColor(R.color.purple_700))
//                }
//
//                Log.e("TAG6", "greater time")
//            }
        } else {
            binding.blockTime.text = "UnHook Time Not Set Yet!"
            binding.button.setBackgroundColor(getColor(R.color.purple_500))
            binding.button2.setBackgroundColor(getColor(R.color.purple_500))
            binding.button3.setBackgroundColor(getColor(R.color.purple_500))
            Log.e("TAG6", "target time else :$targetTime")
        }
        // un lock
        if (getDeviceLockStatus(application)!!) {
            setDeviceLockStatus(application, false)
        }

//
//        if (checkUsageStatsPermission()) {
//        if(isMyServiceRunning(FloatService::class.java)){
//            Log.e("TAG6","Service is running alredy!")
//        }else{
//            Log.e("TAG6","Service is not started yet!")
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                startForegroundService(Intent(application, FloatService::class.java))
//            } else {
//                startService(Intent(application, FloatService::class.java))
//            }
//        }
//
//        } else {
//
//
//        }

    }

    override fun onBackPressed() {
        var targetTime = SavedPreference.getTargetTime(this@MainActivity)
        if (targetTime!!.toInt() == 0) {
            Toast.makeText(this,"Please Select time!",Toast.LENGTH_LONG).show()
        }else{
            super.onBackPressed()
        }

    }





}