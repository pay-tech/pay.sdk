package com.tec.pay.android.base.utils;

import android.text.TextUtils;
import java.io.File;
import java.util.Collection;

/**
 * Validator class.
 *
 * @author Lucas Cheung.
 * @date 2019/4/18.
 */
public class Validator {

  public static void notNull(Object arg, String name) throws NullPointerException {
    if (arg == null) {
      throw new NullPointerException("Argument '" + name + "' cannot be null");
    }
  }

  public static void notEmpty(String str, String name) throws IllegalArgumentException {
    if (TextUtils.isEmpty(str)) {
      throw new IllegalArgumentException("String '" + name + "' cannot be empty");
    }
  }

  public static void fileExists(File file, String name) throws RuntimeException {
    if (file == null || !file.exists()) {
      throw new RuntimeException("File '" + name + "' is not exists!");
    }
  }

  public static <T> void notEmpty(Collection<T> container, String name)
      throws IllegalArgumentException {
    if (container.isEmpty()) {
      throw new IllegalArgumentException("Container '" + name + "' cannot be empty");
    }
  }

  public static <T> void containsNoNulls(Collection<T> container, String name)
      throws NullPointerException {
    Validator.notNull(container, name);
    for (T item : container) {
      if (item == null) {
        throw new NullPointerException("Container '" + name + "' cannot contain null values");
      }
    }
  }

  public static void containsNoNullOrEmpty(Collection<String> container, String name)
      throws NullPointerException, IllegalArgumentException {
    Validator.notNull(container, name);
    for (String item : container) {
      if (item == null) {
        throw new NullPointerException("Container '" + name + "' cannot contain null values");
      }
      if (item.length() == 0) {
        throw new IllegalArgumentException("Container '" + name + "' cannot contain empty values");
      }
    }
  }
}
