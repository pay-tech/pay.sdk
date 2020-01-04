package com.tec.pay.android.base.utils;

import android.support.annotation.NonNull;
import com.tec.pay.android.base.log.DLog;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * DateUtils class.
 *
 * @author Lucas Cheung.
 * @date 2020-01-03.
 */
public class DateUtils {

  private static final ThreadLocal<DateFormat> utcHolder =
      new ThreadLocal<DateFormat>() {
        @NonNull
        @Override
        protected DateFormat initialValue() {
          TimeZone tz = TimeZone.getTimeZone("UTC");
          DateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
          iso8601.setTimeZone(tz);
          return iso8601;
        }
      };

  public static String getUTCTimeStr() {

    StringBuffer UTCTimeBuffer = new StringBuffer();
    // 1、取得本地时间：
    Calendar cal = Calendar.getInstance();

    // 2、取得时间偏移量：
    int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
    // 3、取得夏令时差：
    int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
    // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
    cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) + 1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    int minute = cal.get(Calendar.MINUTE);
    UTCTimeBuffer.append(year).append("-").append(month).append("-").append(day);
    UTCTimeBuffer.append(" ").append(hour).append(":").append(minute);
    try {
      utcHolder.get().parse(UTCTimeBuffer.toString());
      return UTCTimeBuffer.toString();
    } catch (Exception e) {
      DLog.e(e);
    }
    return TextHelper.EMPTY_STRING;
  }

  public static String getUTCTimeZone() {
    try {
      Calendar mDummyDate;
      mDummyDate = Calendar.getInstance();
      Calendar now = Calendar.getInstance();
      mDummyDate.setTimeZone(now.getTimeZone());
      mDummyDate.set(now.get(Calendar.YEAR), 11, 31, 13, 0, 0);
      SimpleDateFormat gmtFormatter = new SimpleDateFormat("ZZZZ");
      gmtFormatter.setTimeZone(now.getTimeZone());
      String gmtString = gmtFormatter.format(new Date());
      try {
        return gmtString.replace("GMT", "UTC");
      } catch (Throwable e) {
        return gmtString;
      }
    } catch (Exception e) {
      return TextHelper.EMPTY_STRING;
    }
  }
}
