package com.cc.blockscreenusage

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity

class AutoStart : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("TAG6", "Broadcast received :" + intent?.action)
        SavedPreference.clearPreferences(context!!)
        val intent1 = Intent(context, MainActivity::class.java)
        intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(intent1)
    }

}