package com.tec.pay.android.hybrid.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.tec.pay.android.hybrid.model.GetResponse;
import com.tec.pay.android.task.Task;

/**
 * IHybridDataSource class.
 *
 * @author Lucas Cheung.
 * @date 2019-12-27.
 */
public interface IHybridDataSource {

  Task<Void> setCache(@NonNull String key, @Nullable String value);

  Task<Void> setCache(@NonNull String key, @Nullable String value, long expiredMillis);

  Task<GetResponse> getCache(@NonNull String key, @Nullable String defaultValue);

  Task<Void> delCache(@NonNull final String key);

  Task<Void> flushCache();
}
