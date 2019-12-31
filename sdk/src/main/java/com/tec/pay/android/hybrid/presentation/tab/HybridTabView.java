package com.tec.pay.android.hybrid.presentation.tab;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.tec.pay.android.base.utils.ConvertUtils;
import com.tec.pay.android.base.utils.LayoutHelper;
import com.tec.pay.android.base.utils.NetUtils;
import com.tec.pay.android.base.wediget.TecProgressBar;
import com.tec.pay.android.hybrid.IHybridClient;
import com.tec.pay.android.hybrid.IHybridFactory;
import com.tec.pay.android.hybrid.IHybridObserver;
import com.tec.pay.android.hybrid.IHybridRouter;
import com.tec.pay.android.hybrid.WebViewPool;
import com.tec.pay.android.hybrid.core.HybridWebView;
import com.tec.pay.android.hybrid.data.HybridDataManager;
import com.tec.pay.android.hybrid.presentation.main.TecPayPresenter;

/**
 * HybridTabView class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-27.
 */
public class HybridTabView extends FrameLayout implements ITabView {

  private static final String ERROR_URL = "about:blank";

  private HybridWebView mHybridWebView;
  private HybridWebPresenter mPresenter;
  private TecProgressBar mProgressBar;

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
    mPresenter = new HybridWebPresenter(HybridDataManager.instance());
    mHybridWebView = WebViewPool.getInstance().acquireWebView(context, new IHybridFactory() {
      @NonNull
      @Override
      public IHybridClient client() {
        return getPresenter();
      }

      @NonNull
      @Override
      public IHybridObserver observer() {
        return getPresenter();
      }

      @NonNull
      @Override
      public IHybridRouter router() {
        return getPresenter();
      }
    });
    addView(mHybridWebView, 0);
    mProgressBar = new TecProgressBar(context);
    mProgressBar.setMax(100);
    addView(mProgressBar, 1,
        LayoutHelper.createFL(LayoutHelper.MATCH, ConvertUtils.dp2px(context, 2)));
  }

  public void attachParentPresenter(TecPayPresenter presenter) {
    mPresenter.attachParent(presenter);
  }

  HybridWebPresenter getPresenter() {
    return mPresenter;
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

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    mPresenter.attachView(this);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    mPresenter.detachView();
  }

  public void destroy() {
    mPresenter.destroy();
  }

  public boolean goBack() {
    return mHybridWebView.goBack(ERROR_URL);
  }

  @Override
  public void startProgressBar() {
    if (mProgressBar == null) {
      return;
    }
    mProgressBar.setVisibility(View.VISIBLE);
    mProgressBar.setCurProgress(100, 6000, null);
  }

  @Override
  public void finishProgressBar() {
    if (mProgressBar == null) {
      return;
    }
    mProgressBar.setNormalProgress(100);
    mProgressBar.setVisibility(View.INVISIBLE);
  }
}
