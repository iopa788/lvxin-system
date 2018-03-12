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
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.farsunset.lvxin.activity.chat.MapViewActivity;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.listener.CloudImageApplyListener;
import com.farsunset.lvxin.listener.OnTransmitProgressListener;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.network.model.ChatMap;
import com.farsunset.lvxin.network.model.MapAddress;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.google.gson.Gson;

public class ChatMapView extends CardView implements OnClickListener, OnTransmitProgressListener, CloudImageApplyListener {
    private WebImageView image;
    private TextView text;
    private View progressbar;
    private ProgressBar uploadProgressBar;
    Handler progressHandler = new Handler() {

        @Override
        public void handleMessage(android.os.Message message) {
            Animation animation = AnimationUtils.loadAnimation(LvxinApplication.getInstance(), R.anim.disappear);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    uploadProgressBar.setVisibility(GONE);
                }

            });
            uploadProgressBar.startAnimation(animation);
        }
    };
    private ChatMap chatMap;


    public ChatMapView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        text = (TextView) this.findViewById(R.id.text);
        image = (WebImageView) this.findViewById(R.id.image);


        progressbar = findViewById(R.id.loadImagePprogressbar);
        uploadProgressBar = (ProgressBar) this.findViewById(R.id.uploadProgressBar);
    }

    /**
     * 设置download url，开始下载
     */
    public void initViews(Message message) {

        chatMap = new Gson().fromJson(message.content, ChatMap.class);

        setTag(chatMap.key);

        text.setText(chatMap.address);
        String url = FileURLBuilder.getFileUrl(chatMap.key);
        progressbar.setVisibility(View.VISIBLE);
        image.load(url, R.drawable.def_chat_progress_background, 0, this);
    }

    @Override
    public void onClick(View view) {
        MapAddress mapAddress = new MapAddress();
        mapAddress.name = chatMap.address;
        mapAddress.latitude = chatMap.latitude;
        mapAddress.longitude = chatMap.longitude;
        Intent intentLoc = new Intent(getContext(), MapViewActivity.class);
        intentLoc.putExtra(MapAddress.class.getName(), mapAddress);
        getContext().startActivity(intentLoc);
    }

    @Override
    public void onProgress(float progress) {

        uploadProgressBar.setProgress((int) progress);
        if (progress >= 100) {
            progressHandler.removeMessages(0);
            progressHandler.sendEmptyMessageDelayed(0, 100);
        }
    }

    @Override
    public void onImageApplyFailure(Object key, ImageView target) {
        ChatMapView chatMapView = (ChatMapView) target.getParent();
        chatMapView.progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onImageApplySucceed(Object key, ImageView target, BitmapDrawable resource) {
        ChatMapView chatMapView = (ChatMapView) target.getParent();
        chatMapView.progressbar.setVisibility(View.GONE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        progressHandler.removeCallbacksAndMessages(null);
    }


}
