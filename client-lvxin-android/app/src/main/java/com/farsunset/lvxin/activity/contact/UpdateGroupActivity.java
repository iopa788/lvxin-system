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
package com.farsunset.lvxin.activity.contact;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.activity.util.PhotoAlbumActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.database.GroupRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.network.CloudFileUploader;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.GroupResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.farsunset.lvxin.util.StringUtils;

import java.io.File;

public class UpdateGroupActivity extends BaseActivity implements OnClickListener, HttpRequestListener {

    public static final int REQUEST_CODE = 8562;
    private EditText name;
    private EditText summary;
    private WebImageView icon;
    private File file;
    private Group group;

    @Override
    public void initComponents() {


        group = (Group) this.getIntent().getExtras().getSerializable(Group.NAME);
        name = (EditText) this.findViewById(R.id.name);
        summary = (EditText) this.findViewById(R.id.summary);
        icon = (WebImageView) this.findViewById(R.id.icon);
        this.findViewById(R.id.iconSwitch).setOnClickListener(this);
        name.setText(group.name);
        summary.setText(group.summary);
        icon.load(FileURLBuilder.getGroupIconUrl(group.groupId), R.drawable.logo_group_normal);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            do {
                if (StringUtils.isEmpty(name.getText())) {
                    showToastView(R.string.tip_required_name);
                    break;
                }
                group.summary = summary.getText().toString();
                group.name = name.getText().toString();
                performUpdateRequest();

            } while (false);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        Intent intentFromGallery = new Intent();
        intentFromGallery.setClass(this, PhotoAlbumActivity.class);
        startActivityForResult(intentFromGallery, 9);
    }

    private void performUpdateRequest() {

        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_save)));
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUP_UPDATE_URL, GroupResult.class);
        requestBody.addParameter("name", group.name);
        requestBody.addParameter("groupId", group.groupId);
        requestBody.addParameter("summary", group.summary);
        HttpRequestLauncher.execute(requestBody, this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 9) {

            AppTools.startPhotoZoom(this, data.getData());
        }
        if (resultCode == Activity.RESULT_OK && requestCode == Constant.RESULT_ZOOM && data != null) {
            file = new File(Global.getCropPhotoFilePath());
            icon.load(file);
        }
    }


    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {
        hideProgressDialog();
        if (result.isSuccess()) {
            if (file != null) {

                CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_GROUP_ICON, group.groupId, file, null);
            }
            GroupRepository.update(group);
            showToastView(R.string.tip_save_complete);
            setResult(RESULT_OK);
            finish();
        }
    }


    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        hideProgressDialog();
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_create_group;
    }

    @Override
    public int getToolbarTitle() {

        return R.string.label_group_modify;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
