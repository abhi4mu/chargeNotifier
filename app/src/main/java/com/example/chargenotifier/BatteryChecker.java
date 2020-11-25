package com.example.chargenotifier;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class BatteryChecker extends BroadcastReceiver {
    public  MediaPlayer player;
    public String CHANNEL_ID = "1";
    public int prev_bat = 0;
    @Override
    public void onReceive(Context context, Intent intent)
    {

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int percentage = level*100/scale;

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING);

        TextView t = ((MainActivity)context).findViewById(R.id.textView);
        t.setText(Integer.toString(percentage)+"%");
        if(!isCharging) {
            try {
                player.release();
            } catch(NullPointerException e) {
                isCharging = false;
            }
        }
        else if(percentage==100 && isCharging && prev_bat!=100){

            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

            AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            while(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)!=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC))
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_ALLOW_RINGER_MODES);

            player = MediaPlayer.create(context, alert);
            player.setLooping(true);
            player.start();
        }
        prev_bat = percentage;

    }




}