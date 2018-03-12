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
package com.farsunset.lvxin.activity;

import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.constant.CIMConstant;
import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.activity.setting.ServerSettingActivity;
import com.farsunset.lvxin.app.ClientConfig;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.LoginBallsView;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.database.GlobalDatabaseHelper;
import com.farsunset.lvxin.listener.CloudImageApplyListener;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.LoginResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.farsunset.lvxin.util.MD5;

public class LoginActivity extends BaseActivity implements TextWatcher, HttpRequestListener, CloudImageApplyListener {
    private EditText accountEdit;
    private EditText passwordEdit;
    private View loginButton;
    private WebImageView icon;
    private LoginBallsView ballsView;
    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkAccountAndPassowrd();
        }

    };

    @Override
    public void initComponents() {
        super.setStatusBarColor(Color.TRANSPARENT);
        super.setWindowFullscreen();
        cancelNotification();
        setDisplayHomeAsUpEnabled(false);
        User self = Global.getCurrentUser();
        ballsView = ((LoginBallsView) findViewById(R.id.balls));
        icon = ((WebImageView) findViewById(R.id.icon));
        findViewById(R.id.label_config).setOnClickListener(this);
        accountEdit = (EditText) this.findViewById(R.id.account);
        passwordEdit = (EditText) this.findViewById(R.id.password);
        loginButton = this.findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        accountEdit.addTextChangedListener(this);
        passwordEdit.addTextChangedListener(passwordTextWatcher);

        if (self != null) {
            icon.load(FileURLBuilder.getUserIconUrl(FileURLBuilder.getUserIconUrl(self.account)), 200, LoginActivity.this);
            accountEdit.setText(self.account);
            passwordEdit.requestFocus();
        }
    }

    @Override
    public boolean getSwipeBackEnable(){
        return false;
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_button:
                if (!checkServerConfig()) {
                    showToastView(R.string.tip_please_configure_serverinfo);
                    return;
                }

                performLoginRequest();
                break;
            case R.id.label_config:
                startActivity(new Intent(this, ServerSettingActivity.class));
                break;
        }
    }

    private void performLoginRequest() {

        showProgressDialog(getString(R.string.tip_loading, getString(R.string.label_login)));

        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.USER_LOGIN_URL, LoginResult.class);
        requestBody.addParameter("password", MD5.digest(passwordEdit.getText().toString().trim()));
        requestBody.addParameter("account", accountEdit.getText().toString().trim());
        HttpRequestLauncher.execute(requestBody, this);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (!TextUtils.isEmpty(accountEdit.getText().toString().trim())) {
            icon.load(FileURLBuilder.getUserIconUrl(accountEdit.getText().toString().trim()), R.drawable.icon_def_head, 200, LoginActivity.this);
        } else {
            icon.setImageResource(R.drawable.icon_def_head);
        }

        checkAccountAndPassowrd();

    }

    private void checkAccountAndPassowrd() {
        if ((accountEdit.getText().toString().length() > 0 && passwordEdit.getText().toString().length() > 0)) {
            loginButton.setEnabled(true);
        } else {
            loginButton.setEnabled(false);
        }
    }


    @Override
    public void onHttpRequestSucceed(final BaseResult result, String url) {
        hideProgressDialog();
        if (result.isSuccess()) {
            ballsView.runaway();
            Global.removeAccount(new AccountManagerCallback() {
                @Override
                public void run(AccountManagerFuture accountManagerFuture) {
                    LoginResult loginResult = (LoginResult) result;
                    User user = loginResult.data;
                    user.password = MD5.digest(passwordEdit.getText().toString().trim());
                    Global.addAccount(user);
                    Global.saveAccessToken(loginResult.token);
                    handleLogined();
                }
            });
        }
        if (CIMConstant.ReturnCode.CODE_403.equals(result.code)) {
            showToastView(R.string.tip_account_or_password_error);
        }
        if (CIMConstant.ReturnCode.CODE_404.equals(result.code)) {
            showToastView(R.string.tip_account_invailed);
        }
        if (CIMConstant.ReturnCode.CODE_405.equals(result.code)) {
            showToastView(R.string.tip_account_disabled);
        }
    }

    private void handleLogined() {

        GlobalDatabaseHelper.onAccountChanged();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.appear, R.anim.disappear);
        finish();
    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        hideProgressDialog();
        showToastView(R.string.tip_conn_server_failed);
    }

    private boolean checkServerConfig() {
        return !(ClientConfig.getServerHost() == null || ClientConfig.getServerPath() == null || ClientConfig.getServerCIMPort() == 0);
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getAction() == null) {
            CIMPushManager.destroy(this);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        this.finish();
    }

    @Override
    public void onImageApplyFailure(Object key, ImageView target) {
        icon.setImageBitmap(null);
    }

    @Override
    public void onImageApplySucceed(Object key, ImageView target, BitmapDrawable resource) {
        icon.startAnimation(AnimationUtils.loadAnimation(this, R.anim.appear));
    }

    private void cancelNotification() {
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
    }

}
