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
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.MediaController;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.component.CircleProgressView;
import com.farsunset.lvxin.component.TextureVideoView;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.listener.CloudImageApplyListener;
import com.farsunset.lvxin.listener.OSSFileDownloadListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.network.CloudFileDownloader;
import com.farsunset.lvxin.network.model.SNSVideo;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class VideoPlayerActivity extends BaseActivity implements OnItemClickedListener, OSSFileDownloadListener, MediaPlayer.OnPreparedListener, CloudImageApplyListener {
    private TextureVideoView videoView;
    private CircleProgressView progressView;
    private WebImageView thumbnailView;
    private SNSVideo video;
    private String url;
    private File videoFile;

    @Override
    public boolean getSwipeBackEnable(){
        return false;
    }
    @Override
    public void initComponents() {

        super.setStatusBarColor(Color.TRANSPARENT);
        super.setWindowFullscreen();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
        setBackIcon(R.drawable.abc_ic_clear_material);
        getWindow().setBackgroundDrawableResource(android.R.color.black);
        videoView = (TextureVideoView) findViewById(R.id.videoView);
        thumbnailView = (WebImageView) findViewById(R.id.thumbnailView);
        progressView = (CircleProgressView) findViewById(R.id.progressView);
        videoView.setMediaController(new MediaController(this));
        videoView.setOnPreparedListener(this);
        this.video = (SNSVideo) getIntent().getSerializableExtra(SNSVideo.class.getName());

        initVideo();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_video_player;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }


    private void initVideo() {
        videoFile = new File(LvxinApplication.CACHE_DIR_VIDEO, video.video);
        File thumbnailFile = new File(LvxinApplication.CACHE_DIR_VIDEO, video.thumbnail);

        if (thumbnailFile.exists()) {
            thumbnailView.load(Uri.fromFile(thumbnailFile).toString(), this);
        } else {
            String url = FileURLBuilder.getFileUrl(video.thumbnail);
            thumbnailView.load(url, this);
        }
    }

    private void downloadVideoFile(String url, File file) {
        this.url = url;
        progressView.setVisibility(View.VISIBLE);
        CloudFileDownloader.asyncDownload(url, file, this);
    }

    public void startPlayVideo() {
        videoView.setVideoURI(Uri.fromFile(videoFile));
        progressView.setVisibility(View.GONE);
        videoView.start();
    }


    @Override
    public void onItemClicked(Object obj, View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        videoView.stopPlayback();
        videoView.setVisibility(View.GONE);
        CloudFileDownloader.stop(url);
        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean buttonVisable = getIntent().getBooleanExtra("buttonVisable", false);
        if (buttonVisable) {
            getMenuInflater().inflate(R.menu.send, menu);
        } else {
            getMenuInflater().inflate(R.menu.download, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_download) {
            downloadAndNotify();
        }
        if (item.getItemId() == R.id.menu_send) {
            setResult(RESULT_OK);
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadAndNotify() {
        try {
            File desFile = new File(Constant.SYSTEM_PHOTO_DIR, videoFile.getName());
            FileUtils.copyFile(videoFile, desFile);
            showToastView(getString(R.string.tip_photo_video_download_complete));

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(desFile);
            intent.setData(uri);
            LvxinApplication.sendGlobalBroadcast(intent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDownloadCompleted(File file, String currentKey) {
        startPlayVideo();
    }

    @Override
    public void onDownloadFailured(File file, String currentKey) {
        progressView.loadError();
    }

    @Override
    public void onDownloadProgress(String key, float progress) {
        progressView.setVisibility(View.VISIBLE);
        progressView.setProgress((int) progress);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        thumbnailView.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleThumbVideoView();
            }
        }, 300);
    }

    private void toggleThumbVideoView() {
        videoView.setAlpha(1f);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.disappear);
        animation.setFillAfter(true);
        animation.setDuration(0);
        thumbnailView.startAnimation(animation);
    }

    @Override
    public void onImageApplyFailure(Object key, ImageView target) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startPostponedEnterTransition();
        }
    }

    @Override
    public void onImageApplySucceed(Object key, ImageView target, BitmapDrawable resource) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startPostponedEnterTransition();
        }

        if (videoFile.exists() && videoFile.length() == video.size) {
            startPlayVideo();
            return;
        }
        if (videoFile.exists() && videoFile.length() > video.size) {
            videoFile.delete();
        }
        downloadVideoFile(FileURLBuilder.getFileUrl(video.video), videoFile);

    }
}