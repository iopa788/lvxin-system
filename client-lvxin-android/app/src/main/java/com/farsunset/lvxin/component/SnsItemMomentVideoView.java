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
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.listener.OnCommentSelectedListener;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.network.model.PublishObject;
import com.farsunset.lvxin.network.model.SNSVideo;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.google.gson.Gson;

import java.io.File;

public class SnsItemMomentVideoView extends SnsItemMomentView {
    private WebImageView thumbnailView;
    private SNSVideo video;

    public SnsItemMomentVideoView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        thumbnailView = (WebImageView) findViewById(R.id.thumbnailView);
        ((ViewGroup) thumbnailView.getParent()).setOnClickListener(this);
    }

    @Override
    public void displayMoment(Moment moment, User self, OnCommentSelectedListener commentSelectedListener) {
        super.displayMoment(moment, self, commentSelectedListener);

        video = new Gson().fromJson(moment.content, PublishObject.class).video;
        if (video.mode == SNSVideo.HORIZONTAL) {
            thumbnailView.getLayoutParams().width = getContext().getResources().getDimensionPixelOffset(R.dimen.sns_video_height);
            thumbnailView.getLayoutParams().height = getContext().getResources().getDimensionPixelOffset(R.dimen.sns_video_width);
        } else {
            thumbnailView.getLayoutParams().width = getContext().getResources().getDimensionPixelOffset(R.dimen.sns_video_width);
            thumbnailView.getLayoutParams().height = getContext().getResources().getDimensionPixelOffset(R.dimen.sns_video_height);
        }

        File thumbnailFile = new File(LvxinApplication.CACHE_DIR_VIDEO, video.thumbnail);
        if (thumbnailFile.exists()) {
            thumbnailView.load(thumbnailFile, R.color.video_background);
        } else {
            String url = FileURLBuilder.getFileUrl(video.thumbnail);
            thumbnailView.load(url, R.color.video_background);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == thumbnailView.getParent()) {
            LvxinApplication.getInstance().startVideoActivity(getContext(), false, video, (View) thumbnailView.getParent());
            return;
        }
        super.onClick(view);
    }
}
