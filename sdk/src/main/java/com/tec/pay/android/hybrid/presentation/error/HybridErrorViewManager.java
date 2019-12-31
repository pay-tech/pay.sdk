package com.tec.pay.android.hybrid.presentation.error;


import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tec.pay.android.R;
import com.tec.pay.android.base.log.DLog;
import com.tec.pay.android.base.utils.NetUtils;

public class HybridErrorViewManager {

  private OnErrorViewListener onErrorViewListener;
  private View mErrorView;
  private View mRetryButton;
  private View mCloseView;
  private TextView mReasonTv;
  private int mCode = 0;
  private String mErrMsg = "";

  public View getErrorView() {
    return mErrorView;
  }

  public HybridErrorViewManager(Context context) {
    mErrorView = LayoutInflater.from(context).inflate(R.layout.tec_layout_failed, null, false);
    mRetryButton = mErrorView.findViewById(R.id.bt_retry);
    mCloseView = mErrorView.findViewById(R.id.iv_close);
    mReasonTv = mErrorView.findViewById(R.id.tv_error);
    mCloseView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (onErrorViewListener != null) {
          onErrorViewListener.onClose(getErrorView(), mErrMsg, mCode);
        }
      }
    });
    mRetryButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!canRefresh()) {
          return;
        }
        if (onErrorViewListener != null) {
          onErrorViewListener.onRetry(getErrorView(), mErrMsg, mCode);
        }
      }
    });
  }

  public void show(@NonNull ViewGroup container, String reason, int code) {
    if (mErrorView.getParent() == null) {
      container.addView(mErrorView);
    } else {
      DLog.e("remove error view first!");
    }
    if (!TextUtils.isEmpty(reason)) {
      mReasonTv.setText(reason);
      mErrMsg = reason;
    }
    this.mCode = code;
  }

  public void close(@NonNull ViewGroup container) {
    container.removeView(mErrorView);
  }

  public void setOnErrorViewListener(OnErrorViewListener onErrorViewListener) {
    this.onErrorViewListener = onErrorViewListener;
  }

  public interface OnErrorViewListener {

    void onRetry(View errorView, String reason, int code);

    void onClose(View errorView, String reason, int code);
  }

  private boolean canRefresh() {
    // net::ERR_NAME_NOT_RESOLVED will not show callback again when refresh on 4.4 device
    return NetUtils.isNetworkAvailable(mErrorView.getContext())
        && !"net::ERR_NAME_NOT_RESOLVED".equals(mErrMsg);
  }
}
