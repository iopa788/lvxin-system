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
package com.farsunset.lvxin.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.farsunset.lvxin.activity.chat.MMWebViewActivity;
import com.farsunset.lvxin.activity.trend.MomentDetailedActivity;
import com.farsunset.lvxin.adapter.viewholder.SelfMomentViewHolder;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.component.SNSImageView;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.network.model.PublishObject;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.network.model.SNSVideo;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.farsunset.lvxin.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.List;

public class SelfMomentListViewAdapter extends MomentListViewAdapter {
    private final float density;
    private int cellWidth;
    private int spacing;

    public SelfMomentListViewAdapter(Context context) {
        super(context);
        spacing = context.getResources().getDimensionPixelOffset(R.dimen.sns_photos_spacing);
        density = Resources.getSystem().getDisplayMetrics().density;
        cellWidth = (int) ((Resources.getSystem().getDisplayMetrics().widthPixels - 110 * density) / 3);
    }

    @Override
    public RecyclerView.ViewHolder getMomentItemViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == ACTION_ITEM_PHOTOS) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_moment_multi_photo, parent, false);
        }
        if (viewType == ACTION_ITEM_LINK) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_moment_link, parent, false);
        }
        if (viewType == ACTION_ITEM_ONEPHOTO) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_moment_one_photo, parent, false);
        }
        if (viewType == ACTION_ITEM_TEXT) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_moment_text, parent, false);
        }
        if (viewType == ACTION_ITEM_VIDEO) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_moment_video, parent, false);
        }

        return new SelfMomentViewHolder(itemView, viewType);
    }


    @Override
    public void onBindMomentViewHolder(RecyclerView.ViewHolder holder, final Moment target) {
        final SelfMomentViewHolder viewHolder = (SelfMomentViewHolder) holder;
        final PublishObject publishObject = new Gson().fromJson(target.content, PublishObject.class);
        viewHolder.text.setVisibility(StringUtils.isNotEmpty(publishObject.content) ? View.VISIBLE : View.GONE);
        viewHolder.text.setText(publishObject.content);


        viewHolder.month.setText(LvxinApplication.getInstance().getString(R.string.label_user_moment_month, AppTools.getMonth(target.timestamp)));
        viewHolder.day.setText(AppTools.getDay(target.timestamp));


        if (viewHolder.viewType == ACTION_ITEM_PHOTOS) {
            List<SNSImage> snsImageList = new Gson().fromJson(target.thumbnail, new TypeToken<List<SNSImage>>() {}.getType());

            viewHolder.gridImageLayout.removeAllViews();

            for (int i = 0; i < snsImageList.size(); i++) {
                SNSImageView itemView = new SNSImageView(holder.itemView.getContext());
                itemView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                viewHolder.gridImageLayout.addView(itemView, cellWidth, cellWidth);
                itemView.display(target, snsImageList.get(i));

                boolean isRowFirst = i % viewHolder.gridImageLayout.getColumnCount() != 0;
                ((GridLayout.LayoutParams) viewHolder.gridImageLayout.getChildAt(i).getLayoutParams()).leftMargin = isRowFirst ? spacing : 0;

                boolean isFirstRow = i < viewHolder.gridImageLayout.getColumnCount();
                ((GridLayout.LayoutParams) viewHolder.gridImageLayout.getChildAt(i).getLayoutParams()).topMargin = isFirstRow ? 0 : spacing;
            }
        }

        if (viewHolder.viewType == ACTION_ITEM_LINK) {
            viewHolder.linkPanel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MMWebViewActivity.class);
                    intent.setData(Uri.parse(publishObject.link.link));
                    view.getContext().startActivity(intent);
                }
            });
            viewHolder.linkTitle.setText(publishObject.link.title);
        }

        if (viewHolder.viewType == ACTION_ITEM_ONEPHOTO) {
            List<SNSImage> snsImageList = new Gson().fromJson(target.thumbnail, new TypeToken<List<SNSImage>>() {
            }.getType());
            viewHolder.singleImageView.displayFitSize(target, snsImageList.get(0));
        }
        if (viewHolder.viewType == ACTION_ITEM_VIDEO) {

            if (viewHolder.thumbnailView.getHeight() == 0) {
                int width = (int) (Resources.getSystem().getDisplayMetrics().widthPixels - 100 * density);
                viewHolder.thumbnailView.getLayoutParams().height = width * 2 / 3;
                viewHolder.thumbnailView.getLayoutParams().width = width;
                viewHolder.thumbnailView.requestLayout();
            }

            SNSVideo video = new Gson().fromJson(target.content, PublishObject.class).video;
            File thumbnailFile = new File(LvxinApplication.CACHE_DIR_VIDEO, video.thumbnail);
            if (thumbnailFile.exists()) {
                viewHolder.thumbnailView.load(thumbnailFile, R.color.video_background);
            } else {
                String url = FileURLBuilder.getFileUrl(video.thumbnail);
                viewHolder.thumbnailView.load(url, R.color.video_background);
            }

        }
        viewHolder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MomentDetailedActivity.class);
                intent.putExtra(Moment.class.getName(), target);
                view.getContext().startActivity(intent);
            }
        });
    }
}
