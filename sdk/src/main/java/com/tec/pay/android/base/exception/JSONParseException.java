package com.tec.pay.android.base.exception;

import android.support.annotation.NonNull;
import com.tec.pay.android.base.data.BaseConstant;
import com.tec.pay.android.base.utils.TextHelper;

/**
 * JSONParseException class.
 *
 * @author Lucas Cheung.
 * @date 2020-01-02.
 */
public class JSONParseException extends TecException {

  public JSONParseException(@NonNull String msg) {
    super(String.format(BaseConstant.MSG_ERROR_JSON_PARSE, msg),
        BaseConstant.CODE_ERROR_JSON_PARSE);
  }

  public JSONParseException(Exception e) {
    this(TextHelper.ensureNotNull(e.getMessage()));
  }
}
