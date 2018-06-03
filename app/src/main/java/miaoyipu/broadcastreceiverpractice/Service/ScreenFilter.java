package miaoyipu.broadcastreceiverpractice.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.WindowManager;
import android.widget.Toast;

import miaoyipu.broadcastreceiverpractice.MUtili;
import miaoyipu.broadcastreceiverpractice.MainActivity;
import miaoyipu.broadcastreceiverpractice.R;
import miaoyipu.broadcastreceiverpractice.View.FilterView;

public class ScreenFilter extends Service {
    private FilterView myView;
    private WindowManager.LayoutParams myLayoutParams;
    private WindowManager myWindowManager;
    private int i = 0;
    private NotificationManager notManager;
    private Thread timer;

    @Override
    public void onCreate() {
        super.onCreate();

        myView = new FilterView(this);
        myLayoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT
        );

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            myLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            myLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        myWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        myWindowManager.addView(myView, myLayoutParams);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent snoozeIntent = new Intent(this, MyBroadcastReceiver.class);
        snoozeIntent.setAction(MUtili.ACTION_SNOOZE_FILTER);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(this, MUtili.NOT_SNOOZE_ID, snoozeIntent, 0);

        NotificationCompat.Action snoozeAction =
                new NotificationCompat.Action.Builder(
                        R.drawable.ic_notification,
                        getString(R.string.noti_snooze),
                        snoozePendingIntent).build();

        NotificationCompat.Builder notBuilder =
                new NotificationCompat.Builder(getApplicationContext(), MUtili.NOT_CHANNEL)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Test")
                .setContentText("Filter is running.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(snoozeAction);

        notManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        /* Starting in Android 8.0, all notifications must be assigned to a channel. */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(MUtili.NOT_CHAANEL_ID, MUtili.NOT_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);
            notManager.createNotificationChannel(channel);
        }

        notManager.notify(MUtili.NOT_ID, notBuilder.build());

        updateRun();
    }

    public void updateRun() {
        timer = new Thread(new Runnable() {
            @Override
            public void run() {
                while (i < 60) {
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                        if (myView != null) {
                            myView.update();
                            myView.postInvalidate();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        i++;
                    }
                }
            }
        });

        timer.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myWindowManager.removeView(myView);
        myView = null;
        notManager.cancelAll();
        timer.interrupt();
        i = 0;
    }
}
