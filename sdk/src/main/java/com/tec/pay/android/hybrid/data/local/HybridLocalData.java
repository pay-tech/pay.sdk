package com.tec.pay.android.hybrid.data.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.tec.pay.android.base.data.Pref;
import com.tec.pay.android.hybrid.data.IHybridDataSource;
import com.tec.pay.android.hybrid.model.GetResponse;
import com.tec.pay.android.task.Task;
import java.util.HashMap;
import java.util.Map;

public class HybridLocalData implements IHybridDataSource {

  public static final String PREF_PAY_CACHE = "TEC%PAY_CACHE";

  private Pref mPayCache;
  private Map<String, Object> mInfoData = new HashMap<>();

  public HybridLocalData(Context context) {
    mPayCache = new Pref(context, PREF_PAY_CACHE);
    mInfoData.putAll(AppData.getAppInfo());
    mInfoData.putAll(AppData.getSDKInfo());
    mInfoData.putAll(DeviceData.getDeviceInfo());
    mInfoData.putAll(DeviceData.getNetworkInfo());
    mInfoData.putAll(DeviceData.getSystemInfo());
  }

  @Override
  public Task<Void> setCache(@NonNull String key, @Nullable String value) {
    return Task.call(() -> {
      mPayCache.put(key, value);
      return null;
    });
  }

  @Override
  public Task<Void> setCache(@NonNull String key, @Nullable String value, long expiredMillis) {
    return Task.call(() -> {
      mPayCache.put(key, value, expiredMillis);
      return null;
    });
  }

  @Override
  public Task<GetResponse> getCache(@NonNull String key, @Nullable String defaultValue) {
    return Task.call(() -> new GetResponse(key, mPayCache.getString(key, defaultValue)));
  }

  @Override
  public Task<Void> delCache(@NonNull final String key) {
    return Task.call(() -> {
      mPayCache.remove(key);
      return null;
    });
  }

  @Override
  public Task<Void> flushCache() {
    return Task.call(() -> {
      mPayCache.clear();
      return null;
    });
  }

  @Override
  public Task<GetResponse> getInfo(@NonNull String key, @Nullable String defaultValue) {
    return Task.call(() -> {
      String value;
      Object o = mInfoData.get(key);
      if (o == null) {
        value = defaultValue;
      } else {
        value = o.toString();
      }
      return new GetResponse(key, value);
    });
  }
}
