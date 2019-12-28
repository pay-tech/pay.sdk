package com.tec.pay.sample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import com.tec.pay.android.TecPay;
import com.tec.pay.android.hybrid.core.BridgeWebView;
import com.tec.pay.android.hybrid.core.DefaultHandler;
import com.tec.pay.android.presentation.TecPayActivity;

public class MainActivity extends Activity implements OnClickListener {

  private final String TAG = "MainActivity";

  BridgeWebView webView;

  Button button;

  int RESULT_CODE = 0;

  ValueCallback<Uri> mUploadMessage;

  ValueCallback<Uri[]> mUploadMessageArray;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    webView = (BridgeWebView) findViewById(R.id.webView);

    button = (Button) findViewById(R.id.button);

    button.setOnClickListener(this);

    webView.setDefaultHandler(new DefaultHandler());

    webView.setWebChromeClient(new WebChromeClient() {

      @SuppressWarnings("unused")
      public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
        this.openFileChooser(uploadMsg);
      }

      @SuppressWarnings("unused")
      public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
        this.openFileChooser(uploadMsg);
      }

      public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        pickFile();
      }

      @Override
      public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
          FileChooserParams fileChooserParams) {
        mUploadMessageArray = filePathCallback;
        pickFile();
        return true;
      }
    });

//    webView.loadUrl("file:///android_asset/demo.html");
    TecPay.init(getApplicationContext(), null, null);
  }

  public void pickFile() {
    Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
    chooserIntent.setType("image/*");
    startActivityForResult(chooserIntent, RESULT_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (requestCode == RESULT_CODE) {
      if (null == mUploadMessage && null == mUploadMessageArray) {
        return;
      }
      if (null != mUploadMessage && null == mUploadMessageArray) {
        Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;
      }

      if (null == mUploadMessage && null != mUploadMessageArray) {
        Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
        mUploadMessageArray.onReceiveValue(new Uri[]{result});
        mUploadMessageArray = null;
      }

    }
  }

  @Override
  public void onClick(View v) {
    if (button.equals(v)) {
      TecPayActivity.show(this, "file:///android_asset/demo.html");
    }

  }

}
