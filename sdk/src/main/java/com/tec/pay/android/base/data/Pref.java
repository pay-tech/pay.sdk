package com.tec.pay.android.base.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Pref {

  private static final String PREFIX_PUT_TIME = "ptime_";
  private static final String PREFIX_EXPIRED_TIME = "etime_";
  private SharedPreferences sp;

  public Pref(Context context, String name) {
    int mode = 0;
    this.sp = context.getSharedPreferences(name, mode);
  }

  public Pref(SharedPreferences sp) {
    this.sp = sp;
  }

  private static String getPutTimeKey(String key) {
    return PREFIX_PUT_TIME + key;
  }

  private static String getExpiredTimeKey(String key) {
    return PREFIX_EXPIRED_TIME + key;
  }

  public Pref put(String key, int value) {
    this.editor().putInt(key, value).apply();
    return this;
  }

  public Pref put(String key, int value, long expiredMillis) {
    this.expiredEditor(key, expiredMillis).putInt(key, value).apply();
    return this;
  }

  public Pref put(String key, long value) {
    this.editor().putLong(key, value).apply();
    return this;
  }

  public Pref put(String key, long value, long expiredMillis) {
    this.expiredEditor(key, expiredMillis).putLong(key, value).apply();
    return this;
  }

  public Pref put(String key, boolean value) {
    this.editor().putBoolean(key, value).apply();
    return this;
  }

  public Pref put(String key, boolean value, long expiredMillis) {
    this.expiredEditor(key, expiredMillis).putBoolean(key, value).apply();
    return this;
  }

  public Pref put(String key, String value) {
    this.editor().putString(key, value).apply();
    return this;
  }

  public Pref put(String key, String value, long expiredMillis) {
    this.expiredEditor(key, expiredMillis).putString(key, value).apply();
    return this;
  }

  public int getInt(String key, int defaultValue) {
    this.checkAndRemove(key);
    return this.sp.getInt(key, defaultValue);
  }

  public long getLong(String key, long defaultValue) {
    this.checkAndRemove(key);
    return this.sp.getLong(key, defaultValue);
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    this.checkAndRemove(key);
    return this.sp.getBoolean(key, defaultValue);
  }

  public String getString(String key, String defaultValue) {
    this.checkAndRemove(key);
    return this.sp.getString(key, defaultValue);
  }

  public long remain(String key) {
    long remain = 0L;
    long currentTimeMillis = System.currentTimeMillis();
    long etime = this.sp.getLong(getExpiredTimeKey(key), -1L);
    long ptime = this.sp.getLong(getPutTimeKey(key), -1L);
    if (etime != -1L && ptime != -1L) {
      remain = etime - (currentTimeMillis - ptime);
    }

    return remain;
  }

  public boolean contains(String key) {
    this.checkAndRemove(key);
    return this.sp.contains(key);
  }

  public void clear() {
    this.editor().clear().apply();
  }

  public void remove(String key) {
    this.editor().remove(key).remove(getPutTimeKey(key)).remove(getExpiredTimeKey(key)).apply();
  }

  private Editor editor() {
    return this.sp.edit();
  }

  private Editor expiredEditor(String key, long expiredMillis) {
    return this.sp
        .edit()
        .putLong(getPutTimeKey(key), System.currentTimeMillis())
        .putLong(getExpiredTimeKey(key), expiredMillis);
  }

  private void checkAndRemove(String key) {
    long currentTimeMillis = System.currentTimeMillis();
    long etime = this.sp.getLong(getExpiredTimeKey(key), -1L);
    long ptime = this.sp.getLong(getPutTimeKey(key), -1L);
    if (etime != -1L && ptime != -1L && currentTimeMillis - ptime > etime) {
      this.remove(key);
    }
  }
}
