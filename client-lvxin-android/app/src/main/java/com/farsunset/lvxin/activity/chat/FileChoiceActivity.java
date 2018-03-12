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


import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.adapter.FileChooseViewAdapter;
import com.farsunset.lvxin.comparator.FileAndDirComparator;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.network.model.ChatFile;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileUtils;
import com.farsunset.lvxin.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FileChoiceActivity extends BaseActivity implements OnDialogButtonClickListener, OnItemClickedListener {

    private FileChooseViewAdapter adapter;
    private ArrayList<File> fileList = new ArrayList<File>();
    private File currentFile;
    private TextView currentDirPath;
    private CustomDialog customDialog;
    private ArrayMap<String, Integer> indexMap = new ArrayMap<>();
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void initComponents() {

        RecyclerView fileListView = (RecyclerView) findViewById(R.id.fileListView);
        linearLayoutManager = new LinearLayoutManager(this);
        fileListView.setLayoutManager(linearLayoutManager);

        findViewById(R.id.parentDirButton).setOnClickListener(this);
        findViewById(R.id.sendFileButton).setOnClickListener(this);
        currentDirPath = (TextView) findViewById(R.id.currentDirPath);
        currentDirPath.setText(Environment.getExternalStorageDirectory().getAbsolutePath());

        updateToFileList(Environment.getExternalStorageDirectory().listFiles());
        adapter = new FileChooseViewAdapter();
        fileListView.setAdapter(adapter);
        adapter.setOnItemClickedListener(this);
        adapter.addAll(fileList);

        customDialog = new CustomDialog(this);
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setTitle(R.string.common_hint);
        customDialog.setMessage((R.string.tip_file_too_large));
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.parentDirButton) {
            String parentPath = new File(currentDirPath.getText().toString()).getParent();
            if (parentPath != null) {
                changeFolder(parentPath);
            }
            Integer currentDirIndex = indexMap.get(parentPath);
            if (currentDirIndex != null && currentDirIndex >= 0) {
                linearLayoutManager.scrollToPositionWithOffset(currentDirIndex, 0);
            }
        }

        if (v.getId() == R.id.sendFileButton) {
            if (currentFile.length() >= 20 * org.apache.commons.io.FileUtils.ONE_MB) {
                customDialog.show();
            } else {
                finishAndSendFile();
            }
        }

    }

    private void finishAndSendFile() {
        Intent intent = new Intent();
        ChatFile file = new ChatFile();
        file.size = currentFile.length();
        file.name = currentFile.getName();
        file.path = currentFile.getAbsolutePath();
        file.key = StringUtils.getUUID();
        intent.putExtra(ChatFile.class.getName(), file);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }


    public void changeFolder(String path) {
        updateToFileList(new File(path).listFiles());
        adapter.addAll(fileList);
        currentDirPath.setText(path);
    }


    public void updateToFileList(File[] files) {
        if (files == null) {
            return;
        }
        fileList.clear();
        fileList.addAll(Arrays.asList(files));
        Collections.sort(fileList, new FileAndDirComparator());
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_filechoice;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_file_choice;
    }


    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        finishAndSendFile();
    }


    @Override
    public void onItemClicked(Object obj, View view) {
        currentFile = (File) obj;
        if (currentFile.isDirectory()) {
            indexMap.put(currentFile.getParentFile().getAbsolutePath(), linearLayoutManager.findFirstVisibleItemPosition());
            changeFolder(currentFile.getAbsolutePath());
        } else {
            findViewById(R.id.selectedPanel).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.currentFileSize)).setText(org.apache.commons.io.FileUtils.byteCountToDisplaySize(currentFile.length()));
            ((TextView) findViewById(R.id.currentFileName)).setText(currentFile.getName());
            ((ImageView) findViewById(R.id.currentFileIcon)).setImageResource(FileUtils.getFileIcon(currentFile.getName()));
            findViewById(R.id.sendFileButton).setEnabled(true);
        }
    }
}
