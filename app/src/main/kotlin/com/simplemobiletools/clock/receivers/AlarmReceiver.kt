package com.simplemobiletools.clock.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.simplemobiletools.clock.R
import com.simplemobiletools.clock.activities.ReminderActivity
import com.simplemobiletools.clock.extensions.*
import com.simplemobiletools.clock.helpers.ALARM_ID

class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra(ALARM_ID, -1)
        val alarm = context.dbHelper.getAlarmWithId(id)
        if(alarm!!.isChild) {
            Intent().also { intent ->
                intent.setAction("com.simplemobiletools.ALARM_GOING_TO_RING")
                intent.putExtra("hours", alarm?.timeInMinutes / 60)
                intent.putExtra("minutes", alarm?.timeInMinutes % 60)
                intent.putExtra("days", alarm?.days)
                intent.putExtra("id", alarm?.id)
                intent.putExtra("label", alarm?.label)
                context.sendBroadcast(intent)
                Log.i("SENT_BROADCAST", "ALARM_GOING_TO_RING")
            }
        }
        else {
            val alarm = context.dbHelper.getAlarmWithId(id) ?: return
            if (context.isScreenOn()) {
                context.showAlarmNotification(alarm)
                Handler().postDelayed({
                    context.hideNotification(id)
                }, context.config.alarmMaxReminderSecs * 1000L)
            } else {

                val pi = PendingIntent.getActivity(
                        context,
                        0,
                        Intent(context, ReminderActivity::class.java).apply{
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            putExtra(ALARM_ID, id)
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT
                )

                val builder = NotificationCompat.Builder(context, "Alarm")
                        .setSmallIcon(R.drawable.ic_alarm_vector)
                        .setContentTitle("Alarm Active")
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setFullScreenIntent(pi, true)

                val mgr = context.getSystemService(NotificationManager::class.java)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                    && mgr.getNotificationChannel("Alarm") == null ) {
                    mgr.createNotificationChannel(
                            NotificationChannel(
                                    "Alarm",
                                    "Alarm",
                                    NotificationManager.IMPORTANCE_HIGH
                            )
                    )
                }

                mgr.notify(1337, builder.build())


            }
            Intent().also { intent ->
                intent.setAction("com.simplemobiletools.ALARM_IS_ACTIVE")
                intent.putExtra("hours", alarm?.timeInMinutes / 60)
                intent.putExtra("minutes", alarm?.timeInMinutes % 60)
                intent.putExtra("days", alarm.days)
                intent.putExtra("id", alarm.id)
                context.sendBroadcast(intent)
                Log.i("SENT_BROADCAST", "ALARM_IS_RINGING")
            }
        }
    }
}
