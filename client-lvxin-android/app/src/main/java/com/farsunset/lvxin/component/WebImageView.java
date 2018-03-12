/**
 * Copyright 2013-2023 Xia Jun(3979434@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * **************************************************************************************
 * *
 * Website : http://www.farsunset.com                           *
 * *
 * **************************************************************************************
 */
package com.farsunset.lvxin.component;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.listener.CloudImageApplyListener;
import com.farsunset.lvxin.network.CloudImageLoaderFactory;
import com.farsunset.lvxin.util.FileURLBuilder;

import java.io.File;

public class WebImageView extends AppCompatImageView implements View.OnClickListener {

    private String originalUrl;

    public WebImageView(Context context) {
        super(context);
    }

    public WebImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }


    /**
     * @return download url
     */
    public String getUrl() {
        return originalUrl;
    }

    public void setUrl(String url) {
        originalUrl = url;
    }

    /**
     * 设置download url，开始下载
     *
     * @param url
     */
    public void load(String url, int defBackgroundId, float rounded, CloudImageApplyListener listener) {

        this.originalUrl = url;
        CloudImageLoaderFactory.get().loadAndApply(this, url, defBackgroundId, rounded, listener);
    }

    public void load(String url, @DrawableRes int defBackgroundId, int rounded) {


        this.originalUrl = url;
        load(url, defBackgroundId, rounded, null);
    }

    public void load(String url, int defBackgroundId) {

        this.originalUrl = url;
        load(url, defBackgroundId, 0, null);
    }

    public void load(String url, float rounded, CloudImageApplyListener listener) {


        this.originalUrl = url;

        load(url, 0, rounded, listener);
    }


    public void load(String url) {
        load(url, 0, 0);
    }

    public void load(Uri uri) {
        load(uri.toString(), 0, 0);
    }

    public void load(Uri uri, int defBackgroundId) {
        load(uri.toString(), defBackgroundId, 0);
    }

    public void load(File file) {
        originalUrl = Uri.fromFile(file).toString();
        load(originalUrl, 0, 0);
    }

    public void load(File file, CloudImageApplyListener listener) {
        originalUrl = Uri.fromFile(file).toString();
        load(originalUrl, 0, 0, listener);
    }

    public void load(String url, CloudImageApplyListener listener) {
        originalUrl = url;
        load(originalUrl, 0, 0, listener);
    }

    public void load(File file, int defBackgroundId) {
        originalUrl = Uri.fromFile(file).toString();
        load(originalUrl, defBackgroundId, 0);
    }

    public void display(String thumb, String originalUrl) {
        load(thumb, 0, 0);
        this.originalUrl = originalUrl;
    }


    /**
     * 设置download url，开始下载
     */
    public void loadLocale(String key, int defBackgroundId) {

        originalUrl = FileURLBuilder.getFileUrl(key);
        load(originalUrl, defBackgroundId, 0);
    }

    public void setPopuShowAble() {

        setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        LvxinApplication.getInstance().startPhotoActivity(getContext(), originalUrl, this);
    }
}
