package miaoyipu.broadcastreceiverpractice;


import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import miaoyipu.broadcastreceiverpractice.Service.MyBroadcastReceiver;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_REQUEST_SYSALERT = 11; // TODO: Move to another class
    private Button submitBtn;
    private Button debugBtn;  //TODO: Remove this after done
    private SeekBar seekbar;
    private TextView timeSettingTxt;
    private int timeSetting;
    private SharedPreferences sharedPref; // Key value save file
    private AlarmManager alarmManager;

    /* Onclick listeners defined here */
    private View.OnClickListener submitBtnOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startAlert(view);
        }
    };

    private View.OnClickListener debugBtnOnclick = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          startAlertImmediately(view);
      }
    };

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            progress = (i + 1) * 30;
            String st = Integer.toString(progress) + " seconds";
            timeSettingTxt.setText(st);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //TODO: Save setting
            timeSetting = progress;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.save_timeSetting), progress);
            editor.commit();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlePermission();

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        submitBtn = (Button) findViewById(R.id.main_submitBtn);
        submitBtn.setOnClickListener(submitBtnOnclick);
        debugBtn = (Button) findViewById(R.id.main_debugBtn);
        debugBtn.setOnClickListener(debugBtnOnclick);
        seekbar = (SeekBar) findViewById(R.id.main_seekBar);
        timeSettingTxt = (TextView) findViewById(R.id.main_currentSettingTime);

        readSettings();

        timeSettingTxt.setText(Integer.toString(timeSetting) + " seconds");
        seekbar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    public void startAlert(View view) {
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        intent.setAction(MUtili.ACTION_MYP_ALARM);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(this, MUtili.BR_CODE, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (timeSetting * 1000), pendingIntent);

        Toast.makeText(this, "Alarm set in " + timeSetting + " secs...", Toast.LENGTH_SHORT).show();
    }

    /* Start alert after 3 secs (for debugging purpose) */
    public void startAlertImmediately(View view) {
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        intent.setAction(MUtili.ACTION_MYP_ALARM);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), MUtili.BR_CODE, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (3000), pendingIntent);

        Toast.makeText(this, "Alarm set in 3 secs...", Toast.LENGTH_SHORT).show();
    }

    public void handlePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, MY_PERMISSION_REQUEST_SYSALERT);
            }
        }
    }

    private void readSettings() {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        timeSetting = sharedPref.getInt(getString(R.string.save_timeSetting), MUtili.TIME_SETTING_DEFAULT);
        seekbar.setProgress(timeSetting / 30 - 1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_PERMISSION_REQUEST_SYSALERT) {
            if (Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else { System.exit(1); }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) {
            case MY_PERMISSION_REQUEST_SYSALERT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else { System.exit(1); }
            }
        }
    }

}
