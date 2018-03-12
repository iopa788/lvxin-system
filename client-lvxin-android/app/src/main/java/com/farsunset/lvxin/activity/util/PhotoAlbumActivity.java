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

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.adapter.ImageChooseViewAdapter;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.PermissionCompat;
import com.farsunset.lvxin.bean.Bucket;
import com.farsunset.lvxin.component.AlbumPaddingDecoration;
import com.farsunset.lvxin.dialog.AlbumBucketWindow;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.listener.OnItemCheckedListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.pro.BuildConfig;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AlbumBucketLoader;
import com.farsunset.lvxin.util.AlbumPhotoLoader;
import com.farsunset.lvxin.util.AppTools;

import java.io.File;
import java.util.List;


public class PhotoAlbumActivity extends BaseActivity implements OnItemCheckedListener, OnItemClickedListener,OnDialogButtonClickListener {

    public final static String KEY_MAX_COUNT = "KEY_MAX_COUNT";
    public static final int REQUEST_CODE_ONE = 1287;
    public static final int REQUEST_CODE_MULT = 1587;
    public ImageChooseViewAdapter adapter;
    public RecyclerView recyclerView;
    public float density;
    private Button button;
    private int maxCount;
    private AlbumBucketWindow bucketWindow;
    private CustomDialog permissionDialog;
    @Override
    public void initComponents() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new AlbumPaddingDecoration());
        maxCount = getIntent().getIntExtra(KEY_MAX_COUNT, 1);
        adapter = new ImageChooseViewAdapter(maxCount);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemCheckedListener(this);
        adapter.setOnItemClickedListener(this);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        density = mDisplayMetrics.density;
        bucketWindow = new AlbumBucketWindow(this, this);
        if (checkStoragePermission()) {
            searchImageList();
        }
    }

    public boolean checkStoragePermission() {
        boolean hasPermission = PermissionCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasPermission) {
            return true;
        }
        boolean isPermissionDenied = PermissionCompat.checkPermissionForeverDenied(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (isPermissionDenied) {
            permissionDialog = new CustomDialog(this);
            permissionDialog.setTitle(R.string.tip_permission_denied);
            permissionDialog.setMessage(getString(R.string.tip_permission_album_disable));
            permissionDialog.hideCancelButton();
            permissionDialog.setOnDialogButtonClickListener(this);
            permissionDialog.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 8);
        }

        return false;
    }
    @Override
    public void onLeftButtonClicked() {
        permissionDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        permissionDialog.dismiss();
        gotoPermissionSettingActivity();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            searchImageList();
        } else {
            showToastView(R.string.tip_permission_album_rejected);
        }

    }
    private void gotoPermissionSettingActivity() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        LvxinApplication.getInstance().startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            onBackResultAndFinish();
        }
    }


    public void searchImageList() {
        loadAlbumPhoto(null);
        loadAlbumDir();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_image_choice;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.common_album;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (maxCount > 1) {
            getMenuInflater().inflate(R.menu.menu_album_multiple, menu);
            button = (Button) menu.findItem(R.id.menu_button).getActionView().findViewById(R.id.button);
            button.setOnClickListener(this);
            button.setText(R.string.common_confirm);
            button.setEnabled(false);
        } else {
            getMenuInflater().inflate(R.menu.menu_album, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemChecked(Object file, View view, boolean checked) {


        if (adapter.getSelectedSize() > 0) {
            button.setEnabled(true);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
            button.setText(getString(R.string.label_album_selected_count, adapter.getSelectedSize() + "/" + maxCount));
        } else {
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            button.setEnabled(false);
            button.setText(R.string.common_confirm);
        }

    }

    private void loadAlbumPhoto(String bucket) {
        List<Uri> list = AlbumPhotoLoader.newInstance(this, bucket).syncLoadList();
        adapter.clear();
        adapter.addAll(list);
        adapter.notifyDataSetChanged();

    }

    private void loadAlbumDir() {
        bucketWindow.setAlbumBucketList(new AlbumBucketLoader(this).syncLoadList());
    }

    @Override
    public void onItemClicked(Object obj, View view) {
        if (obj instanceof Uri) {
            onBackResultAndFinish(new File(obj.toString()));
        }
        if (obj instanceof Bucket) {
            loadAlbumPhoto(((Bucket) obj).id);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_camera) {
            AppTools.startCameraActivity(this);
        }
        if (item.getItemId() == R.id.menu_bucket) {
            bucketWindow.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constant.RESULT_CAMERA) {
            File photo = new File(Global.getPhotoGraphFilePath());
            onBackResultAndFinish(photo);
        }
    }

    private void onBackResultAndFinish(File file) {
        Intent intent = new Intent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String authority = BuildConfig.APPLICATION_ID + ".fileprovider";
            Uri contentUri = FileProvider.getUriForFile(this,authority , file);
            intent.setData(contentUri);
        } else {
            intent.setData(Uri.fromFile(file));
        }
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void onBackResultAndFinish() {
        Intent intent = new Intent();
        intent.putExtra("files", adapter.getFinalSelectedFiles());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
