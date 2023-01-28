package com.cc.blockscreenusage;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.rvalerio.fgchecker.AppChecker;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FloatService extends Service {
    AppChecker appChecker;
    public static Long totalUsageTime = 0L;
    private static final int timeout = 500;
    private static final String CHANNEL_DEFAULT_IMPORTANCE = "BlockScreenUsage";
    private static final String CHANNEL_ID = "13";
    private static final int ONGOING_NOTIFICATION_ID = 14;

    KeyguardManager myKM;
    String launcherPackageName;


    @Override
    public void onCreate() {
        super.onCreate();
        myKM = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        PackageManager localPackageManager = getPackageManager();
        Intent inten = new Intent("android.intent.action.MAIN");
        inten.addCategory("android.intent.category.HOME");
        launcherPackageName = localPackageManager
                .resolveActivity(inten, PackageManager.MATCH_DEFAULT_ONLY)
                .activityInfo
                .packageName;
        SavedPreference.setLauncherPackageName(getApplication(),launcherPackageName);
        Log.e("Current launcher Package Name:", SavedPreference.getLauncherPackageName(getApplication()));
        appChecker = new AppChecker();
    }

    @Override
    public void onDestroy() {

//        appChecker.stop();
//        SavedPreference.clearPreferences(getApplication());
        Log.e("TAG6", "Service is going to destro!");
        super.onDestroy();
        appChecker.stop();


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "UnHook notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_DEFAULT_IMPORTANCE, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        createNotificationChannel();

        Notification notification =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("UnHook")
                        .setContentText("UnHook is running...")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentIntent(pendingIntent)
                        .setTicker("UnHook is running")
                        .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            startForeground(ONGOING_NOTIFICATION_ID, notification);
        } else {
            startService(intent);
        }

        appChecker.whenAny(new AppChecker.Listener() {
                    @Override
                    public void onForeground(String packageName) {
                        Long targetUsage = SavedPreference.getTargetTime(getApplication());
                        if (targetUsage != 0L) {

                            if (!packageName.equals(getApplicationContext().getPackageName()) && !packageName.equals(launcherPackageName)) {
                                totalUsageTime += 500;
                                Log.v("TAG6", "onForeground is running... :" + packageName + "total usage time :" + totalUsageTime + " target usage time :" + targetUsage);
                                if (totalUsageTime > targetUsage) {

                                    totalUsageTime = 0L;
                                    SavedPreference.setUsageTime(getApplication(),totalUsageTime);
                                    Intent intent = new Intent(getApplication(),AutoStart.class);
                                    intent.setAction("com.cc.blockscreenusage.restartMyActivity");
                                    sendBroadcast(intent);
                                    Log.v("TAG6", "onForeground is running...:196");
//                                    stopSelf();
                                }

                            } else {
                                Log.v("TAG6", "onForeground is running...else :" + packageName + "  " + targetUsage);
                            }
                        } else {
                            if (myKM.inKeyguardRestrictedInputMode()) {
                                // lock
                                Log.v("TAG6", "Target Time is 0 and device is lock");
                                SavedPreference.setDeviceLockStatus(getApplication(), true);

                            } else {
                                // un lock
                                if (SavedPreference.getDeviceLockStatus(getApplication())) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Log.v("TAG6", "Target Time is 0 and device is not lock take it to main activity to set time");
                                } else {
                                    Log.v("TAG6", "Target Time is 0 and device is not lock");
                                }

                            }
                        }
                    }
                })
                .timeout(timeout)
                .start(this);


        return START_STICKY;
    }


}
