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
import android.net.Uri;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.listener.CloudImageApplyListener;
import com.farsunset.lvxin.listener.OnTransmitProgressListener;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.network.model.SNSVideo;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class ChatVideoView extends CardView implements OnClickListener, OnTransmitProgressListener, CloudImageApplyListener, Palette.PaletteAsyncListener {
    protected TextView extraTextView;
    protected WebImageView image;
    private Message message;
    private CircleProgressView uploadProgressView;

    public ChatVideoView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        image = (WebImageView) this.findViewById(R.id.image);
        extraTextView = (TextView) this.findViewById(R.id.extra);
    }

    public void setSendProgressView(CircleProgressView circleProgressView) {
        this.uploadProgressView = circleProgressView;
    }

    public void initViews(SNSVideo video, Message msg) {
        this.message = msg;
        if (video.mode == SNSVideo.HORIZONTAL) {
            image.getLayoutParams().width = getContext().getResources().getDimensionPixelOffset(R.dimen.chat_video_height);
            image.getLayoutParams().height = getContext().getResources().getDimensionPixelOffset(R.dimen.chat_video_width);
        } else {
            image.getLayoutParams().width = getContext().getResources().getDimensionPixelOffset(R.dimen.chat_video_width);
            image.getLayoutParams().height = getContext().getResources().getDimensionPixelOffset(R.dimen.chat_video_height);
        }
        image.requestLayout();
        File thumbnailFile = new File(LvxinApplication.CACHE_DIR_VIDEO, video.thumbnail);
        if (thumbnailFile.exists()) {
            image.load(Uri.fromFile(thumbnailFile).toString(), R.drawable.def_chat_video_background, 0, this);
        } else {
            String url = FileURLBuilder.getFileUrl(video.thumbnail);
            image.load(url, R.drawable.def_chat_video_background, 0, this);
        }

        if (getParent() instanceof BaseFromMessageView) {
            extraTextView.setText(video.duration + "\" " + FileUtils.byteCountToDisplaySize(video.size));
        }
    }

    @Override
    public void onClick(View view) {
        SNSVideo video = new Gson().fromJson(message.content, SNSVideo.class);

        LvxinApplication.getInstance().startVideoActivity(getContext(), false, video, this);

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

    @Override
    public void onImageApplyFailure(Object key, ImageView target) {

    }

    @Override
    public void onImageApplySucceed(Object key, ImageView target, BitmapDrawable resource) {
        if (getParent() instanceof BaseFromMessageView) {
            Palette.Builder builder = new Palette.Builder(resource.getBitmap());
            builder.generate(this);
        }
    }

    @Override
    public void onGenerated(Palette palette) {
        Palette.Swatch vibrant = palette.getLightVibrantSwatch();
        if (vibrant != null) {
            extraTextView.setTextColor(vibrant.getTitleTextColor());
        }
    }
}
