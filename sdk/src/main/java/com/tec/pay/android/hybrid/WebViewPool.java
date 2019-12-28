package com.tec.pay.android.hybrid;

import android.content.Context;
import android.content.MutableContextWrapper;
import com.tec.pay.android.base.component.BaseHandler;
import com.tec.pay.android.hybrid.core.HybridWebView;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * WebViewPool class.
 *
 * @author Lucas Cheung.
 * @date 2019-06-07.
 */
public class WebViewPool {

  /**
   * 引用类型的原子类
   */
  private static final AtomicReference<WebViewPool> ATOMIC_REFERENCE = new AtomicReference<>();

  private final Queue<HybridWebView> mWebViews;
  private final Object lock = new Object();

  private WebViewPool() {
    mWebViews = new LinkedBlockingQueue<>();
  }

  public static WebViewPool getInstance() {
    for (; ; ) {
      if (ATOMIC_REFERENCE.get() != null) {
        return ATOMIC_REFERENCE.get();
      }
      if (ATOMIC_REFERENCE.compareAndSet(null, new WebViewPool())) {
        return ATOMIC_REFERENCE.get();
      }
    }
  }

  public void prepareWebView(final Context context, final int size) {
    BaseHandler.post(new Runnable() {
      @Override
      public void run() {
        int needToPrepareCount = size - mWebViews.size();
        if (needToPrepareCount <= 0) {
          return;
        }
        for (int i = 0; i < needToPrepareCount; i++) {
          mWebViews.offer(create(context));
        }
      }
    });
  }

  /**
   * 页面销毁 （1）去除WebView的上下文，避免内存泄漏 （2）加入缓存
   */
  public final void recycle(final HybridWebView webView) {
    if (webView == null) {
      return;
    }
    BaseHandler.post(new Runnable() {
      @Override
      public void run() {
        webView.unBind();
        store(webView);
      }
    });
  }

  /**
   * 页面加入浏览器 （1）缓存没有，则新建webView
   */
  public final HybridWebView acquireWebView(Context context, IHybridFactory hybridFactory) {

    HybridWebView mWebView = mWebViews.poll();

    if (mWebView == null) {
      synchronized (lock) {
        mWebView = create(context);
      }
    } else {
      if (mWebView.getContext() instanceof MutableContextWrapper) {
        MutableContextWrapper mMutableContextWrapper =
            (MutableContextWrapper) mWebView.getContext();
        mMutableContextWrapper.setBaseContext(context);
      }
    }
    mWebView.bind(hybridFactory);
    return mWebView;
  }

  HybridWebView create(Context context) {
    HybridWebView view = new HybridWebView(new MutableContextWrapper(context));
    view.init();
    return view;
  }

  private void store(final HybridWebView webView) {
    if (webView.getContext() instanceof MutableContextWrapper) {
      MutableContextWrapper mContext = (MutableContextWrapper) webView.getContext();
      mContext.setBaseContext(mContext.getApplicationContext());
      webView.destroy();
      mWebViews.offer(create(webView.getContext().getApplicationContext()));
    }
  }

  public final void clear() {
    while (mWebViews.size() != 0) {
      HybridWebView webView = mWebViews.poll();
      webView.destroy();
    }
  }
}
