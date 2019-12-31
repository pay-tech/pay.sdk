package com.tec.pay.android.base.utils;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.TypedValue;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ConvertUtils class.
 *
 * @author Lucas Cheung.
 * @date 2019-09-03.
 */
public class ConvertUtils {

  public static final int BYTE = 1;
  public static final int KB = 1024;
  public static final int MB = 1048576;
  public static final int GB = 1073741824;

  /**
   * Input stream to output stream.
   *
   * @param is The input stream.
   * @return output stream
   */
  public static ByteArrayOutputStream input2OutputStream(final InputStream is) {
    if (is == null) {
      return null;
    }
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      byte[] b = new byte[KB];
      int len;
      while ((len = is.read(b, 0, KB)) != -1) {
        os.write(b, 0, len);
      }
      return os;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      try {
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Input stream to bytes.
   *
   * @param is The input stream.
   * @return bytes
   */
  public static byte[] inputStream2Bytes(final InputStream is) {
    if (is == null) {
      return null;
    }
    return input2OutputStream(is).toByteArray();
  }

  @IntDef({BYTE, KB, MB, GB})
  @Retention(RetentionPolicy.SOURCE)
  public @interface Unit {

  }

  /**
   * dp转px
   */
  public static int dp2px(Context context, float dpVal) {
    return (int)
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
  }
}
