package miaoyipu.broadcastreceiverpractice.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import miaoyipu.broadcastreceiverpractice.MUtili;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BCReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        switch (action) {
            case MUtili.ACTION_MYP_ALARM: {
                startFilter(context);
            }
            case MUtili.ACTION_SNOOZE_FILTER: {
                Log.d(TAG, "Snooze filter action received");
                snoozeFilter(context);
            }
            case MUtili.ACTION_STOP_FILTER: {
                stopFilter(context);
            }
            default: {}
        }
    }

    private void startFilter(Context context) {
        // Vibrate for 1 sec
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
        // Make toast
        Toast.makeText(context, "Time up", Toast.LENGTH_SHORT).show();
        // Start screen filter
        Intent filterIntent = new Intent(context, ScreenFilter.class);
        context.startService(filterIntent);
    }

    private void snoozeFilter(Context context) {
        Intent filterIntent = new Intent(context, ScreenFilter.class);
        context.stopService(filterIntent);
    }

    private void stopFilter(Context context) {

    }
}
