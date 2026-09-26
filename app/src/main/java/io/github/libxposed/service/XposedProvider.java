package io.github.libxposed.service;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class XposedProvider extends ContentProvider {

  private static final String TAG = "XposedProvider";

  @Override
  public boolean onCreate() {
    return false;
  }

  @Nullable
  @Override
  public Cursor query(
      @NonNull Uri uri,
      @Nullable String[] projection,
      @Nullable String selection,
      @Nullable String[] selectionArgs,
      @Nullable String sortOrder) {
    return null;
  }

  @Nullable
  @Override
  public String getType(@NonNull Uri uri) {
    return null;
  }

  @Nullable
  @Override
  public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
    return null;
  }

  @Override
  public int delete(
      @NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
    return 0;
  }

  @Override
  public int update(
      @NonNull Uri uri,
      @Nullable ContentValues values,
      @Nullable String selection,
      @Nullable String[] selectionArgs) {
    return 0;
  }

  @Nullable
  @Override
  public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
    // Только системные вызывающие: фреймворк работает максимум с shell-правами,
    // обычные приложения (uid >= 10000) сюда ходить не должны (аудит 24.09, M-04).
    if (Binder.getCallingUid() >= 10000) {
      Log.w(TAG, "call rejected for uid " + Binder.getCallingUid());
      return null;
    }
    if (method.equals(IXposedService.SEND_BINDER) && extras != null) {
      IBinder binder = extras.getBinder("binder");
      if (binder != null) {
        Log.d(TAG, "binder received: " + binder);
        XposedServiceHelper.onBinderReceived(binder);
      }
      return new Bundle();
    }
    return null;
  }
}
