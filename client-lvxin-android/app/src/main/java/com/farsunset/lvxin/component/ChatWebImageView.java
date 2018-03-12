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
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.listener.CloudImageApplyListener;
import com.farsunset.lvxin.listener.OnTransmitProgressListener;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.google.gson.Gson;

public class ChatWebImageView extends CardView implements OnClickListener, OnTransmitProgressListener, CloudImageApplyListener {

    protected WebImageView image;
    private String key;
    private Message message;
    private View progressbar;
    private CircleProgressView uploadProgressView;


    public ChatWebImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        image = (WebImageView) this.findViewById(R.id.image);
        progressbar = findViewById(R.id.loadImagePprogressbar);
    }

    public void setUploadProgressView(CircleProgressView view) {
        uploadProgressView = view;
    }

    public void initViews(SNSImage snsImage, Message msg) {



        this.message = msg;
        key = snsImage.thumbnail;
        String url = FileURLBuilder.getFileUrl(key);
        progressbar.setVisibility(View.VISIBLE);

        int minSide = getResources().getDimensionPixelOffset(R.dimen.chat_thumbnail_min_side);
        if (snsImage.tw > snsImage.th) {
            this.image.setLayoutParams(new FrameLayout.LayoutParams(minSide * snsImage.tw / snsImage.th, minSide));
        } else {
            this.image.setLayoutParams(new FrameLayout.LayoutParams(minSide, minSide * snsImage.th / snsImage.tw));
        }

        image.load(url, 0, this);
    }

    @Override
    public void onClick(View view) {
        SNSImage image = new Gson().fromJson(message.content, SNSImage.class);
        image.thumbnail = key;
        LvxinApplication.getInstance().startPhotoActivity(getContext(), image, this);
    }

    @Override
    public void onImageApplyFailure(Object key, ImageView target) {
        onImageApplySucceed(key, target, null);
    }

    @Override
    public void onImageApplySucceed(Object key, ImageView target, BitmapDrawable resource) {
        ChatWebImageView chatWebImageView = (ChatWebImageView) target.getParent();
        chatWebImageView.progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onProgress(float progress) {
        uploadProgressView.setProgress((int) progress);
        if (progress >= 100) {
            uploadProgressView.setVisibility(GONE);
            return;
        }
        if (progress > 0) {
            uploadProgressView.setVisibility(VISIBLE);
        }
    }


}
