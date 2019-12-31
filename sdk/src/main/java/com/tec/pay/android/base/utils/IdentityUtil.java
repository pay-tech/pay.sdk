package com.tec.pay.android.base.utils;

import android.text.TextUtils;
import com.mbs.utdi.device.ut.UTDevice;
import com.tec.pay.android.base.component.ContextManager;
import com.tec.pay.android.hybrid.data.local.SdkData;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public final class IdentityUtil {

  private static String mIdentity;
  private static IUDIDProvider sGAIDProvider = new GAIDProvider();
  private static IUDIDProvider sUtdidProvider = new UtdidProvider();

  public static String getGAId() {
    String id = sGAIDProvider.get();
    if (sGAIDProvider.isIdValidate(id)) {
      return id;
    }
    return "";
  }

  public static String getUtdid() {
    String id = sUtdidProvider.get();
    if (sUtdidProvider.isIdValidate(id)) {
      return id;
    }
    return "";
  }

  public static String getInstallId() {
    if (TextUtils.isEmpty(mIdentity)) {
      mIdentity = getFromSp();
    } else {
      return mIdentity;
    }

    if (TextUtils.isEmpty(mIdentity)) {
      mIdentity = generateUserHash();
    }

    setToSp(mIdentity);

    return mIdentity;
  }

  private static String md5(String key) {
    if (key == null) {
      return "";
    }
    String cacheKey;
    try {
      final MessageDigest mDigest = MessageDigest.getInstance("MD5");
      mDigest.update(key.getBytes());
      cacheKey = bytesToHexString(mDigest.digest());
    } catch (NoSuchAlgorithmException e) {
      cacheKey = String.valueOf(key.hashCode());
    }
    return cacheKey;
  }

  private static String bytesToHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte _byte : bytes) {
      String hex = Integer.toHexString(0xFF & _byte);
      if (hex.length() == 1) {
        sb.append('0');
      }
      sb.append(hex);
    }
    return sb.toString();
  }

  private static String generateUserHash() {
    return md5(UUID.randomUUID().toString() + System.currentTimeMillis());
  }

  private static String getFromSp() {
    return SdkData.instance().getInfoDeviceId();
  }

  private static void setToSp(String userHash) {
    SdkData.instance().updateInfoDeviceId(userHash);
  }

  static class GAIDProvider implements IUDIDProvider {

    @Override
    public String get() {
      String advertisingID = SdkData.instance().getAdvertisingID();
      if (TextUtils.isEmpty(advertisingID)) {
        advertisingID = AdvertisingIdUtils.getAdvertisingID(ContextManager.appContext());
        SdkData.instance().updateAdvertisingID(advertisingID);
      }
      return advertisingID;
    }

    @Override
    public boolean isIdValidate(String id) {
      return !TextUtils.isEmpty(id);
    }
  }

  static class UtdidProvider implements IUDIDProvider {

    @Override
    public String get() {
      return UTDevice.getUtdid(ContextManager.appContext());
    }

    @Override
    public boolean isIdValidate(String id) {
      return !id.startsWith("ffffff");
    }
  }

  interface IUDIDProvider {

    String get();

    boolean isIdValidate(String id);
  }
}
