package com.tec.pay.android.hybrid.data.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.tec.pay.android.base.component.ContextManager;
import com.tec.pay.android.base.data.Pref;

/**
 * SdkData class.
 *
 * @author Lucas Cheung.
 * @date 2019-05-07.
 */
public final class SdkData {

  private static final String PREF_FLAGS = "TEC%PREF_FLAGS";
  private static final String KEY_INFO_DEVICE_ID = "info_device_id";
  private static final String KEY_GAD_ID = "info_gad_id";
  private static final String KEY_APP_KEY = "appKey";

  private static SdkData INSTANCE;
  private Pref mFlagsPref;

  public static SdkData instance() {
    if (INSTANCE == null) {
      INSTANCE = new SdkData(ContextManager.appContext());
    }
    return INSTANCE;
  }

  private SdkData(Context context) {
    mFlagsPref = new Pref(context, PREF_FLAGS);
  }

  public String getInfoDeviceId() {
    return mFlagsPref.getString(KEY_INFO_DEVICE_ID, null);
  }

  public void updateInfoDeviceId(String deviceId) {
    if (TextUtils.isEmpty(deviceId)) {
      return;
    }
    mFlagsPref.put(KEY_INFO_DEVICE_ID, deviceId);
  }

  public String getAdvertisingID() {
    return mFlagsPref.getString(KEY_GAD_ID, "");
  }

  public void updateAdvertisingID(String id) {
    if (TextUtils.isEmpty(id)) {
      return;
    }
    mFlagsPref.put(KEY_GAD_ID, id);
  }

  public void saveAppKey(@NonNull String appKey) {
    mFlagsPref.put(KEY_APP_KEY, appKey);
  }

  public String getAppKey() {
    return mFlagsPref.getString(KEY_APP_KEY, "");
  }
}
