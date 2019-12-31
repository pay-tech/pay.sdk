package com.tec.pay.android.base.utils;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.SEND_SMS;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.tec.pay.android.base.component.ContextManager;
import java.util.List;


public final class PhoneUtil {

  private PhoneUtil() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /**
   * Return whether the device is phone.
   *
   * @return {@code true}: yes<br> {@code false}: no
   */
  public static boolean isPhone() {
    TelephonyManager tm = null;
    try {
      tm = (TelephonyManager) ContextManager.appContext()
          .getSystemService(Context.TELEPHONY_SERVICE);
    } catch (Exception ignored) {
    }
    return tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
  }

  /**
   * Return the unique device id.
   *
   * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}
   *
   * @return the unique device id
   */
  @SuppressLint({"HardwareIds", "MissingPermission"})
  @RequiresPermission(READ_PHONE_STATE)
  public static String getDeviceId() {
    TelephonyManager tm = null;
    try {
      tm = (TelephonyManager) ContextManager.appContext()
          .getSystemService(Context.TELEPHONY_SERVICE);
    } catch (Exception ignored) {
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      if (tm == null) {
        return "";
      }
      String imei = tm.getImei();
      if (!TextUtils.isEmpty(imei)) {
        return imei;
      }
      String meid = tm.getMeid();
      return TextUtils.isEmpty(meid) ? "" : meid;
    }
    return tm != null ? tm.getDeviceId() : "";
  }

  /**
   * Return the IMEI.
   *
   * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}
   *
   * @return the IMEI
   */
  @SuppressLint({"HardwareIds", "MissingPermission"})
  public static String getIMEI() {
    String imei = "";
    TelephonyManager tm = null;
    try {
      tm = (TelephonyManager) ContextManager.appContext()
          .getSystemService(Context.TELEPHONY_SERVICE);
    } catch (Exception ignored) {
    }
    if (tm == null) {
      return imei;
    }
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        return tm.getImei();
      }
      return tm.getDeviceId();
    } catch (Exception ignored) {
    }
    return imei;
  }

  /**
   * Return the MEID.
   *
   * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}
   *
   * @return the MEID
   */
  @SuppressLint({"HardwareIds", "MissingPermission"})
  public static String getMEID() {
    String meid = "";
    TelephonyManager tm = null;
    try {
      tm = (TelephonyManager) ContextManager.appContext()
          .getSystemService(Context.TELEPHONY_SERVICE);
    } catch (Exception ignored) {
    }
    if (tm == null) {
      return meid;
    }
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        return tm.getMeid();
      }
      return tm.getDeviceId();
    } catch (Exception ignored) {
    }
    return meid;
  }

  /**
   * Return the IMSI.
   *
   * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}
   *
   * @return the IMSI
   */
  @SuppressLint({"HardwareIds", "MissingPermission"})
  public static String getIMSI() {
    String imsi = "";
    TelephonyManager tm = null;
    try {
      tm = (TelephonyManager) ContextManager.appContext()
          .getSystemService(Context.TELEPHONY_SERVICE);
    } catch (Exception ignored) {
    }
    if (tm == null) {
      return imsi;
    }
    try {
      return tm.getSubscriberId();
    } catch (Exception ignored) {
    }
    return imsi;
  }

  /**
   * Returns the current phone type.
   *
   * @return the current phone type
   * <ul>
   * <li>{@link TelephonyManager#PHONE_TYPE_NONE}
   * <li>{@link TelephonyManager#PHONE_TYPE_GSM }
   * <li>{@link TelephonyManager#PHONE_TYPE_CDMA}
   * <li>{@link TelephonyManager#PHONE_TYPE_SIP }
   * </ul>
   */
  public static int getPhoneType() {
    TelephonyManager tm = null;
    try {
      tm = (TelephonyManager) ContextManager.appContext()
          .getSystemService(Context.TELEPHONY_SERVICE);
    } catch (Exception ignored) {
    }
    int phoneType = -1;
    try {
      phoneType = tm != null ? tm.getPhoneType() : -1;
    } catch (Exception ignored) {
    }
    return phoneType;
  }

  /**
   * Return whether sim card state is ready.
   *
   * @return {@code true}: yes<br> i {@code false}: no
   */
  public static boolean isSimCardReady() {
    TelephonyManager tm = null;
    try {
      tm = (TelephonyManager) ContextManager.appContext()
          .getSystemService(Context.TELEPHONY_SERVICE);
    } catch (Exception ignored) {
    }
    boolean isReady = false;
    try {
      isReady = (tm != null && (tm.getSimState() == TelephonyManager.SIM_STATE_READY));
    } catch (Exception ignored) {
    }
    return isReady;
  }

  /**
   * Return the sim operator name.
   *
   * @return the sim operator name
   */
  public static String getSimOperatorName() {
    TelephonyManager tm = null;
    try {
      tm = (TelephonyManager) ContextManager.appContext()
          .getSystemService(Context.TELEPHONY_SERVICE);
    } catch (Exception ignored) {
    }

    String operator = "";
    try {
      operator = tm != null ? tm.getSimOperatorName() : "";
    } catch (Exception ignored) {

    }
    return operator;
  }

  public static String getSimOperator() {
    TelephonyManager tm = null;
    try {
      tm = (TelephonyManager) ContextManager.appContext()
          .getSystemService(Context.TELEPHONY_SERVICE);
    } catch (Exception ignored) {
    }

    String operator = "";
    try {
      operator = tm != null ? tm.getSimOperator() : "";
    } catch (Exception ignored) {

    }
    return operator;
  }

  public static String getSimCountryIso() {
    TelephonyManager tm = null;
    try {
      tm = (TelephonyManager) ContextManager.appContext()
          .getSystemService(Context.TELEPHONY_SERVICE);
    } catch (Exception ignored) {
    }

    String operator = "";
    try {
      operator = tm != null ? tm.getSimCountryIso() : "";
    } catch (Exception ignored) {

    }
    return operator;
  }

  @SuppressLint({"HardwareIds", "MissingPermission"})
  public static String getSimSerialNumber() {
    TelephonyManager tm = null;
    try {
      tm = (TelephonyManager) ContextManager.appContext()
          .getSystemService(Context.TELEPHONY_SERVICE);
    } catch (Exception ignored) {
    }
    try {
      return tm != null ? tm.getSimSerialNumber() : "";
    } catch (Exception e) {
      return "";
    }
  }

  /**
   * Return the sim operator using mnc.
   *
   * @return the sim operator
   */
  public static String getSimOperatorByMnc() {
    TelephonyManager tm = null;
    try {
      tm = (TelephonyManager) ContextManager.appContext()
          .getSystemService(Context.TELEPHONY_SERVICE);
    } catch (Exception ignored) {
    }
    String operator = "";
    try {
      operator = tm != null ? tm.getSimOperator() : "";
    } catch (Exception ignored) {

    }

    switch (operator) {
      case "46000":
      case "46002":
      case "46007":
        return "中国移动";
      case "46001":
        return "中国联通";
      case "46003":
        return "中国电信";
      default:
        return operator;
    }
  }

  /**
   * Skip to dial.
   *
   * @param phoneNumber The phone number.
   */
  public static void dial(final String phoneNumber) {
    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
    ContextManager.appContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
  }

  /**
   * Make a phone call.
   *
   * <p>Must hold {@code <uses-permission android:name="android.permission.CALL_PHONE" />}
   *
   * @param phoneNumber The phone number.
   */
  @RequiresPermission(CALL_PHONE)
  public static void call(final String phoneNumber) {
    Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber));
    ContextManager.appContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
  }

  /**
   * Send sms.
   *
   * @param phoneNumber The phone number.
   * @param content The content.
   */
  public static void sendSms(final String phoneNumber, final String content) {
    Uri uri = Uri.parse("smsto:" + phoneNumber);
    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
    intent.putExtra("sms_body", content);
    ContextManager.appContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
  }

  /**
   * Send sms silently.
   *
   * <p>Must hold {@code <uses-permission android:name="android.permission.SEND_SMS" />}
   *
   * @param phoneNumber The phone number.
   * @param content The content.
   */
  @RequiresPermission(SEND_SMS)
  public static void sendSmsSilent(final String phoneNumber, final String content) {
    if (TextUtils.isEmpty(content)) {
      return;
    }
    PendingIntent sentIntent =
        PendingIntent.getBroadcast(ContextManager.appContext(), 0, new Intent("send"), 0);
    SmsManager smsManager = SmsManager.getDefault();
    if (content.length() >= 70) {
      List<String> ms = smsManager.divideMessage(content);
      for (String str : ms) {
        smsManager.sendTextMessage(phoneNumber, null, str, sentIntent, null);
      }
    } else {
      smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null);
    }
  }

  public static String getNetworkOperatorName() {
    TelephonyManager tm = null;
    try {
      tm = (TelephonyManager) ContextManager.appContext()
          .getSystemService(Context.TELEPHONY_SERVICE);
    } catch (Exception ignored) {
    }
    String networkOperatorName = "";
    try {
      networkOperatorName = tm != null ? tm.getNetworkOperatorName() : "";
    } catch (Exception ignored) {

    }
    return networkOperatorName;
  }

  public static String getNetworkOperator() {
    TelephonyManager tm = null;
    try {
      tm = (TelephonyManager) ContextManager.appContext()
          .getSystemService(Context.TELEPHONY_SERVICE);
    } catch (Exception ignored) {
    }
    String networkOperator = "";
    try {
      networkOperator = tm != null ? tm.getNetworkOperator() : "";
    } catch (Exception ignored) {

    }
    return networkOperator;
  }

  public static String getNetworkCountryIso() {
    TelephonyManager tm = null;
    try {
      tm = (TelephonyManager) ContextManager.appContext()
          .getSystemService(Context.TELEPHONY_SERVICE);
    } catch (Exception ignored) {
    }
    String networkCountryIso = "";
    try {
      networkCountryIso = tm != null ? tm.getNetworkCountryIso() : "";
    } catch (Exception ignored) {

    }
    return networkCountryIso;
  }
}
