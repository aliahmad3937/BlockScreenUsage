package com.cc.blockscreenusage

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


object SavedPreference {

    const val TARGETTIME = "targettime"
    const val USAGETTIME = "usagetime"
    const val DEVICELOCK = "devicelock"
    const val PKGNAME = "pkgname"


    private fun getSharedPreference(ctx: Context?): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }


    private fun editor(context: Context, key: String, value: Long) {
        getSharedPreference(
            context
        )?.edit()?.putLong(key, value)?.apply()
    }

    private fun editor(context: Context, key: String, value: Boolean) {
        getSharedPreference(
            context
        )?.edit()?.putBoolean(key, value)?.apply()
    }


    private fun editor(context: Context, key: String, value: String) {
        getSharedPreference(
            context
        )?.edit()?.putString(key, value)?.apply()
    }


    @JvmStatic
    fun setTargetTime(context: Context, time: Long) {
        editor(
            context = context,
            key = TARGETTIME,
            value = time
        )
    }

    @JvmStatic
    public fun getTargetTime(context: Context) = getSharedPreference(
        context
    )?.getLong(TARGETTIME, 0)



    @JvmStatic
    fun setUsageTime(context: Context, time: Long) {
        editor(
            context = context,
            key = USAGETTIME,
            value = time
        )
    }

    @JvmStatic
    public fun getUsageTime(context: Context) = getSharedPreference(
        context
    )?.getLong(USAGETTIME, -1)

    @JvmStatic
    fun setDeviceLockStatus(context: Context, lock: Boolean) {
        editor(
            context = context,
            key = DEVICELOCK,
            value = lock
        )
    }

    @JvmStatic
    public fun getDeviceLockStatus(context: Context) = getSharedPreference(
        context
    )?.getBoolean(DEVICELOCK, false)

    @JvmStatic
    fun setLauncherPackageName(context: Context, lock: String) {
        editor(
            context = context,
            key = PKGNAME,
            value = lock
        )
    }

    @JvmStatic
    public fun getLauncherPackageName(context: Context) = getSharedPreference(
        context
    )?.getString(PKGNAME, "")



    @JvmStatic
    fun clearTargetTime(context: Context) {
        getSharedPreference(
            context
        )?.edit()?.remove(TARGETTIME)?.apply()
    }

    @JvmStatic
    fun clearDeviceLockStatus(context: Context) {
        getSharedPreference(
            context
        )?.edit()?.remove(DEVICELOCK)?.apply()
    }

    @JvmStatic
    fun clearPreferences(context: Context) {
        getSharedPreference(
            context
        )?.edit()?.clear()?.apply()
    }
}