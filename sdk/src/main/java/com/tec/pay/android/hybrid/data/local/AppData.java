package com.tec.pay.android.hybrid.data.local;

import com.tec.pay.android.BuildConfig;
import com.tec.pay.android.base.utils.AppUtil;
import com.tec.pay.android.base.utils.IdentityUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * AppData class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-30.
 */
public class AppData {

  private AppData() {
  }

  public static Map<String, Object> getAppInfo() {
    Map<String, Object> map = new HashMap<>(4);
    map.put("appPkg", AppUtil.packageName());
    map.put("appVN", AppUtil.versionName());
    map.put("appVC", AppUtil.versionCode());
    map.put("installId", IdentityUtil.getInstallId());
    return map;
  }

  public static Map<String, Object> getSDKInfo() {
    Map<String, Object> map = new HashMap<>(2);
    map.put("sdkVN", BuildConfig.VERSION_NAME);
    map.put("sdkVC", BuildConfig.VERSION_CODE);
    return map;
  }


}
