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


import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.ClientConfig;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.database.GlobalDatabaseHelper;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 消息配置
 */
public class ServerSettingActivity extends BaseActivity implements OnDialogButtonClickListener {

    private EditText port;
    private EditText path;
    private CustomDialog customDialog;

    @Override
    public void initComponents() {

        path = (EditText) findViewById(R.id.path);
        path.setText(ClientConfig.getServerPath());
        path.requestFocus();
        port = (EditText) findViewById(R.id.port);
        port.setText(String.valueOf(ClientConfig.getServerCIMPort()));
        customDialog = new CustomDialog(this);
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setTitle((R.string.title_save_config));
        customDialog.setMessage((R.string.tip_save_server_config));

    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_server_setting;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_setting_server;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            do {

                if (TextUtils.isEmpty(path.getText().toString().trim())) {
                    showToastView(R.string.tip_required_server_path);
                    break;
                }
                if (!StringUtils.isWebUrl(path.getText().toString().trim())) {
                    showToastView(R.string.tip_invalid_server_path);
                    break;
                }

                if (TextUtils.isEmpty(port.getText().toString().trim())) {
                    showToastView(R.string.tip_required_server_cimport);
                    break;
                }
                customDialog.show();
            } while (false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    private void restartApp() {
        URLConstant.initialize();
        Global.removeAccount(new AccountManagerCallback() {
            @Override
            public void run(AccountManagerFuture accountManagerFuture) {
                CIMPushManager.destroy(LvxinApplication.getInstance());
                GlobalDatabaseHelper.onServerChanged();
                LvxinApplication.getInstance().restartSelf();
            }
        });
    }

    @Override
    public void onRightButtonClicked() {
        customDialog.dismiss();

        try {
            URL url = new URL(path.getText().toString().trim());
            ClientConfig.setServerHost(url.getHost());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ClientConfig.setServerPath(path.getText().toString().trim());
        ClientConfig.setServerCIMPort(Integer.parseInt(port.getText().toString().trim()));
        restartApp();
    }

}
