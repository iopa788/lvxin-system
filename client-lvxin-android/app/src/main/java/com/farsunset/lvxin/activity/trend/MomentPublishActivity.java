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
package com.farsunset.lvxin.activity.trend;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.activity.util.PhotoAlbumActivity;
import com.farsunset.lvxin.adapter.PhotoGridViewAdapter;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.database.MomentRepository;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.network.MomentPublishRequester;
import com.farsunset.lvxin.network.model.MapAddress;
import com.farsunset.lvxin.network.model.PubLinkMessage;
import com.farsunset.lvxin.network.model.PublishObject;
import com.farsunset.lvxin.network.model.SNSVideo;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.CommonResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;
import com.farsunset.lvxin.util.StringUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

public class MomentPublishActivity extends BaseActivity implements HttpRequestListener, OnDialogButtonClickListener, OnItemClickedListener {

    public static final int REQUEST_CODE = 7452;
    private PhotoGridViewAdapter adapter;
    private EditText content;
    private User self;
    private CustomDialog customDialog;
    private Moment article = new Moment();
    private PubLinkMessage linkMessage;
    private SNSVideo snsVideo;
    private RecyclerView imageGridView;
    private MapAddress mapAddress;
    private ImageView locIcon;
    private TextView address;

    @Override
    public void initComponents() {


        self = Global.getCurrentUser();
        imageGridView = (RecyclerView) findViewById(R.id.imageGridView);
        imageGridView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);
        imageGridView.setLayoutManager(gridLayoutManager);
        imageGridView.setItemAnimator(new DefaultItemAnimator());
        imageGridView.setEnabled(false);
        content = (EditText) findViewById(R.id.content);
        adapter = new PhotoGridViewAdapter();
        adapter.setOnItemClickedListener(this);
        findViewById(R.id.locationPanel).setOnClickListener(this);
        locIcon = (ImageView) findViewById(R.id.locIcon);
        address = (TextView) findViewById(R.id.address);

        buildPhotoView();
        buildLinkView();
        buildVideoView();
    }

    private void buildVideoView() {
        snsVideo = (SNSVideo) getIntent().getSerializableExtra(SNSVideo.class.getName());
        if (snsVideo != null) {
            WebImageView thumbnailView = ((WebImageView) findViewById(R.id.thumbnailView));
            imageGridView.setVisibility(View.GONE);
            findViewById(R.id.videoPanel).setVisibility(View.VISIBLE);
            findViewById(R.id.removePicTips).setVisibility(View.GONE);
            findViewById(R.id.videoPanel).setOnClickListener(this);
            File thumbnailFile = new File(LvxinApplication.CACHE_DIR_VIDEO, snsVideo.thumbnail);
            thumbnailView.load(thumbnailFile);
        }
    }

    private void buildLinkView() {
        linkMessage = (PubLinkMessage) getIntent().getSerializableExtra(PubLinkMessage.NAME);
        if (linkMessage != null) {
            imageGridView.setVisibility(View.GONE);
            findViewById(R.id.linkPanel).setVisibility(View.VISIBLE);
            findViewById(R.id.removePicTips).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.linkTitle)).setText(linkMessage.title);
        }
    }

    private void buildPhotoView() {
        imageGridView.setVisibility(View.VISIBLE);
        imageGridView.setAdapter(adapter);
        registerForContextMenu(imageGridView);
        customDialog = new CustomDialog(this);
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setTitle(R.string.title_add_picture);
        customDialog.setMessage((R.string.title_choice_picture));
        customDialog.setButtonsText(getString(R.string.common_camera), getString(R.string.common_album));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.videoPanel) {
            LvxinApplication.getInstance().startVideoActivity(this, false, snsVideo, v);
        }
        if (v.getId() == R.id.locationPanel) {
            startActivityForResult(new Intent(this, MapAddressActivity.class), MapAddressActivity.REQUEST_CODE);
        }
    }


    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {

        hideProgressDialog();
        if (result.isSuccess()) {
            this.article.gid = String.valueOf(((CommonResult) result).data);
            this.article.timestamp = System.currentTimeMillis();
            MomentRepository.add(article);
            showToastView(R.string.tip_publish_complete);
            Intent intent = new Intent();
            intent.putExtra(Moment.class.getName(), article);
            setResult(Activity.RESULT_OK, intent);
            finish();
            adapter.getImageList().clear();
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        hideProgressDialog();
    }

    public void buildMomentObject() {
        if (adapter.getListSize() > 0) {
            article.thumbnail = new Gson().toJson(adapter.getImageList());
        }
        article.type = Moment.FORMAT_TEXT_IMAGE;
        PublishObject object = new PublishObject();
        object.content = content.getText().toString().trim();
        object.name = self.name;
        object.location = mapAddress;
        object.link = linkMessage;
        object.video = snsVideo;

        if (object.link != null) {
            article.type = Moment.FORMAT_LINK;
        }
        if (object.video != null) {
            article.type = Moment.FORMAT_VIDEO;
        }

        article.account = self.account;
        article.content = new Gson().toJson(object);
    }

    // 添加上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        menu.add(1, Integer.MAX_VALUE, 1, getString(R.string.common_delete));
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    // 响应上下文菜单
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == Integer.MAX_VALUE) {
            adapter.removeSelected();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PhotoAlbumActivity.REQUEST_CODE_MULT) {
            adapter.addAll((List<File>) data.getSerializableExtra("files"));
        } else if (resultCode == RESULT_OK && requestCode == Constant.RESULT_CAMERA) {
            File photo = new File(Global.getPhotoGraphFilePath());
            adapter.add(photo);
        } else if (resultCode == RESULT_OK && requestCode == MapAddressActivity.REQUEST_CODE) {
            mapAddress = (MapAddress) data.getSerializableExtra(MapAddress.class.getName());
            locIcon.setSelected(true);
            address.setText(mapAddress.name);
        } else if (resultCode == RESULT_CANCELED && requestCode == MapAddressActivity.REQUEST_CODE) {
            mapAddress = null;
            locIcon.setSelected(false);
            address.setText(R.string.label_select_location);
        }
    }


    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
        AppTools.startCameraActivity(this);
    }

    @Override
    public void onRightButtonClicked() {
        customDialog.dismiss();
        Intent intent = new Intent(this, PhotoAlbumActivity.class);
        intent.putExtra(PhotoAlbumActivity.KEY_MAX_COUNT, 9 - adapter.getListSize());
        startActivityForResult(intent, PhotoAlbumActivity.REQUEST_CODE_MULT);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_moment_publish;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_moment_publish;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_send) {
            preparePublishMoment();
        }
        return super.onOptionsItemSelected(item);
    }

    private void preparePublishMoment() {
        if (adapter.isEmpty() && StringUtils.isEmpty(content.getText().toString()) && linkMessage == null && snsVideo == null) {
            showToastView(R.string.tip_required_article);
            return;
        }
        buildMomentObject();
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.label_moment_publish)));
        MomentPublishRequester.publish(article, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClicked(Object obj, View view) {
        if (adapter.getListSize() == 9) {
            showToastView(R.string.tip_max_nine_picture);
        } else {
            customDialog.show();
        }
    }
}
