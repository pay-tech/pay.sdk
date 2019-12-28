package com.tec.pay.android.filechooser;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

public class FileResultFragment extends Fragment {

  static final String TAG = "FileResultFragment";
  private static final int REQUEST_CODE = 200;
  private ValueCallback<Uri[]> mFilePathCallback;
  private ValueCallback<Uri> mUploadFile;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public void showFileChooser(
      ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserPara) {
    mFilePathCallback = filePathCallback;
    showFileChooser(fileChooserPara.createIntent(), String.valueOf(fileChooserPara.getTitle()));
  }

  public void showFileChooser(ValueCallback<Uri> uploadFile, String acceptType) {

    mUploadFile = uploadFile;
    Intent extraIntent = new Intent();
    extraIntent.setType(acceptType);
    showFileChooser(extraIntent, "Chooser");
  }

  private void showFileChooser(Intent intent, String title) {
    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
    chooserIntent.putExtra(Intent.EXTRA_INTENT, intent);
    chooserIntent.putExtra(Intent.EXTRA_TITLE, title);
    startActivityForResult(chooserIntent, REQUEST_CODE);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
      Uri[] results = null;
      String dataString = data.getDataString();
      if (dataString != null) {
        results = new Uri[]{Uri.parse(dataString)};
      } else {
        if (data.getClipData() != null) {
          final int numSelectedFiles = data.getClipData().getItemCount();
          results = new Uri[numSelectedFiles];
          for (int i = 0; i < numSelectedFiles; i++) {
            results[i] = data.getClipData().getItemAt(i).getUri();
          }
        }
      }

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        mFilePathCallback.onReceiveValue(results);
      } else {
        if (results != null && results.length > 0) {
          mUploadFile.onReceiveValue(results[0]);
        }
      }
    } else {
      if (mFilePathCallback != null) {
        mFilePathCallback.onReceiveValue(null);
        mFilePathCallback = null;
      }
      if (mUploadFile != null) {
        mUploadFile.onReceiveValue(null);
        mUploadFile = null;
      }
    }
  }
}
