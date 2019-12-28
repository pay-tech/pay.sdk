package com.tec.pay.android.hybrid;

import android.support.annotation.NonNull;
import com.tec.pay.android.hybrid.presentation.IHybridClient;

public interface IHybridFactory {

  @NonNull
  IHybridClient client();

  @NonNull
  IHybridObserver observer();

  @NonNull
  IHybridRouter router();
}
