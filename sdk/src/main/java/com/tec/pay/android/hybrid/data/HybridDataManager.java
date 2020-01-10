package com.tec.pay.android.hybrid.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.tec.pay.android.base.component.ContextManager;
import com.tec.pay.android.hybrid.data.local.HybridLocalData;
import com.tec.pay.android.hybrid.data.local.SdkData;
import com.tec.pay.android.hybrid.model.GetArrayRequest;
import com.tec.pay.android.hybrid.model.GetArrayResponse;
import com.tec.pay.android.hybrid.model.GetResponse;
import com.tec.pay.android.task.Task;

/**
 * HybridDataManager class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-27.
 */
public class HybridDataManager implements IHybridDataSource {

  private HybridLocalData mHybridLocalData;

  public HybridDataManager init(String appId, String appKey) {
    SdkData.instance().saveAppKey(appKey);
    return this;
  }

  public static HybridDataManager instance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {

    private static final HybridDataManager INSTANCE = new HybridDataManager();
  }

  private HybridDataManager() {
    mHybridLocalData = new HybridLocalData(ContextManager.appContext());
  }

  @Override
  public Task<Void> setCache(@NonNull String key, @Nullable String value) {
    return mHybridLocalData.setCache(key, value);
  }

  @Override
  public Task<Void> setCache(@NonNull String key, @Nullable String value, long expiredMillis) {
    return mHybridLocalData.setCache(key, value, expiredMillis);
  }

  @Override
  public Task<GetResponse> getCache(@NonNull String key, @Nullable String defaultValue) {
    return mHybridLocalData.getCache(key, defaultValue);
  }

  @Override
  public Task<Void> delCache(@NonNull String key) {
    return mHybridLocalData.delCache(key);
  }

  @Override
  public Task<Void> flushCache() {
    return mHybridLocalData.flushCache();
  }

  @Override
  public Task<GetArrayResponse> getInfoList(@NonNull GetArrayRequest request) {
    return mHybridLocalData.getInfoList(request);
  }
}
