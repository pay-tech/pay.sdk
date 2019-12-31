package com.tec.pay.android.hybrid;

import android.support.annotation.NonNull;

public interface IHybridFactory {

  @NonNull
  IHybridClient client();

  @NonNull
  IHybridObserver observer();

  @NonNull
  IHybridRouter router();
}
