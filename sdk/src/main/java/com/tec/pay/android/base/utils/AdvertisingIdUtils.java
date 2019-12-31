package com.tec.pay.android.base.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import com.tec.pay.android.base.log.DLog;
import java.lang.reflect.Method;
import java.util.concurrent.LinkedBlockingQueue;

public class AdvertisingIdUtils {

  private static final String ADVERTISING_ID_CLIENT_CLASS_NAME =
      "com.google.android.gms.ads.identifier.AdvertisingIdClient";

  public static boolean isAdvertisingIdAvailable() {
    boolean advertisingIdAvailable = false;
    try {
      Class.forName(ADVERTISING_ID_CLIENT_CLASS_NAME);
      advertisingIdAvailable = true;
    } catch (ClassNotFoundException ignored) {
    }
    return advertisingIdAvailable;
  }

  private static String getAdvertisingIdFromApi(final Context context) throws Throwable {
    try {
      final Class<?> cls = Class.forName(ADVERTISING_ID_CLIENT_CLASS_NAME);
      final Method getAdvertisingIdInfo = cls.getMethod("getAdvertisingIdInfo", Context.class);
      Object info = getAdvertisingIdInfo.invoke(null, context);
      if (info != null) {
        final Method getId = info.getClass().getMethod("getId");
        Object id = getId.invoke(info);
        return (String) id;
      }
    } catch (Throwable ignored) {
    }
    return null;
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private static boolean isLimitAdTrackingEnabled(final Context context) {
    try {
      final Class<?> cls = Class.forName(ADVERTISING_ID_CLIENT_CLASS_NAME);
      final Method getAdvertisingIdInfo = cls.getMethod("getAdvertisingIdInfo", Context.class);
      Object info = getAdvertisingIdInfo.invoke(null, context);
      if (info != null) {
        final Method getId = info.getClass().getMethod("isLimitAdTrackingEnabled");
        Object id = getId.invoke(info);
        return (boolean) id;
      }
    } catch (Throwable t) {
      if (t.getCause() != null
          && t.getCause().getClass().toString().contains("java.lang.ClassNotFoundException")
          && t.getCause()
          .getMessage()
          .contains("com.google.android.gms.ads.identifier.AdvertisingIdClient")) {
        DLog.w(
            "Play Services are not available, while checking if limited ad tracking enabled");
      }
    }
    return false;
  }

  /**
   * Cache advertising ID for attribution
   */
  @WorkerThread
  public static String getAdvertisingID(final Context context) {
    try {
      if (!isLimitAdTrackingEnabled(context)) {
        String adId = getAdvertisingIdFromApi(context);
        if (TextUtils.isEmpty(adId)) {
          adId = getRemoteGAIDString(context);
        }
        return adId;
      }
    } catch (Throwable t) {
      if (t.getCause() != null
          && t.getCause()
          .getClass()
          .toString()
          .contains("GooglePlayServicesAvailabilityException")) {
        DLog.i("Advertising ID cannot be determined yet, while caching");
      } else if (t.getCause() != null
          && t.getCause()
          .getClass()
          .toString()
          .contains("GooglePlayServicesNotAvailableException")) {
        DLog.w(
            "Advertising ID cannot be determined because Play Services are not available, while caching");
      } else if (t.getCause() != null
          && t.getCause().getClass().toString().contains("java.lang.ClassNotFoundException")
          && t.getCause()
          .getMessage()
          .contains("com.google.android.gms.ads.identifier.AdvertisingIdClient")) {
        DLog.w("Play Services are not available, while caching advertising id");
      } else {
        // unexpected
        DLog.e("Couldn't get advertising ID, while caching", t);
      }
    }
    return "";
  }

  private static String getRemoteGAIDString(Context context) {
    try {
      PackageManager pm = context.getPackageManager();
      pm.getPackageInfo("com.android.vending", 0);
    } catch (Exception e) {
      DLog.w(e.toString());
    }

    String aid = null;
    try {
      GAIDConnection connection = new GAIDConnection();
      Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
      intent.setPackage("com.google.android.gms");
      if (context.bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
        try {
          GAIDBinder adInterface = new GAIDBinder(connection.getBinder());
          aid = adInterface.getId();
          return aid;
        } catch (Exception e) {
          DLog.w(e.toString());
        } finally {
          context.unbindService(connection);
        }
      }
    } catch (Exception e) {
      DLog.w(e.toString());
    }
    return aid;
  }

  private static final class GAIDConnection implements ServiceConnection {

    boolean retrieved = false;
    private final LinkedBlockingQueue<IBinder> queue = new LinkedBlockingQueue<>(1);

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      try {
        this.queue.put(service);
      } catch (InterruptedException ignored) {
      }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }

    IBinder getBinder() throws InterruptedException {
      if (this.retrieved) {
        throw new IllegalStateException();
      }
      this.retrieved = true;
      return this.queue.take();
    }
  }

  private static final class GAIDBinder implements IInterface {

    private IBinder binder;

    GAIDBinder(IBinder pBinder) {
      binder = pBinder;
    }

    @Override
    public IBinder asBinder() {
      return binder;
    }

    public String getId() throws RemoteException {
      Parcel data = Parcel.obtain();
      Parcel reply = Parcel.obtain();
      String id;
      try {
        data.writeInterfaceToken(
            "com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
        binder.transact(1, data, reply, 0);
        reply.readException();
        id = reply.readString();
      } finally {
        reply.recycle();
        data.recycle();
      }
      return id;
    }
  }
}
