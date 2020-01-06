package com.tec.pay.android;

import android.support.annotation.Keep;

/**
 * TecPayCallback class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-31.
 */
@Keep
public interface TecPayCallback {

  void onResult(TecPayResult result);
}
