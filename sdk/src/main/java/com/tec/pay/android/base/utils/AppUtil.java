package com.tec.pay.android.base.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.tec.pay.android.base.component.ContextManager;
import com.tec.pay.android.base.log.DLog;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AppUtil {
  // Property

  private static String sPackageName;
  private static String sVersionName;
  private static int sVersionCode = Integer.MIN_VALUE;
  private static String sProcessName;

  public static String getDefaultAuthority() {
    return String.format("%s.%s", AppUtil.packageName(), "TDWebContentProvider");
  }

  public static String packageName() {
    if (sPackageName == null) {
      sPackageName = ContextManager.appContext().getPackageName();
    }
    return sPackageName;
  }

  public static String versionName() {
    if (sVersionName == null) {
      try {
        sVersionName =
            ContextManager.appContext()
                .getPackageManager()
                .getPackageInfo(AppUtil.packageName(), 0)
                .versionName;
      } catch (Exception ignored) {
      }
      if (TextUtils.isEmpty(sVersionName)) {
        sVersionName = "null";
      }
    }
    return sVersionName;
  }

  public static int versionCode() {
    if (sVersionCode == Integer.MIN_VALUE) {
      try {
        sVersionCode =
            ContextManager.appContext()
                .getPackageManager()
                .getPackageInfo(AppUtil.packageName(), 0)
                .versionCode;
      } catch (Exception ignored) {
      }
    }
    return sVersionCode;
  }

  public static String processName() {
    return processName(ContextManager.appContext());
  }

  public static String processName(Context context) {
    if (sProcessName != null) {
      return sProcessName;
    }
    List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = null;
    int pid = pid();
    ActivityManager am = null;
    try {
      am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    } catch (Exception ignored) {
    }
    if (am != null) {
      runningAppProcessInfoList = am.getRunningAppProcesses();
    }
    if (runningAppProcessInfoList != null) {
      for (ActivityManager.RunningAppProcessInfo info : runningAppProcessInfoList) {
        if (info.pid == pid) {
          sProcessName = info.processName;
          break;
        }
      }
    }
    if (TextUtils.isEmpty(sProcessName)) {
      sProcessName = "";
    }
    return sProcessName;
  }

  public static int pid() {
    return android.os.Process.myPid();
  }

  public static String mainProcessName() {
    ApplicationInfo info = ContextManager.appContext().getApplicationInfo();
    return (info == null) ? "" : info.processName;
  }

  public static boolean currentlyOnMainThread() {
    return Looper.getMainLooper() == Looper.myLooper();
  }

  public static List<PackageInfo> getUserPackageInfoList() {
    // Return a List of all packages that are installed on the device.
    List<PackageInfo> userPag = new ArrayList<>();
    try {
      PackageManager pm = ContextManager.packageManager();
      List<PackageInfo> packages = pm.getInstalledPackages(0);
      for (PackageInfo packageInfo : packages) {
        // insert user's app
        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
          userPag.add(packageInfo);
        }
      }
    } catch (Exception ignored) {
    }
    return userPag;
  }

  private static String sUserAgent;

  public static String getUserAgent() {
    if (sUserAgent == null) {
      sUserAgent =
          String.format(
              "Mozilla/5.0 (Linux; Android %s; %s Build/%s) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Mobile Safari/537.36; mall_sdk",
              Build.VERSION.RELEASE, Build.MANUFACTURER, Build.ID);
    }
    return sUserAgent;
  }

  /**
   * get network state :none, wifi, 2G, 3G, 4G and mobile
   *
   * @return the state of network
   */
  public static String getNetworkState() {
    ConnectivityManager connManager = null;
    try {
      // 获取网络服务
      connManager = (ConnectivityManager)
          ContextManager.appContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    } catch (Exception ignored) {
    }
    if (null == connManager) {
      // 为空则认为无网络
      return "none";
    }
    // 获取网络类型，如果为空，返回无网络
    NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
    if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
      return "none";
    }
    // 判断是否为WIFI
    NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    if (null != wifiInfo) {
      NetworkInfo.State state = wifiInfo.getState();
      if (null != state) {
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
          return "wifi";
        }
      }
    }
    // 若不是WIFI，则去判断是2G、3G、4G网
    TelephonyManager telephonyManager;
    try {
      telephonyManager = (TelephonyManager) ContextManager.appContext()
          .getSystemService(Context.TELEPHONY_SERVICE);
    } catch (Exception e) {
      return "none";
    }
    int networkType = telephonyManager.getNetworkType();
    switch (networkType) {
        /*
        GPRS : 2G(2.5) General Packet Radia Service 114kbps
        EDGE : 2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
        UMTS : 3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
        CDMA : 2G 电信 Code Division Multiple Access 码分多址
        EVDO_0 : 3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
        EVDO_A : 3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
        1xRTT : 2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
        HSDPA : 3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
        HSUPA : 3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
        HSPA : 3G (分HSDPA,HSUPA) High Speed Packet Access
        IDEN : 2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）
        EVDO_B : 3G EV-DO Rev.B 14.7Mbps 下行 3.5G
        LTE : 4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G
        EHRPD : 3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
        HSPAP : 3G HSPAP 比 HSDPA 快些
        */
      // 2G网络
      case TelephonyManager.NETWORK_TYPE_GPRS:
      case TelephonyManager.NETWORK_TYPE_CDMA:
      case TelephonyManager.NETWORK_TYPE_EDGE:
      case TelephonyManager.NETWORK_TYPE_1xRTT:
      case TelephonyManager.NETWORK_TYPE_IDEN:
        return "2G";
      // 3G网络
      case TelephonyManager.NETWORK_TYPE_EVDO_A:
      case TelephonyManager.NETWORK_TYPE_UMTS:
      case TelephonyManager.NETWORK_TYPE_EVDO_0:
      case TelephonyManager.NETWORK_TYPE_HSDPA:
      case TelephonyManager.NETWORK_TYPE_HSUPA:
      case TelephonyManager.NETWORK_TYPE_HSPA:
      case TelephonyManager.NETWORK_TYPE_EVDO_B:
      case TelephonyManager.NETWORK_TYPE_EHRPD:
      case TelephonyManager.NETWORK_TYPE_HSPAP:
        return "3G";
      // 4G网络
      case TelephonyManager.NETWORK_TYPE_LTE:
        return "4G";
      default:
        return "mobile";
    }
  }

  static final String RELEASE_STAGE_DEVELOPMENT = "development";
  static final String RELEASE_STAGE_PRODUCTION = "production";

  public static String getReleaseStage() {
    ApplicationInfo info = ContextManager.appContext().getApplicationInfo();

    if (info != null) {
      if ((info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
        return RELEASE_STAGE_DEVELOPMENT;
      }
    }
    return RELEASE_STAGE_PRODUCTION;
  }

  private static Boolean sIsAppDebuggable;

  public static boolean isAppDebuggable() {
    if (sIsAppDebuggable != null) {
      return sIsAppDebuggable;
    }
    ApplicationInfo info = ContextManager.appContext().getApplicationInfo();
    if (info != null) {
      sIsAppDebuggable = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    } else {
      sIsAppDebuggable = false;
    }
    return sIsAppDebuggable;
  }

  public static boolean isVPNEnable() {
    if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
      return isVPNEnableV23();
    }
    return isVPNEnableV16();
  }

  private static boolean isVPNEnableV16() {
    List<String> networkList = new ArrayList<>();
    try {
      for (NetworkInterface networkInterface :
          Collections.list(NetworkInterface.getNetworkInterfaces())) {
        if (networkInterface.isUp()) {
          networkList.add(networkInterface.getName());
        }
      }
    } catch (Exception ex) {
      DLog.d("isVpnUsing Network List didn't received");
    }

    return networkList.contains("tun0");
  }

  @RequiresApi(api = VERSION_CODES.M)
  private static boolean isVPNEnableV23() {
    ConnectivityManager connectivityManager =
        (ConnectivityManager)
            ContextManager.appContext().getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络服务
    if (null == connectivityManager) {
      return false;
    }
    try {
      Network activeNetwork = connectivityManager.getActiveNetwork();
      NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(activeNetwork);
      return caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
    } catch (Exception e) {
      return isVPNEnableV16();
    }
  }
}
