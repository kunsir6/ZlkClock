package com.zlkclock;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.PowerManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @author zhenglk on 2020-07-05
 */
public class FullscreenActivity extends AppCompatActivity {


    private SimpleClockView simpleClockView;
    PowerManager.WakeLock wakeLock = null;


    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        simpleClockView = findViewById(R.id.tomato_view);
        simpleClockView.showNewTime();

        // 更新时间的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        wakeLock.acquire(120000L);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            switch (action) {
                case Intent.ACTION_TIME_TICK:
                    simpleClockView.showNewTime();
                    wakeLock.acquire(120000L);
                    break;
                case Intent.ACTION_BATTERY_CHANGED:
                    simpleClockView.showNewBatteryLevel(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 100));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销广播
        unregisterReceiver(receiver);
    }
}
