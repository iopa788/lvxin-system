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
package com.farsunset.lvxin.activity.chat;


import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.listener.OSSFileDownloadListener;
import com.farsunset.lvxin.network.CloudFileDownloader;
import com.farsunset.lvxin.network.model.ChatFile;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.farsunset.lvxin.util.FileUtils;
import com.google.gson.Gson;

import java.io.File;
import java.text.DecimalFormat;

public class FileViewerActivity extends BaseActivity implements OSSFileDownloadListener {


    private ChatFile chatFile;
    private File file;
    private DecimalFormat format = new DecimalFormat("0.00");
    private TextView downLoadButton;
    private TextView progressSizeText;
    private TextView progressTxt;
    private ProgressBar fileProgressBar;
    private String threadKey;

    @Override
    public void initComponents() {
        String content = MessageRepository.queryById(this.getIntent().getStringExtra("mid")).content;
        chatFile =  new Gson().fromJson(content, ChatFile.class);

        findViewById(R.id.openFileButton).setOnClickListener(this);
        (downLoadButton = (TextView) findViewById(R.id.downLoadButton)).setOnClickListener(this);
        findViewById(R.id.stopDownLoadButton).setOnClickListener(this);
        progressSizeText = ((TextView) findViewById(R.id.progressSizeText));
        progressTxt = ((TextView) findViewById(R.id.progressTxt));
        fileProgressBar = ((ProgressBar) findViewById(R.id.fileProgressBar));

        ((TextView) findViewById(R.id.name)).setText(chatFile.name);
        ((TextView) findViewById(R.id.size)).setText(org.apache.commons.io.FileUtils.byteCountToDisplaySize(chatFile.size));
        ((ImageView) findViewById(R.id.icon)).setImageResource(FileUtils.getFileIcon(chatFile.name));

        if (chatFile.getLocalFile() != null && chatFile.getLocalFile().exists()) {
            file = chatFile.getLocalFile();
            findViewById(R.id.openFileButton).setVisibility(View.VISIBLE);
            return;
        }
        file = new File(LvxinApplication.DOWNLOAD_DIR, chatFile.name);
        if (file.exists() && file.length() == chatFile.size) {
            findViewById(R.id.openFileButton).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.path)).setText(getString(R.string.label_file_path_format,file.getName()));
        } else {
            findViewById(R.id.downLoadButton).setVisibility(View.VISIBLE);
        }
        if (file.length() > 0 && file.length() < chatFile.size) {
            findViewById(R.id.downloadPanel).setVisibility(View.VISIBLE);
            downLoadButton.setText(R.string.common_resume_download);
            progressSizeText.setText(getString(R.string.label_file_download, org.apache.commons.io.FileUtils.byteCountToDisplaySize(file.length())) + "/" + org.apache.commons.io.FileUtils.byteCountToDisplaySize(this.chatFile.size));
            progressTxt.setText(format.format((double) file.length() / chatFile.size * 100) + "%");
            fileProgressBar.setProgress((int) (file.length() * 100 / this.chatFile.size));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openFileButton:
                this.startActivity(FileUtils.getFileIntent(file.getAbsolutePath()));
                break;
            case R.id.downLoadButton:
                findViewById(R.id.downLoadButton).setVisibility(View.GONE);
                findViewById(R.id.downloadPanel).setVisibility(View.VISIBLE);
                findViewById(R.id.stopDownLoadButton).setVisibility(View.VISIBLE);
                file = AppTools.creatSDFile(LvxinApplication.DOWNLOAD_DIR + "/" + chatFile.name);
                String url = FileURLBuilder.getFileUrl(chatFile.key);
                threadKey = url;
                CloudFileDownloader.asyncDownload(file, url, this);
                break;

            case R.id.stopDownLoadButton:

                CloudFileDownloader.stop(threadKey);
                findViewById(R.id.stopDownLoadButton).setVisibility(View.GONE);
                findViewById(R.id.openFileButton).setVisibility(View.GONE);
                downLoadButton.setText(R.string.common_resume_download);
                downLoadButton.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    public void onDestroy() {
        CloudFileDownloader.stop(threadKey);
        super.onDestroy();
    }

    @Override
    public int getContentLayout() {

        return R.layout.activity_file_viewer;
    }

    @Override
    public int getToolbarTitle() {

        return R.string.label_file_info;
    }


    @Override
    public void onDownloadCompleted(final File file, String currentKey) {
        showToastView(getString(R.string.tip_file_download_complete, file.getAbsolutePath()));
        findViewById(R.id.stopDownLoadButton).setVisibility(View.GONE);
        findViewById(R.id.openFileButton).setVisibility(View.VISIBLE);
        downLoadButton.setVisibility(View.GONE);
        findViewById(R.id.downloadPanel).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.path)).setText(getString(R.string.label_file_path_format,file.getName()));
    }

    @Override
    public void onDownloadFailured(File file, String currentKey) {

        findViewById(R.id.stopDownLoadButton).setVisibility(View.GONE);
        downLoadButton.setText(R.string.common_resume_download);
        downLoadButton.setVisibility(View.VISIBLE);
        FileViewerActivity.this.showToastView(getString(R.string.tip_file_download_failed));

    }

    @Override
    public void onDownloadProgress(String key, float progress) {
        progressSizeText.setText(getString(R.string.label_file_download, org.apache.commons.io.FileUtils.byteCountToDisplaySize((long) (chatFile.size * progress) / 100)) + "/" + org.apache.commons.io.FileUtils.byteCountToDisplaySize(chatFile.size));
        progressTxt.setText(format.format(progress) + "%");
        fileProgressBar.setProgress((int) progress);
    }

}
