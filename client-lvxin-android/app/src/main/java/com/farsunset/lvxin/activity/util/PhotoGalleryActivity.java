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
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.adapter.GalleryPhotoViewAdapter;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.component.GalleryViewPager;
import com.farsunset.lvxin.component.ProgressbarPhotoView;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.BitmapUtils;

import java.io.File;
import java.util.List;


public class PhotoGalleryActivity extends BaseActivity implements ViewPager.OnPageChangeListener, OnItemClickedListener {
    private GalleryViewPager viewPager;
    private LinearLayout tagPanel;
    private int selectedIndex = 0;

    @Override
    public boolean getSwipeBackEnable(){
        return false;
    }
    @Override
    public void initComponents() {

        super.setStatusBarColor(Color.TRANSPARENT);
        super.setWindowFullscreen();
        setBackIcon(R.drawable.abc_ic_clear_material);
        viewPager = (GalleryViewPager) findViewById(R.id.viewPager);
        tagPanel = (LinearLayout) findViewById(R.id.ViewPagerTagPanel);
        List<SNSImage> ossImages = (List<SNSImage>) getIntent().getSerializableExtra(SNSImage.class.getName());
        getWindow().setBackgroundDrawableResource(android.R.color.black);
        buildPagerIndicator(ossImages);
        GalleryPhotoViewAdapter adapter = new GalleryPhotoViewAdapter(ossImages);
        adapter.setOnItemClickedListener(this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(2);
        tagPanel.getChildAt(0).setSelected(true);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    public void buildPagerIndicator(List<SNSImage> ossImages) {
        selectedIndex = 0;
        for (int i = 0; i < ossImages.size(); i++) {
            ImageView tag = new ImageView(this);
            tag.setImageResource(R.drawable.icon_pager_tag);
            tag.setPadding(12, 0, 0, 0);
            tagPanel.addView(tag);
        }
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {
    }


    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }


    @Override
    public void onPageSelected(int index) {
        tagPanel.getChildAt(selectedIndex).setSelected(false);
        tagPanel.getChildAt(index).setSelected(true);
        selectedIndex = index;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_gallery_photoview;
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
    public void onItemClicked(Object obj, View view) {
        onBackPressed();
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
        ProgressbarPhotoView photoView = (ProgressbarPhotoView) viewPager.findViewWithTag(viewPager.getCurrentItem());
        if (photoView.getBitmap() == null) {
            return;
        }
        File desFile = new File(Constant.SYSTEM_PHOTO_DIR, System.currentTimeMillis() + ".jpg");
        BitmapUtils.savePhotoBitmap2File(photoView.getBitmap(), desFile);
        showToastView(getString(R.string.tip_photo_video_download_complete));

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(desFile));
        LvxinApplication.sendGlobalBroadcast(intent);
    }
}
