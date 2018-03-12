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
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.farsunset.lvxin.listener.CloudImageApplyListener;
import com.farsunset.lvxin.listener.CloudImageLoadListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.network.CloudImageLoaderFactory;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.github.chrisbanes.photoview.OnPhotoTapListener;


public class ProgressbarPhotoView extends RelativeLayout implements OnPhotoTapListener, CloudImageApplyListener {
    private OnItemClickedListener onPhotoViewClickListener;
    private ProgressBar progressbar;
    private WebPhotoView photoView;
    private Bitmap mBitmap;

    public ProgressbarPhotoView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }


    @Override
    public void onImageApplyFailure(Object key, ImageView target) {
        removeView(progressbar);
    }

    @Override
    public void onImageApplySucceed(Object key, ImageView target, BitmapDrawable resource) {
        removeView(progressbar);
        mBitmap = resource.getBitmap();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setOnPhotoViewClickListener(
            OnItemClickedListener onPhotoViewClickListener) {
        this.onPhotoViewClickListener = onPhotoViewClickListener;
    }

    @Override
    public void onFinishInflate() {
        super.onDetachedFromWindow();
        super.onFinishInflate();
        photoView = (WebPhotoView) findViewById(R.id.image);
        progressbar = (ProgressBar) findViewById(R.id.progress);
        photoView.setOnPhotoTapListener(this);
    }


    public void display(final SNSImage image) {
        progressbar.setVisibility(View.VISIBLE);
        ViewCompat.setTransitionName(photoView, image.image);
        final String thumbnailUrl = FileURLBuilder.getFileUrl(image.thumbnail);
        final String imageUrl = FileURLBuilder.getFileUrl(image.image);
        CloudImageLoaderFactory.get().downloadOnly(thumbnailUrl, new CloudImageLoadListener() {
            @Override
            public void onImageLoadFailure(Object key) {
                onImageLoadSucceed(key, null);
            }

            @Override
            public void onImageLoadSucceed(Object key, Bitmap resource) {
                if (!photoView.isDetachedFromWindow()) {
                    CloudImageLoaderFactory.get().loadAndApply(photoView, imageUrl, resource, ProgressbarPhotoView.this);
                }
            }
        });

    }


    public void display(String imageUrl) {
        photoView.display(imageUrl, this);
    }


    @Override
    public void onPhotoTap(ImageView imageView, float v, float v1) {
        onPhotoViewClickListener.onItemClicked(null, photoView);
    }
}
