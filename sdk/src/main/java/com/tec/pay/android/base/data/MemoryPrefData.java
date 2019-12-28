package com.tec.pay.android.base.data;

import android.text.TextUtils;
import android.util.LruCache;
import com.tec.pay.android.base.log.DLog;

/**
 * MemoryPrefData class.
 *
 * @author Lucas Cheung.
 * @date 2019-06-15.
 */
public class MemoryPrefData {

  private static final int DEFAULT_CACHE_SIZE = 20;
  private final Pref mPref;
  private final LruCache<Object, Object> mLRUCache;

  public MemoryPrefData(Pref mPref) {
    this.mPref = mPref;
    this.mLRUCache = new LruCache<>(DEFAULT_CACHE_SIZE);
  }

  public MemoryPrefData(Pref mPref, int maxSize) {
    this.mPref = mPref;
    this.mLRUCache = new LruCache<>(maxSize);
  }

  public void clear() {
    mPref.clear();
    mLRUCache.evictAll();
  }

  public void put(String key, int value) {
    mPref.put(key, value);
    mLRUCache.put(key, value);
  }

  public void put(String key, int value, long expiredMillis) {
    mPref.put(key, value, expiredMillis);
  }

  public void put(String key, long value) {
    mPref.put(key, value);
    mLRUCache.put(key, value);
  }

  public void put(String key, long value, long expiredMillis) {
    mPref.put(key, value, expiredMillis);
  }

  public void put(String key, boolean value) {
    mPref.put(key, value);
    mLRUCache.put(key, value);
  }

  public void put(String key, boolean value, long expiredMillis) {
    mPref.put(key, value, expiredMillis);
  }

  public void put(String key, String value) {
    mPref.put(key, value);
    mLRUCache.put(key, value);
  }

  public void put(String key, String value, long expiredMillis) {
    mPref.put(key, value, expiredMillis);
  }

  public int getInt(String key, int defaultValue) {
    try {
      Integer value = (Integer) mLRUCache.get(key);
      if (value == null) {
        value = mPref.getInt(key, defaultValue);
        mLRUCache.put(key, value);
      }
      return value;
    } catch (Exception e) {
      DLog.w("key %s cache occur error.", key);
      return mPref.getInt(key, defaultValue);
    }
  }

  public long getLong(String key, long defaultValue) {
    try {
      Long value = (Long) mLRUCache.get(key);
      if (value == null) {
        value = mPref.getLong(key, defaultValue);
        mLRUCache.put(key, value);
      }
      return value;
    } catch (Exception e) {
      DLog.w("key %s cache occur error.", key);
      return mPref.getLong(key, defaultValue);
    }
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    try {
      Boolean value = (Boolean) mLRUCache.get(key);
      if (value == null) {
        value = mPref.getBoolean(key, defaultValue);
        mLRUCache.put(key, value);
      }
      return value;
    } catch (Exception e) {
      DLog.w("key %s cache occur error.", key);
      return mPref.getBoolean(key, defaultValue);
    }
  }

  public String getString(String key, String defaultValue) {
    try {
      String value = (String) mLRUCache.get(key);
      if (TextUtils.isEmpty(value)) {
        value = mPref.getString(key, defaultValue);
        mLRUCache.put(key, value);
      }
      return value;
    } catch (Exception e) {
      DLog.w("key %s cache occur error.", key);
      return mPref.getString(key, defaultValue);
    }
  }

  public long remain(String key) {
    return mPref.remain(key);
  }

  public boolean contains(String key) {
    return mPref.contains(key);
  }

  public void remove(String key) {
    mPref.remove(key);
    mLRUCache.remove(key);
  }
}
