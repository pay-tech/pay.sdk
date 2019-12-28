package com.tec.pay.android.base.component;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;

public final class AlarmTicker {

  public static void scheduleForBroadcast(
      long interval, Intent intent, AbstractAlarmReceiver receiver) {
    Context context = ContextManager.appContext();
    PendingIntent pendingIntent =
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    receiver.register(context);
    scheduleInternal(interval, pendingIntent);
  }

  public static void scheduleForService(long interval, Intent intent) {
    PendingIntent pendingIntent =
        PendingIntent.getService(
            ContextManager.appContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    scheduleInternal(interval, pendingIntent);
  }

  private static void scheduleInternal(long interval, PendingIntent pendingIntent) {
    AlarmManager alarmManager;
    try {
      alarmManager = (AlarmManager) ContextManager.appContext()
          .getSystemService(Context.ALARM_SERVICE);
    } catch (Exception e) {
      return;
    }
    alarmManager.cancel(pendingIntent);
    alarmManager.set(
        AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + interval, pendingIntent);
  }

  public abstract static class AbstractAlarmReceiver extends BroadcastReceiver {

    private final IntentFilter mIntentFilter;

    protected AbstractAlarmReceiver(String... filterActions) {
      mIntentFilter = new IntentFilter();
      for (String action : filterActions) {
        mIntentFilter.addAction(action);
      }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
      unregister(context);
      onAlarmReceive(context, intent);
    }

    public void register(Context context) {
      context.registerReceiver(this, mIntentFilter);
    }

    public void unregister(Context context) {
      context.unregisterReceiver(this);
    }

    protected abstract void onAlarmReceive(Context context, Intent intent);
  }
}
