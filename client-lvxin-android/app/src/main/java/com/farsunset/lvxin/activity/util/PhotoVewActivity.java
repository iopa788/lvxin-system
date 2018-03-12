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
package com.farsunset.lvxin.activity.util;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.component.WebPhotoView;
import com.farsunset.lvxin.listener.CloudImageApplyListener;
import com.farsunset.lvxin.listener.CloudImageLoadListener;
import com.farsunset.lvxin.network.CloudImageLoaderFactory;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.BitmapUtils;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.github.chrisbanes.photoview.OnPhotoTapListener;

import java.io.File;


public class PhotoVewActivity extends BaseActivity implements OnPhotoTapListener, CloudImageLoadListener, CloudImageApplyListener {
    private SNSImage snsImage;
    private ProgressBar progressbar;
    private WebPhotoView photoView;
    private Bitmap mBitmap;

    @Override
    public boolean getSwipeBackEnable(){
        return false;
    }
    @Override
    public void initComponents() {
        super.setStatusBarColor(Color.TRANSPARENT);
        setWindowFullscreen();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
        setBackIcon(R.drawable.abc_ic_clear_material);
        getWindow().setBackgroundDrawableResource(android.R.color.black);
        photoView = (WebPhotoView) findViewById(R.id.photoView);
        progressbar = (ProgressBar) findViewById(R.id.progress);
        photoView.setOnPhotoTapListener(this);
        snsImage = (SNSImage) getIntent().getSerializableExtra(SNSImage.class.getName());
        if (snsImage == null) {
            display(getIntent().getStringExtra("url"));
        } else {
            display(snsImage);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void display(final SNSImage image) {
        final String thumbnailUrl = FileURLBuilder.getFileUrl(image.thumbnail);
        this.display(thumbnailUrl);
    }

    public void display(String imageUrl) {
        photoView.display(imageUrl, this);
    }

    @Override
    public void onPhotoTap(ImageView imageView, float v, float v1) {
        onBackPressed();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_single_photoview;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }


    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    public void onImageLoadFailure(Object key) {
        progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onImageLoadSucceed(Object key, final Bitmap resource) {

        if (isDestroyed()) {
            return;
        }
        mBitmap = resource;
        photoView.setImageBitmap(resource);
        progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onImageApplyFailure(Object key, ImageView target) {
        progressbar.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startPostponedEnterTransition();
        }
    }


    @Override
    public void onImageApplySucceed(Object key, ImageView target, BitmapDrawable resource) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startPostponedEnterTransition();
        }
        if (isDestroyed() || snsImage == null) {
            progressbar.setVisibility(View.GONE);
            return;
        }
        final String imageUrl = FileURLBuilder.getFileUrl(snsImage.image);
        photoView.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressbar.setVisibility(View.VISIBLE);
                CloudImageLoaderFactory.get().downloadOnly(imageUrl, PhotoVewActivity.this);
            }
        }, 500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_download) {
            downloadAndNotify();
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadAndNotify() {
        if (mBitmap == null) {
            return;
        }
        File desFile = new File(Constant.SYSTEM_PHOTO_DIR, snsImage.image + ".jpg");
        BitmapUtils.savePhotoBitmap2File(mBitmap, desFile);
        showToastView(getString(R.string.tip_photo_video_download_complete));

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(desFile);
        intent.setData(uri);
        LvxinApplication.sendGlobalBroadcast(intent);
    }

}
