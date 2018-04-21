package com.example.faiq.taskmanagement.BackgroundService;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {
    public TimerService() {
        counter=0;
    }

    // constant
    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds
    int counter = 0;
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = new Timer();

    @Override
    public IBinder onBind(Intent intent) {
        counter=0;
        return null;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
//        if (mTimer != null) {
//            mTimer.cancel();
//        } else {
//            // recreate new
//            mTimer = new Timer();
//        }
        // schedule task
       // mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.e("checking_service"+startId, counter++ + "");
            }
        },0,NOTIFY_INTERVAL);

        return START_STICKY;

    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run()
                {
                    // display toast
                    // Toast.makeText(getApplicationContext(), getDateTime(),
                    //    Toast.LENGTH_SHORT).show();

                    Log.e("checking_service", counter++ + "");
                }

            });
        }

        private String getDateTime() {
            // get date time in custom format
            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
            return sdf.format(new Date());
        }

    }
}
