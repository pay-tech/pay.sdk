package com.tec.pay.android.base.utils;

import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * ObjectUtil class.
 *
 * @author Lucas Cheung.
 * @date 2019-06-15.
 */
public class ObjectUtils {

  private ObjectUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  public static boolean isEmpty(Object obj) {
    if (obj == null) {
      return true;
    } else if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
      return true;
    } else if (obj instanceof CharSequence && obj.toString().length() == 0) {
      return true;
    } else if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
      return true;
    } else if (obj instanceof Map && ((Map) obj).isEmpty()) {
      return true;
    } else if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
      return true;
    } else if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
      return true;
    } else if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
      return true;
    } else if (VERSION.SDK_INT >= 18 && obj instanceof SparseLongArray
        && ((SparseLongArray) obj).size() == 0) {
      return true;
    } else {
      return VERSION.SDK_INT >= 16 && obj instanceof android.util.LongSparseArray
          && ((android.util.LongSparseArray) obj).size() == 0;
    }
  }

  public static boolean isEmpty(CharSequence obj) {
    return obj == null || obj.toString().length() == 0;
  }

  public static boolean isEmpty(Collection obj) {
    return obj == null || obj.isEmpty();
  }

  public static boolean isEmpty(Map obj) {
    return obj == null || obj.isEmpty();
  }

  public static boolean isEmpty(SparseArray obj) {
    return obj == null || obj.size() == 0;
  }

  public static boolean isEmpty(SparseBooleanArray obj) {
    return obj == null || obj.size() == 0;
  }

  public static boolean isEmpty(SparseIntArray obj) {
    return obj == null || obj.size() == 0;
  }

  @RequiresApi(
      api = 18
  )
  public static boolean isEmpty(SparseLongArray obj) {
    return obj == null || obj.size() == 0;
  }

  @RequiresApi(
      api = 16
  )
  public static boolean isEmpty(android.util.LongSparseArray obj) {
    return obj == null || obj.size() == 0;
  }

  public static boolean equals(Object o1, Object o2) {
    return o1 == o2 || o1 != null && o1.equals(o2);
  }

  public static void requireNonNull(Object... objects) {
    if (objects == null) {
      throw new NullPointerException();
    } else {
      Object[] var1 = objects;
      int var2 = objects.length;

      for (int var3 = 0; var3 < var2; ++var3) {
        Object object = var1[var3];
        if (object == null) {
          throw new NullPointerException();
        }
      }

    }
  }

  public static <T> T getOrDefault(T object, T defaultObject) {
    return object == null ? defaultObject : object;
  }

  public static int hashCode(Object o) {
    return o != null ? o.hashCode() : 0;
  }
}
