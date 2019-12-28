package com.tec.pay.android.hybrid.data.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.tec.pay.android.base.data.Pref;
import com.tec.pay.android.hybrid.data.IHybridDataSource;
import com.tec.pay.android.hybrid.model.GetResponse;
import com.tec.pay.android.task.Task;

public class HybridLocalData implements IHybridDataSource {

  public static final String PREF_PAY_CACHE = "TEC%PAY_CACHE";

  private Pref mPayCache;

  public HybridLocalData(Context context) {
    mPayCache = new Pref(context, PREF_PAY_CACHE);
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
}
