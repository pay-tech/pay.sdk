package com.tec.pay.android.base.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.LocaleList;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.tec.pay.android.base.component.ContextManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;

public final class DeviceUtil {

  private static String mCPUArch;
  private static long mRamSize;

  private DeviceUtil() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /**
   * Return whether device is rooted.
   *
   * @return {@code true}: yes<br> {@code false}: no
   */
  public static boolean isDeviceRooted() {
    String su = "su";
    String[] locations = {
        "/system/bin/",
        "/system/xbin/",
        "/sbin/",
        "/system/sd/xbin/",
        "/system/bin/failsafe/",
        "/data/local/xbin/",
        "/data/local/bin/",
        "/data/local/"
    };
    for (String location : locations) {
      if (new File(location + su).exists()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Return version code of device's system.
   *
   * @return version code of device's system
   */
  public static int osVersionCode() {
    return Build.VERSION.SDK_INT;
  }

  /**
   * Return the android id of device.
   *
   * @return the android id of device
   */
  @SuppressLint("HardwareIds")
  public static String androidID() {
    String id =
        Settings.Secure.getString(
            ContextManager.appContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    if (!(TextUtils.isEmpty(id) || "9774d56d682e549c".equals(id) || id.length() < 15)) {
      return id;
    }
    return "";
  }

  /**
   * The android version which is displayed in system Settings/About phone/Android version
   */
  public static String osVersion() {
    return Build.VERSION.RELEASE;
  }

  /**
   * The serial number which is displayed in system Settings/About phone/Status/Serial number
   */
  public static String serial() {
    String serial;
    try {
      serial = Build.class.getField("SERIAL").get(null).toString();
    } catch (Exception e) {
      serial = "";
    }
    return serial;
  }

  /**
   * The OS build number which is displayed in system Settings/About phone/Build number
   */
  public static String displayBuildNumber() {
    return Build.DISPLAY;
  }

  /**
   * The OS build number
   */
  public static String buildNumber() {
    return Build.ID;
  }

  /**
   * The baseband version which is displayed in system Settings/About phone/Baseband version
   */
  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  public static String radioVersion() {
    if (isSdkLegal(14)) {
      try {
        return Build.getRadioVersion();
      } catch (Throwable e) {
        return Build.UNKNOWN;
      }
    }
    return Build.UNKNOWN;
  }

  /**
   * The end-user-visible name for the end product.
   */
  public static String model() {
    return Build.MODEL;
  }

  /**
   * The manufacturer (e.g.,Samsung/Huawei...) of the product/hardware
   */
  public static String manufacturer() {
    return Build.MANUFACTURER;
  }

  /**
   * Return the MAC address.
   *
   * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
   * {@code <uses-permission android:name="android.permission.INTERNET" />}
   *
   * @return the MAC address
   */
  public static String macAddress() {
    return macAddress((String[]) null);
  }

  /**
   * Return the MAC address.
   *
   * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
   * {@code <uses-permission android:name="android.permission.INTERNET" />}
   *
   * @return the MAC address
   */
  public static String macAddress(final String... excepts) {
    String macAddress = getMacAddressByWifiInfo();
    if (isAddressNotInExcepts(macAddress, excepts)) {
      return macAddress;
    }
    macAddress = getMacAddressByNetworkInterface();
    if (isAddressNotInExcepts(macAddress, excepts)) {
      return macAddress;
    }
    macAddress = getMacAddressByInetAddress();
    if (isAddressNotInExcepts(macAddress, excepts)) {
      return macAddress;
    }
    macAddress = getMacAddressByFile();
    if (isAddressNotInExcepts(macAddress, excepts)) {
      return macAddress;
    }
    return "";
  }

  private static boolean isAddressNotInExcepts(final String address, final String... excepts) {
    if (excepts == null || excepts.length == 0) {
      return !"02:00:00:00:00:00".equals(address);
    }
    for (String filter : excepts) {
      if (address.equals(filter)) {
        return false;
      }
    }
    return true;
  }

  @SuppressLint({"HardwareIds", "MissingPermission"})
  private static String getMacAddressByWifiInfo() {
    try {
      Context context = ContextManager.appContext().getApplicationContext();
      WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
      if (wifi != null) {
        WifiInfo info = wifi.getConnectionInfo();
        if (info != null) {
          return info.getMacAddress();
        }
      }
    } catch (Exception ignored) {
    }
    return "02:00:00:00:00:00";
  }

  private static String getMacAddressByNetworkInterface() {
    try {
      Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
      while (nis.hasMoreElements()) {
        NetworkInterface ni = nis.nextElement();
        if (ni == null || !ni.getName().equalsIgnoreCase("wlan0")) {
          continue;
        }
        byte[] macBytes = ni.getHardwareAddress();
        if (macBytes != null && macBytes.length > 0) {
          StringBuilder sb = new StringBuilder();
          for (byte b : macBytes) {
            sb.append(String.format("%02x:", b));
          }
          return sb.substring(0, sb.length() - 1);
        }
      }
    } catch (Exception ignored) {
    }
    return "02:00:00:00:00:00";
  }

  private static String getMacAddressByInetAddress() {
    try {
      InetAddress inetAddress = getInetAddress();
      if (inetAddress != null) {
        NetworkInterface ni = NetworkInterface.getByInetAddress(inetAddress);
        if (ni != null) {
          byte[] macBytes = ni.getHardwareAddress();
          if (macBytes != null && macBytes.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (byte b : macBytes) {
              sb.append(String.format("%02x:", b));
            }
            return sb.substring(0, sb.length() - 1);
          }
        }
      }
    } catch (Exception ignored) {
    }
    return "02:00:00:00:00:00";
  }

  private static InetAddress getInetAddress() {
    try {
      Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
      while (nis.hasMoreElements()) {
        NetworkInterface ni = nis.nextElement();
        // To prevent phone of xiaomi return "10.0.2.15"
        if (!ni.isUp()) {
          continue;
        }
        Enumeration<InetAddress> addresses = ni.getInetAddresses();
        while (addresses.hasMoreElements()) {
          InetAddress inetAddress = addresses.nextElement();
          if (!inetAddress.isLoopbackAddress()) {
            String hostAddress = inetAddress.getHostAddress();
            if (hostAddress.indexOf(':') < 0) {
              return inetAddress;
            }
          }
        }
      }
    } catch (SocketException ignored) {
    }
    return null;
  }

  private static String getMacAddressByFile() {
    ShellUtil.CommandResult result = ShellUtil.execCmd("getprop wifi.interface", false);
    if (result.result == 0) {
      String name = result.successMsg;
      if (name != null) {
        result = ShellUtil.execCmd("cat /sys/class/net/" + name + "/address", false);
        if (result.result == 0) {
          String address = result.successMsg;
          if (address != null && address.length() > 0) {
            return address;
          }
        }
      }
    }
    return "02:00:00:00:00:00";
  }

  /**
   * Return an ordered list of ABIs supported by this device. The most preferred ABI is the first
   * element in the list.
   *
   * @return an ordered list of ABIs supported by this device
   */
  public static String[] getABIs() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      return Build.SUPPORTED_ABIS;
    } else {
      if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
        return new String[]{Build.CPU_ABI, Build.CPU_ABI2};
      }
      return new String[]{Build.CPU_ABI};
    }
  }

  /**
   * The brand (e.g., Google) the software is customized for, if any.
   */
  public static String brand() {
    return Build.BRAND;
  }

  public static String lang() {
    Locale locale;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      locale = LocaleList.getDefault().get(0);
    } else {
      locale = Locale.getDefault();
    }
    return locale.getLanguage() + "-" + locale.getCountry();
  }

  /**
   * isSdkLegal >= |min|
   *
   * @param min min isSdkLegal level
   * @return true is matched
   */
  public static boolean isSdkLegal(int min) {
    return Build.VERSION.SDK_INT >= min;
  }

  /**
   * |min| <= isSdkRange <= |max|
   *
   * @param min min isSdkRange level
   * @param max max isSdkRange level
   * @return true is matched
   */
  public static boolean isSdkRange(int min, int max) {
    return inRange(Build.VERSION.SDK_INT, min, max);
  }

  public static boolean isTargetSdks(int... targets) {
    final int sdk = Build.VERSION.SDK_INT;
    for (int target : targets) {
      if (sdk == target) {
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings("deprecation")
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static String cpuArch() {
    if (mCPUArch == null) {
      /**
       * Email from yuming.li@intel.com in 2014/06/27 said their new x86 ROM modifies the
       * android.os.abi to make the Build.CPU_ABI to always return "armeabi-v7a" and recommended
       * following method to get real CPU arch.
       */
      BufferedReader ibr = null;
      try {
        Process process = Runtime.getRuntime().exec("getprop ro.product.cpu.abi");
        ibr = new BufferedReader(new InputStreamReader(process.getInputStream()));
        mCPUArch = ibr.readLine();
      } catch (IOException ignored) {
      } finally {
        if (ibr != null) {
          try {
            ibr.close();
          } catch (IOException ignored) {
          }
        }
      }

      if (TextUtils.isEmpty(mCPUArch)) {
        // if meet something wrong, get cpu arch from android isSdkLegal.
        mCPUArch = isSdkLegal(21) ? Build.SUPPORTED_ABIS[0] : Build.CPU_ABI;
      }
    }

    return mCPUArch;
  }

  /**
   * Get total ram size of this device.
   *
   * @return -- ram size in byte.
   */
  public static long ramSize() {
    if (mRamSize == 0) {
      BufferedReader br = null;
      try {
        br = new BufferedReader(new FileReader("/proc/meminfo"), 1024);
        String line = br.readLine();

        /*
         * # cat /proc/meminfo MemTotal: 94096 kB MemFree: 1684 kB
         */
        if (!TextUtils.isEmpty(line)) {
          String[] splits = line.split("\\s+");
          if (splits.length > 1) {
            mRamSize = Long.valueOf(splits[1]) * UnitHelper.BYTES_PER_KB;
          }
        }
      } catch (IOException ignored) {
      } finally {
        if (br != null) {
          try {
            br.close();
          } catch (IOException ignored) {
          }
        }
      }
    }

    return mRamSize;
  }

  public static boolean inRange(int value, int min, int max) {
    return value >= min && value <= max;
  }

  public static String resolution() {
    DisplayMetrics displayMetrics = ContextManager.resources().getDisplayMetrics();
    int width = displayMetrics.widthPixels;
    int height = displayMetrics.heightPixels;
    return width + "*" + height;
  }

  public static String timezoneUTC() {
    try {
      return TimeZone.getTimeZone("UTC").getDisplayName();
    } catch (Exception e) {
      return TextHelper.EMPTY;
    }
  }

  public static String browserKernel() {
    return VERSION.SDK_INT > VERSION_CODES.KITKAT ? "chromium" : "webkit";
  }

  public static String browserKernelVersion() {
    try {
      String ua;
      if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
        ua = WebSettings.getDefaultUserAgent(ContextManager.appContext());
      } else {
        WebView webView = new WebView(ContextManager.appContext());
        ua = webView.getSettings().getUserAgentString();
      }
      if (VERSION.SDK_INT > VERSION_CODES.KITKAT) {
        return getWebkitVerFromUA(ua);
      } else {
        return getChromiumVerFromUA(ua);
      }

    } catch (Throwable e) {
      return TextHelper.EMPTY;
    }
  }

  public static String getChromiumVerFromUA(String ua) {
    if (TextUtils.isEmpty(ua)) {
      return TextHelper.EMPTY;
    }
    StringBuilder chromiumVer = new StringBuilder();
    for (int i = ua.indexOf("Chrome/"); i < ua.length(); i++) {
      char c = ua.charAt(i);
      if (' ' == c) {
        break;
      }
      chromiumVer.append(c);
    }
    return chromiumVer.toString();
  }

  public static String getWebkitVerFromUA(String ua) {
    if (TextUtils.isEmpty(ua)) {
      return TextHelper.EMPTY;
    }
    StringBuilder webkitVer = new StringBuilder();
    for (int i = ua.indexOf("AppleWebKit/"); i < ua.length(); i++) {
      char c = ua.charAt(i);
      if (' ' == c) {
        break;
      }
      webkitVer.append(c);
    }
    return webkitVer.toString();
  }
}
