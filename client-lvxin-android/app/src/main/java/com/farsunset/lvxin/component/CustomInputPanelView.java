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
package com.farsunset.lvxin.component;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.activity.chat.FileChoiceActivity;
import com.farsunset.lvxin.activity.chat.MapLocationActivity;
import com.farsunset.lvxin.activity.util.PhotoAlbumActivity;
import com.farsunset.lvxin.activity.util.VideoRecorderActivity;
import com.farsunset.lvxin.app.PermissionCompat;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.KeyboardUtil;
import com.farsunset.lvxin.util.StringUtils;


public class CustomInputPanelView extends SimpleInputPanelView {

    public final static int PERMISSION_RADIO = 100;
    public final static int PERMISSION_LOCATION = 200;
    private View centerInputBox;
    private View chatEmotionButton;
    private View leftSwitchButton;
    private View chatingSelectMore;
    private View chatingSelectMorePanel;

    public CustomInputPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onFinishInflate() {

        super.onFinishInflate();
        chatingSelectMorePanel = findViewById(R.id.panelAioTool);
        chatingSelectMore = findViewById(R.id.chating_select_more);
        chatEmotionButton = findViewById(R.id.chat_emotion);
        leftSwitchButton = findViewById(R.id.leftSwitchButton);
        chatingSelectMore.setOnClickListener(this);
        findViewById(R.id.tool_camera).setOnClickListener(this);
        findViewById(R.id.tool_photo).setOnClickListener(this);
        findViewById(R.id.tool_file).setOnClickListener(this);
        findViewById(R.id.tool_location).setOnClickListener(this);
        leftSwitchButton.setOnClickListener(this);
        leftSwitchButton.setOnClickListener(this);
        centerInputBox = findViewById(R.id.centerInputBox);


    }


    public boolean checkAudioPermission() {
        boolean hasPermission = PermissionCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO);
        if (!hasPermission) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale((BaseActivity) getContext(), Manifest.permission.RECORD_AUDIO)) {
                final CustomDialog dialog = new CustomDialog(getContext());
                dialog.setTitle(R.string.tip_permission_denied);
                dialog.setMessage((R.string.tip_permission_audio_disable));
                dialog.setOnDialogButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public void onLeftButtonClicked() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onRightButtonClicked() {
                        dialog.dismiss();
                        gotoPermissionSettingActivity();
                    }

                });
                dialog.show();
            } else {
                ActivityCompat.requestPermissions((BaseActivity) getContext(), new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_RADIO);
            }

            return false;
        }

        return true;
    }


    public boolean checkLocationPermission() {
        boolean hasPermission = PermissionCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (!hasPermission) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale((BaseActivity) getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                final CustomDialog dialog = new CustomDialog(getContext());
                dialog.setTitle(R.string.tip_permission_denied);
                dialog.setMessage(getContext().getString(R.string.tip_permission_location_disable));
                dialog.setButtonsText(getContext().getString(R.string.common_cancel), getContext().getString(R.string.common_confirm));
                dialog.setOnDialogButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public void onLeftButtonClicked() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onRightButtonClicked() {
                        dialog.dismiss();
                        gotoPermissionSettingActivity();
                    }

                });
                dialog.show();
            } else {
                ActivityCompat.requestPermissions((BaseActivity) getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
            }

            return false;
        }

        return true;
    }


    private void gotoPermissionSettingActivity() {

        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", getContext().getPackageName(), null));
            getContext().startActivity(intent);
        } catch (Exception e) {
        }
    }


    public void onCameraButtonClicked() {

        Intent intent = new Intent(getContext(), VideoRecorderActivity.class);
        ((AppCompatActivity) getContext()).startActivityForResult(intent, 1);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.chat_emotion:
                if (chatEmotionButton.isSelected()) {
                    chatEmotionButton.setSelected(false);
                    KeyboardUtil.showKeyboard(messageEditText);
                } else {
                    chatEmotionButton.setSelected(true);
                    emoticoPanelView.setVisibility(View.VISIBLE);
                    inputAreaPanelView.setVisibility(View.VISIBLE);
                    chatingSelectMorePanel.setVisibility(View.GONE);
                    KeyboardUtil.hideKeyboard(messageEditText);
                }

                centerInputBox.setVisibility(View.VISIBLE);
                leftSwitchButton.setSelected(false);
                findViewById(R.id.voiceButton).setVisibility(View.GONE);
                break;

            case R.id.chating_select_more:
                if (inputAreaPanelView.getVisibility() == View.VISIBLE && chatingSelectMorePanel.getVisibility() == View.VISIBLE) {
                    chatingSelectMorePanel.setVisibility(View.GONE);
                    messageEditText.requestFocus();
                    KeyboardUtil.showKeyboard(messageEditText);
                } else {
                    inputAreaPanelView.setVisibility(View.VISIBLE);
                    emoticoPanelView.setVisibility(View.GONE);
                    chatEmotionButton.setSelected(false);
                    chatingSelectMorePanel.setVisibility(View.VISIBLE);
                    KeyboardUtil.hideKeyboard(messageEditText);
                }
                centerInputBox.setVisibility(View.VISIBLE);
                leftSwitchButton.setSelected(false);
                findViewById(R.id.voiceButton).setVisibility(View.GONE);
                break;

            case R.id.leftSwitchButton:
                if (leftSwitchButton.isSelected())
                {
                    onKeyboardMenuClicked();
                }else
                {
                    onRadioButtonClicked();
                }

                break;
            case R.id.tool_camera:
                onCameraButtonClicked();
                break;


            case R.id.tool_photo:

                Intent intentFromGallery = new Intent(getContext(), PhotoAlbumActivity.class);
                ((Activity) getContext()).startActivityForResult(intentFromGallery, 2);
                break;


            case R.id.tool_file:

                Intent intentFile = new Intent(getContext(), FileChoiceActivity.class);
                ((Activity) getContext()).startActivityForResult(intentFile, 3);
                break;
            case R.id.tool_location:
                onMapButtonClicked();

                break;

            case R.id.sendMessageButton:
                this.onInputPanelEventListener.onSendButtonClicked(messageEditText.getText().toString());
                break;
        }

    }

    private void onKeyboardMenuClicked(){
        centerInputBox.setVisibility(View.VISIBLE);
        chatingSelectMorePanel.setVisibility(View.GONE);
        leftSwitchButton.setSelected(false);
        findViewById(R.id.voiceButton).setVisibility(View.GONE);
        KeyboardUtil.showKeyboard(messageEditText);
        if (!TextUtils.isEmpty(messageEditText.getText().toString())) {
            chatingSelectMore.setVisibility(View.GONE);
            sendButton.setVisibility(View.VISIBLE);
        }
    }
    public void onRadioButtonClicked() {

        if (!checkAudioPermission()) {
            return;
        }

        chatingSelectMore.setVisibility(View.VISIBLE);
        sendButton.setVisibility(View.INVISIBLE);
        emoticoPanelView.setVisibility(View.GONE);
        chatingSelectMorePanel.setVisibility(View.GONE);
        inputAreaPanelView.setVisibility(GONE);
        chatEmotionButton.setSelected(false);
        leftSwitchButton.setSelected(true);
        findViewById(R.id.voiceButton).setVisibility(View.VISIBLE);
        centerInputBox.setVisibility(View.GONE);
        KeyboardUtil.hideKeyboard(messageEditText);

    }


    public void onMapButtonClicked() {

        if (!checkLocationPermission()) {
            return;
        }
        Intent intentLoc = new Intent(getContext(), MapLocationActivity.class);
        ((Activity) getContext()).startActivityForResult(intentLoc, 4);
    }

    @Override
    public void onKeyboardVisableChanged(boolean visable) {
        if (visable) {
            chatingSelectMorePanel.setVisibility(View.GONE);
            chatEmotionButton.setSelected(false);
        }
    }

    @Override
    public void onTextChanged(CharSequence content, int i, int j, int k) {

        if (messageEditText.getVisibility() != View.VISIBLE) {
            return;
        }
        if (!StringUtils.isEmpty(content)) {
            chatingSelectMore.setVisibility(View.GONE);
            sendButton.setVisibility(View.VISIBLE);
        } else {
            chatingSelectMore.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onKeyboardHeightChanged(int height) {
        super.onKeyboardHeightChanged(height);
        chatingSelectMorePanel.getLayoutParams().height = height;
    }


    @Override
    public void resetInputPanel() {
        KeyboardUtil.hideKeyboard(messageEditText);
        inputAreaPanelView.setVisibility(GONE);
        chatEmotionButton.setSelected(false);
        emoticoPanelView.setVisibility(GONE);
        chatingSelectMorePanel.setVisibility(View.GONE);
    }
}
