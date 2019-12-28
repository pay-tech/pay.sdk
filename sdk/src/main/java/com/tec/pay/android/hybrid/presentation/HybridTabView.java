package com.tec.pay.android.hybrid.presentation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.tec.pay.android.base.utils.NetUtils;
import com.tec.pay.android.hybrid.IHybridFactory;
import com.tec.pay.android.hybrid.IHybridObserver;
import com.tec.pay.android.hybrid.IHybridRouter;
import com.tec.pay.android.hybrid.WebViewPool;
import com.tec.pay.android.hybrid.core.HybridWebView;
import com.tec.pay.android.hybrid.data.HybridDataManager;

/**
 * HybridTabView class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-27.
 */
public class HybridTabView extends FrameLayout {

  private HybridWebView mHybridWebView;
  private HybridWebViewModel mViewModel;

  public HybridTabView(@NonNull Context context) {
    this(context, null);
  }

  public HybridTabView(@NonNull Context context,
      @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public HybridTabView(@NonNull Context context,
      @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    mViewModel = new HybridWebViewModel(HybridDataManager.instance());
    mHybridWebView = WebViewPool.getInstance().acquireWebView(context, new IHybridFactory() {
      @NonNull
      @Override
      public IHybridClient client() {
        return getViewModel();
      }

      @NonNull
      @Override
      public IHybridObserver observer() {
        return getViewModel();
      }

      @NonNull
      @Override
      public IHybridRouter router() {
        return getViewModel();
      }
    });
    addView(mHybridWebView, 0);
  }

  HybridWebViewModel getViewModel() {
    return mViewModel;
  }

  public void loadUri(String url) {
    if (NetUtils.isNetworkAvailable(getContext())) {
      mHybridWebView.loadUri(url);
    } else {
      showErrorView(-1, "network is unavailable");
    }
  }

  private void showErrorView(int code, String msg) {

  }
}
