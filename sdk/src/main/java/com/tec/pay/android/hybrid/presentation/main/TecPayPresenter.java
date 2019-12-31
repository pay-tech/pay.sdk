package com.tec.pay.android.hybrid.presentation.main;

import com.tec.pay.android.base.component.ContextManager;
import com.tec.pay.android.base.frame.MvpBasePresenter;
import com.tec.pay.android.base.utils.Validator;
import com.tec.pay.android.controller.HybridController;
import com.tec.pay.android.hybrid.HybridConstant;
import com.tec.pay.android.hybrid.core.HybridWebView;
import com.tec.pay.android.hybrid.model.RequestBody;
import com.tec.pay.android.hybrid.model.ResponseBody;
import com.tec.pay.android.hybrid.model.SetTitleRequest;
import com.tec.pay.android.task.Task;
import org.json.JSONObject;

/**
 * HybridWebPresenter class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-27.
 */
public class TecPayPresenter extends MvpBasePresenter<ITecPayView> {

  TecPayPresenter() {
  }

  public Task<Boolean> handleMessage(HybridWebView view, RequestBody requestBody) {
    switch (requestBody.getService()) {
      case HybridConstant.SERVICE_UI:
        switch (requestBody.getAction()) {
          case HybridConstant.ACTION_CLOSE:

            return Task.call(() -> {
              if (ContextManager.activity() != null) {
                ContextManager.activity().finish();
              }
              return true;
            }, Task.UI_THREAD_EXECUTOR);
          case HybridConstant.ACTION_SET_TITLE:
            return Task.forResult(requestBody.getParams()).continueWith(task -> {
              JSONObject params = task.getResult();
              Validator.notNull(params, "params");
              return SetTitleRequest.from(params);
            }, Task.BACKGROUND_EXECUTOR).onSuccess(task -> {
              SetTitleRequest result = task.getResult();
              ifViewAttached(payView -> {
                payView.setTitle(result.getTitle());
              });
              return true;
            }, Task.UI_THREAD_EXECUTOR);
          default:
        }
        break;
      case HybridConstant.SERVICE_BIZ:
        switch (requestBody.getAction()) {
          case HybridConstant.ACTION_CALLBACK:
            return Task.forResult(requestBody.getParams()).continueWith(task -> {
              JSONObject params = task.getResult();
              Validator.notNull(params, "params");
              return ResponseBody.from(params);
            }, Task.BACKGROUND_EXECUTOR).onSuccessTask(task -> {
              ResponseBody result = task.getResult();
              return HybridController.instance().handlePayComplete(result);
            }, Task.UI_THREAD_EXECUTOR);
          default:
        }
        break;
      default:
    }
    return Task.forResult(false);
  }

  @Override
  public void destroy() {
    super.destroy();
    HybridController.instance().handleHybridClose();
  }
}
