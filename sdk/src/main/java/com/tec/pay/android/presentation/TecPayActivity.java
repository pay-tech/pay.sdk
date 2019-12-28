package com.tec.pay.android.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import com.tec.pay.android.R;
import com.tec.pay.android.base.component.ContextManager;
import com.tec.pay.android.base.log.DLog;
import com.tec.pay.android.hybrid.presentation.HybridTabView;

public class TecPayActivity extends Activity {

  public static final String KEY_URL = "key_url";
  private String mUrl;
  private HybridTabView mHybridTabView;

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
//    if (!TextUtils.isEmpty(mUrl) && mWebController != null) {
//      mWebController.showNewTab(mUrl);
//    } else {
//      DLog.e("url is empty!!");
//      finish();
//    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
    mHybridTabView.loadUri(mUrl);
    findViewById(R.id.pay_close).setOnClickListener(view -> {
      finish();
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    ContextManager.update(null);
  }
}
