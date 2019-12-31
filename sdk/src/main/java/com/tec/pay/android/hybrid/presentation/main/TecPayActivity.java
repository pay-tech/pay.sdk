package com.tec.pay.android.hybrid.presentation.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.TextView;
import com.tec.pay.android.R;
import com.tec.pay.android.base.component.ContextManager;
import com.tec.pay.android.base.log.DLog;
import com.tec.pay.android.hybrid.presentation.tab.HybridTabView;

public class TecPayActivity extends Activity implements ITecPayView {

  public static final String KEY_URL = "key_url";
  private String mUrl;
  private HybridTabView mHybridTabView;
  private TextView mTitleView;
  private TecPayPresenter mPresenter;

  public static void show(Context context, String url) {
    if (TextUtils.isEmpty(url)) {
      DLog.e("url is empty!!");
      return;
    }
    Intent intent = new Intent(context, TecPayActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra(KEY_URL, url);
    context.startActivity(intent);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    if (intent == null) {
      return;
    }
    mUrl = intent.getStringExtra(KEY_URL);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mPresenter = new TecPayPresenter();
    mPresenter.attachView(this);
    ContextManager.update(this);
    setContentView(R.layout.tec_pay_activity);
    //设置窗口周围触摸不消失
    this.setFinishOnTouchOutside(false);
    //设置窗口周围透明
    getWindow().setDimAmount(0f);
    try {
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    } catch (Exception ignored) {
    }
    mUrl = getIntent().getStringExtra(KEY_URL);
    if (TextUtils.isEmpty(mUrl)) {
      DLog.e("Url cannot be null!");
      finish();
      return;
    }
    mHybridTabView = findViewById(R.id.pay_web);

    mHybridTabView.attachParentPresenter(mPresenter);
    mHybridTabView.loadUri(mUrl);
    findViewById(R.id.pay_close).setOnClickListener(view -> {
      finish();
    });
    mTitleView = findViewById(R.id.pay_title);
  }

  @Override
  public void onBackPressed() {
    if (mHybridTabView.goBack()) {
      return;
    }
    super.onBackPressed();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mPresenter.detachView();
    mPresenter.destroy();
    ContextManager.update(null);
    mHybridTabView.destroy();
  }

  @Override
  public void setTitle(String title) {
    mTitleView.setText(title);
  }
}
