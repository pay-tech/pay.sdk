package com.tec.pay.android.hybrid.data.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.tec.pay.android.base.data.BaseConstant;
import com.tec.pay.android.base.data.Pref;
import com.tec.pay.android.base.exception.TecException;
import com.tec.pay.android.base.utils.ObjectUtils;
import com.tec.pay.android.hybrid.data.IHybridDataSource;
import com.tec.pay.android.hybrid.model.GetArrayRequest;
import com.tec.pay.android.hybrid.model.GetArrayResponse;
import com.tec.pay.android.hybrid.model.GetRequest;
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
    return Task.call(() -> {
      String value = mPayCache.getString(key, defaultValue);
      if (TextUtils.isEmpty(value)) {
        throw new TecException(String.format(BaseConstant.MSG_ERROR_ACTION_GET_NULL, key),
            BaseConstant.CODE_ERROR_ACTION_GET_NULL);
      }
      return new GetResponse(key, value);
    });
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
  public Task<GetArrayResponse> getInfoList(@NonNull GetArrayRequest request) {
    return Task.call(() -> {
      GetArrayResponse response = new GetArrayResponse(
          new HashMap<>(request.getGetRequestList().size()));
      for (GetRequest getRequest : request.getGetRequestList()) {
        GetResponse info = getInfo(getRequest);
        response.values.put(info.key, info.value);
      }
      return response;
    });
  }

  private GetResponse getInfo(GetRequest getRequest) {
    Object o = mInfoData.get(getRequest.getKey());
    if (!ObjectUtils.isEmpty(o)) {
      return new GetResponse(getRequest.getKey(), o.toString());
    }
    if (!ObjectUtils.isEmpty(getRequest.getDefValue())) {
      return new GetResponse(getRequest.getKey(), getRequest.getDefValue());
    }
    return new GetResponse(getRequest.getKey(), "");
  }
}
