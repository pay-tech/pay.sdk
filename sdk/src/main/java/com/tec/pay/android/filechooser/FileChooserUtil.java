package com.tec.pay.android.filechooser;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

public class FileChooserUtil {

  private FileResultFragment mFragment;

  public FileChooserUtil(Activity activity) {
    FragmentManager fragmentManager = activity.getFragmentManager();
    mFragment = (FileResultFragment) fragmentManager.findFragmentByTag(FileResultFragment.TAG);
    if (mFragment == null) {
      mFragment = new FileResultFragment();
    }
    if (!mFragment.isAdded()) {
      fragmentManager
          .beginTransaction()
          .add(mFragment, FileResultFragment.TAG)
          .commitAllowingStateLoss();
      fragmentManager.executePendingTransactions();
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public void showFileChooser(
      ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserPara) {
    mFragment.showFileChooser(filePathCallback, fileChooserPara);
  }

  public void showFileChooser(ValueCallback<Uri> uploadFile, String acceptType) {
    mFragment.showFileChooser(uploadFile, acceptType);
  }
}
