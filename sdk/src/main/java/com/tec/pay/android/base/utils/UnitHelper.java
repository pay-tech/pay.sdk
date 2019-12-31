package com.tec.pay.android.base.utils;

import java.text.DecimalFormat;

public final class UnitHelper {

  public static final long BYTES_PER_KB = 1024L;
  public static final long BYTES_PER_MB = BYTES_PER_KB * BYTES_PER_KB;
  public static final long BYTES_PER_GB = BYTES_PER_KB * BYTES_PER_MB;

  public static String formatBytesInByte(long size, boolean withUnit) {
    DecimalFormat formatter;
    double formatSize;
    if (size > BYTES_PER_GB) {
      formatter = new DecimalFormat("#0.00");
      formatSize = size / (float) (BYTES_PER_GB);
    } else if (size > BYTES_PER_MB) {
      formatter = new DecimalFormat("#0.0");
      formatSize = size / (float) (BYTES_PER_MB);
    } else if (size > BYTES_PER_KB) {
      formatter = new DecimalFormat("#0.0");
      formatSize = size / (float) (BYTES_PER_KB);
    } else {
      formatter = new DecimalFormat("#0");
      formatSize = size;
    }

    return formatter.format(formatSize) + (withUnit ? formatUnit(size) : "");
  }

  public static String formatBytesInByte(long size) {
    return formatBytesInByte(size, true);
  }

  public static String formatBytesInKByte(long size) {
    DecimalFormat formatter;
    double formatSize;
    if (size > BYTES_PER_GB) {
      formatter = new DecimalFormat("#0.00");
      formatSize = size / (float) (BYTES_PER_GB);
      return formatter.format(formatSize) + "G";
    } else if (size > BYTES_PER_MB) {
      formatter = new DecimalFormat("#0.0");
      formatSize = size / (float) (BYTES_PER_MB);
      return formatter.format(formatSize) + "M";
    } else {
      formatter = new DecimalFormat("#0.0");
      formatSize = size / (float) (BYTES_PER_MB);
      return formatter.format(formatSize) + "K";
    }
  }

  public static String formatGBBytesInByte(long size) {
    DecimalFormat formatter = new DecimalFormat("#0.00");
    return formatter.format(size / (float) (BYTES_PER_KB)) + "GB";
  }

  public static String formatUnit(long size) {
    String strUnit = "";
    if (size > BYTES_PER_GB) {
      strUnit = "G";
    } else if (size > BYTES_PER_MB) {
      strUnit = "M";
    } else if (size > BYTES_PER_KB) {
      strUnit = "K";
    } else {
      strUnit = "B";
    }
    return strUnit;
  }

  public static boolean compareSize(long size1, long size2) {
    float f1;
    float f2;
    if (size1 > BYTES_PER_GB) {
      f1 = size1 / (float) (BYTES_PER_GB);
    } else if (size1 > BYTES_PER_MB) {
      f1 = size1 / (float) (BYTES_PER_MB);
    } else if (size1 > BYTES_PER_KB) {
      f1 = size1 / (float) (BYTES_PER_KB);
    } else {
      f1 = size1;
    }

    if (size2 > BYTES_PER_GB) {
      f2 = size2 / (float) (BYTES_PER_GB);
    } else if (size2 > BYTES_PER_MB) {
      f2 = size2 / (float) (BYTES_PER_MB);
    } else if (size2 > BYTES_PER_KB) {
      f2 = size2 / (float) (BYTES_PER_KB);
    } else {
      f2 = size2;
    }

    int i1, i2;
    i1 = (int) f1;
    i2 = (int) f2;

    return (i1 == i2);
  }
}
