/*
 * Copyright (C) 2015 Drakeet <drakeet.me@gmail.com>
 *
 * This file is part of Meizhi
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */

package cn.app.library.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.app.library.R;
import cn.app.library.widget.toast.ToastUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * @describe:保存网络图片到本地
 */
public class RxImage {

    public static void saveImageAndGetPathObservable(final Context context, final String url) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> subscribe) throws Exception {
                Bitmap bitmap = null;
                try {
                    bitmap = Picasso.with(context).load(url).get();
                } catch (IOException e) {
                    subscribe.onError(e);
                }
                if (bitmap == null) {
                    subscribe.onError(new Exception("无法下载到图片"));
                }
                subscribe.onNext(bitmap);
                subscribe.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Bitmap bitmap) {
                        File appDir = new File(Environment.getExternalStorageDirectory(), context.getPackageName() + "App");
                        if (!appDir.exists()) {
                            appDir.mkdir();
                        }
                        String fileName = DateUtils.getNowTimeMills() + ".jpg";
                        File file = new File(appDir, fileName);
                        try {
                            FileOutputStream outputStream = new FileOutputStream(file);
                            assert bitmap != null;
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            outputStream.flush();
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Uri uri = Uri.fromFile(file);
                        // 通知图库更新
                        Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                        context.sendBroadcast(scannerIntent);
                        String msg = String.format(context.getString(R.string.picture_has_save_to),
                                appDir.getAbsolutePath());
                        ToastUtil.show(msg);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        ToastUtil.show("图片保存失败，请重试");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
