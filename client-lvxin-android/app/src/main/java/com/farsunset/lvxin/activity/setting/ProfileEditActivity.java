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
package com.farsunset.lvxin.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.model.SentBody;
import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.activity.util.PhotoAlbumActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.FileResource;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.database.GlideImageRepository;
import com.farsunset.lvxin.database.OrganizationRepository;
import com.farsunset.lvxin.listener.OSSFileUploadListener;
import com.farsunset.lvxin.network.CloudFileUploader;
import com.farsunset.lvxin.network.CloudImageLoaderFactory;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;
import com.farsunset.lvxin.util.FileURLBuilder;

import java.io.File;

public class ProfileEditActivity extends BaseActivity implements OSSFileUploadListener {

    private WebImageView icon;
    private User user;

    @Override
    public void initComponents() {
        user = Global.getCurrentUser();

        //findViewById(R.id.iconSwicth).setOnClickListener(this);
        findViewById(R.id.modify_motto_item).setOnClickListener(this);

        ((TextView) findViewById(R.id.account)).setText(user.account);
        ((TextView) findViewById(R.id.name)).setText(user.name);
        ((TextView) findViewById(R.id.email)).setText(user.email);
        ((TextView) findViewById(R.id.telephone)).setText(user.telephone);
        ((TextView) findViewById(R.id.org)).setText(OrganizationRepository.queryOrgName(user.orgCode));


        if (User.GENDER_MAN.equals(user.gender)) {
            ((TextView) findViewById(R.id.gender)).setText(R.string.common_man);
        }
        if (User.GENDER_FEMALE.equals(user.gender)) {
            ((TextView) findViewById(R.id.gender)).setText(R.string.common_female);
        }

        icon = (WebImageView) findViewById(R.id.icon);
        icon.load(FileURLBuilder.getUserIconUrl(user.account), R.drawable.icon_def_head);
    }

    @Override
    public void onResume() {
        super.onResume();
        user = Global.getCurrentUser();
        ((TextView) findViewById(R.id.motto)).setText(user.motto);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iconSwicth)
        {
            startActivityForResult(new Intent(this, PhotoAlbumActivity.class), 9);
        }
        if (v.getId() == R.id.modify_motto_item)
        {
            startActivity(new Intent(this, ModifyMottoActivity.class));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 9) {
            AppTools.startPhotoZoom(this, data.getData());
        }
        if (resultCode == Activity.RESULT_OK && requestCode == Constant.RESULT_ZOOM && data != null) {
            CloudImageLoaderFactory.get().clearMemory();
            File photo = new File(Global.getCropPhotoFilePath());
            CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_USER_ICON, user.account, photo, this);
            showProgressDialog(getString(R.string.tip_file_uploading, 0));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.single_icon, menu);
        //menu.findItem(R.id.menu_icon).setIcon(R.drawable.icon_menu_lock);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_icon) {
            startActivity(new Intent(this, ModifyPasswordActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_profile_edit;
    }


    @Override
    public int getToolbarTitle() {

        return R.string.label_setting_profile;
    }

    @Override
    public void onUploadCompleted(FileResource resource) {
        hideProgressDialog();
        SentBody sent = new SentBody();
        sent.setKey(Constant.CIMRequestKey.CLIENT_MODIFY_LOGO);
        sent.put("account", user.account);
        CIMPushManager.sendRequest(this, sent);
        showToastView(R.string.tip_logo_updated);

        GlideImageRepository.save(FileURLBuilder.getUserIconUrl(user.account), String.valueOf(System.currentTimeMillis()));
        icon.load(FileURLBuilder.getUserIconUrl(user.account));

        LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_LOGO_CHANGED));
    }

    @Override
    public void onUploadProgress(String key, float progress) {
        showProgressDialog(getString(R.string.tip_file_uploading, (int) progress));
    }

    @Override
    public void onUploadFailured(FileResource resource, Exception e) {
        hideProgressDialog();
        showToastView(R.string.tip_logo_update_error);
    }
}
