package com.tec.pay.android.hybrid.presentation.tab;

import com.tec.pay.android.base.frame.MvpView;

/**
 * ITabView class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-30.
 */
public interface ITabView extends MvpView {

  void startProgressBar();

  void finishProgressBar();
}
