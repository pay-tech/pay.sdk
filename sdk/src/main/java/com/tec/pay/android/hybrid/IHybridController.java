package com.tec.pay.android.hybrid;

import com.tec.pay.android.hybrid.model.ResponseBody;

/**
 * IHybridController class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-31.
 */
public interface IHybridController {

  void onPayComplete(ResponseBody responseBody);
}
