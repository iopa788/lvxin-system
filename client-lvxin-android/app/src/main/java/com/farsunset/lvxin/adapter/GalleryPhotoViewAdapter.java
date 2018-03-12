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

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farsunset.lvxin.component.ProgressbarPhotoView;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.pro.R;

import java.util.List;

public class GalleryPhotoViewAdapter extends PagerAdapter {

    private List<SNSImage> list;
    private OnItemClickedListener onItemClickedListener;

    public GalleryPhotoViewAdapter(List<SNSImage> list) {
        this.list = list;
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {


        ProgressbarPhotoView photoView = (ProgressbarPhotoView) LayoutInflater.from(container.getContext()).inflate(R.layout.layout_progressbar_photoview, null);
        photoView.setTag(position);
        photoView.setOnPhotoViewClickListener(onItemClickedListener);
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        photoView.display(list.get(position));
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void addAll(List<SNSImage> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}
