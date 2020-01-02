package com.tec.pay.android.hybrid;

import com.tec.pay.android.base.data.BaseConstant;
import com.tec.pay.android.base.exception.TecException;

/**
 * HybridActionException class.
 *
 * @author Lucas Cheung.
 * @date 2020-01-02.
 */
public class HybridActionException extends TecException {

  public HybridActionException() {
    super(BaseConstant.MSG_ERROR_ACTION_NOT_BE_SUPPORT,
        BaseConstant.CODE_ERROR_ACTION_NOT_BE_SUPPORT);
  }
}
