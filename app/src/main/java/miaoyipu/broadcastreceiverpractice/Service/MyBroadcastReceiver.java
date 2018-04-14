package miaoyipu.broadcastreceiverpractice.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

import miaoyipu.broadcastreceiverpractice.MUtili;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action != null && action == MUtili.ACTION_MYP_ALARM) {

            Toast.makeText(context, "Time up", Toast.LENGTH_SHORT).show();
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(1000);

            Intent filterService = new Intent(context, ScreenFilter.class);
            context.startService(filterService);
        } else if (action != null && action == MUtili.ACTION_SNOOZE_FILTER) {
            Intent filterService = new Intent(context, ScreenFilter.class);
            context.stopService(filterService);
        }
    }
}
