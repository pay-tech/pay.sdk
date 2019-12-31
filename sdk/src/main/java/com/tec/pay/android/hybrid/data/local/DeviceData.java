package com.tec.pay.android.hybrid.data.local;

import android.os.Build;
import com.tec.pay.android.base.utils.AppUtil;
import com.tec.pay.android.base.utils.DeviceUtil;
import com.tec.pay.android.base.utils.IdentityUtil;
import com.tec.pay.android.base.utils.PhoneUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * DeviceData class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-30.
 */
public class DeviceData {

  private DeviceData() {

  }

  public static Map<String, Object> getSystemInfo() {
    Map<String, Object> map = new HashMap<>(10);
    map.put("os", "android");
    map.put("osVer", DeviceUtil.osVersion());
    map.put("lang", DeviceUtil.lang());
    map.put("timezone", DeviceUtil.timezoneUTC());
    map.put("browserKernel", DeviceUtil.browserKernel());
    map.put("browserKernelVer", DeviceUtil.browserKernelVersion());
    map.put("isRooted", DeviceUtil.isDeviceRooted());
    map.put("androidId", DeviceUtil.androidID());
    map.put("utdId", IdentityUtil.getUtdid());
    map.put("gaid", IdentityUtil.getGAId());

    return map;
  }

  public static Map<String, Object> getDeviceInfo() {
    Map<String, Object> map = new HashMap<>(10);
    map.put("brand", DeviceUtil.brand());
    map.put("model", DeviceUtil.model());
    map.put("resolution", DeviceUtil.resolution());
    map.put("board", Build.BOARD);
    map.put("manufacturer", DeviceUtil.manufacturer());
    map.put("radio", DeviceUtil.radioVersion());
    map.put("mac", DeviceUtil.macAddress());
    map.put("serial", DeviceUtil.serial());
    map.put("imei", PhoneUtil.getIMEI());
    map.put("imsi", PhoneUtil.getIMSI());
    return map;
  }

  public static Map<String, Object> getNetworkInfo() {
    Map<String, Object> map = new HashMap<>(9);
    map.put("hasSim", PhoneUtil.isSimCardReady());
    map.put("simOperatorId", PhoneUtil.getSimOperator());
    map.put("simOperatorName", PhoneUtil.getSimOperatorName());
    map.put("simCountry", PhoneUtil.getSimCountryIso());
    map.put("simSerial", PhoneUtil.getSimSerialNumber());
    map.put("netCountry", PhoneUtil.getNetworkCountryIso());
    map.put("netOperatorId", PhoneUtil.getNetworkOperator());
    map.put("netOperatorName", PhoneUtil.getNetworkOperatorName());
    map.put("isVPNEnable", AppUtil.isVPNEnable());
    return map;
  }
}
