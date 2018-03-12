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
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.listener.CloudImageLoadListener;
import com.farsunset.lvxin.network.CloudImageLoaderFactory;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.BitmapUtils;
import com.google.gson.Gson;

public class ToMessageImageView extends BaseToMessageView {
    private CircleProgressView circleProgressView;

    public ToMessageImageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        circleProgressView = (CircleProgressView) findViewById(R.id.circleProgressView);
    }

    @Override
    public View getContentView() {
        return imageView;
    }

    @Override
    public void displayMessage() {
        if (Constant.MessageStatus.STATUS_NO_SEND.equals(message.status)) {
            setVisibility(GONE);
            SNSImage chatImage = new Gson().fromJson(message.content, SNSImage.class);
            message.status = Constant.MessageStatus.STATUS_DELAY_SEND;
            loadNativeImageFile(Uri.parse(chatImage.image));

        } else {
            SNSImage chatImage = new Gson().fromJson(message.content, SNSImage.class);
            initImageView(chatImage);
        }

    }

    private void initImageView(SNSImage chatImage) {
        setVisibility(VISIBLE);
        circleProgressView.setVisibility(GONE);
        imageView.setTag(chatImage.image);
        imageView.initViews(chatImage, message);
        imageView.setOnClickListener(imageView);
        sendProgressbar.setVisibility(View.GONE);
        imageView.setUploadProgressView(circleProgressView);
    }


    private void loadNativeImageFile(final Uri file) {
        CloudImageLoaderFactory.get().downloadOnly(file, new CloudImageLoadListener() {
            @Override
            public void onImageLoadFailure(Object key) {
            }

            @Override
            public void onImageLoadSucceed(Object key, Bitmap resource) {
                message.status = Constant.MessageStatus.STATUS_NO_SEND;
                SNSImage snsImage = BitmapUtils.compressSNSImage(resource);
                message.content = new Gson().toJson(snsImage);
                MessageRepository.deleteById(message.mid);
                MessageRepository.add(message);

                initImageView(snsImage);

                statusHandler();
            }
        });
    }

}
