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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.activity.util.PhotoAlbumActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.database.GroupMemberRepository;
import com.farsunset.lvxin.database.GroupRepository;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.GroupMember;
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

public class CreateGroupActivity extends BaseActivity implements HttpRequestListener, OnDialogButtonClickListener {

    private EditText name;
    private EditText summary;
    private CustomDialog customDialog;
    private WebImageView icon;
    private File file;
    private Group newGroup = new Group();
    private User self;


    @Override
    public void initComponents() {


        name = (EditText) this.findViewById(R.id.name);
        summary = (EditText) this.findViewById(R.id.summary);
        icon = (WebImageView) this.findViewById(R.id.icon);
        this.findViewById(R.id.iconSwitch).setOnClickListener(this);

        customDialog = new CustomDialog(this);
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setTitle(R.string.label_group_create_succesed);
        customDialog.setMessage((R.string.tip_group_create_succesed));
        self = Global.getCurrentUser();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:

                doCreate();
                break;


            case R.id.iconSwitch:
                Intent intentFromGallery = new Intent();
                intentFromGallery.setClass(this, PhotoAlbumActivity.class);
                startActivityForResult(intentFromGallery, 9);

                break;

        }

    }

    private void doCreate() {


        newGroup.summary = summary.getText().toString();
        if (StringUtils.isEmpty(name.getText())) {

            showToastView(R.string.tip_required_name);
            return;
        }
        newGroup.name = name.getText().toString();
        newGroup.founder = self.account;

        performCreateRequest();
    }

    private void performCreateRequest() {

        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_create)));

        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUP_CREATE_URL, GroupResult.class);
        requestBody.addParameter("name", newGroup.name);
        requestBody.addParameter("founder", newGroup.founder);
        requestBody.addParameter("summary", newGroup.summary);
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
            newGroup = ((GroupResult) result).data;
            if (file != null) {

                CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_GROUP_ICON, newGroup.groupId, file, null);
            }
            GroupMember member = new GroupMember();
            member.account = self.account;
            member.groupId = newGroup.groupId;
            member.gid = StringUtils.getUUID();
            member.host = GroupMember.RULE_FOUNDER;
            GroupRepository.add(newGroup);
            GroupMemberRepository.saveMember(member);
            customDialog.show();

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

        return R.string.label_group_create;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_button, menu);
        Button button = (Button) menu.findItem(R.id.menu_button).getActionView().findViewById(R.id.button);
        button.setOnClickListener(this);
        button.setText(R.string.common_create);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onLeftButtonClicked() {

        customDialog.dismiss();
        finish();
    }

    @Override
    public void onRightButtonClicked() {
        customDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("group", newGroup);
        intent.setClass(CreateGroupActivity.this, InviteGroupMemberActivity.class);
        startActivity(intent);

        finish();


    }


}
