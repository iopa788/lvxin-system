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


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.MD5;
import com.farsunset.lvxin.util.StringUtils;

/**
 * 修改密码
 */
public class ModifyPasswordActivity extends BaseActivity implements HttpRequestListener {
    private User user;

    @Override
    public void initComponents() {

        user = Global.getCurrentUser();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save
                && StringUtils.isNotEmpty(((EditText) findViewById(R.id.oldPassword)).getText().toString().trim())
                && StringUtils.isNotEmpty(((EditText) findViewById(R.id.newPassword)).getText().toString().trim())) {
            performUpdateRequest();
        }
        return super.onOptionsItemSelected(item);
    }


    private void performUpdateRequest() {

        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_save)));
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.USER_MODIFYPASSWORD_URL, BaseResult.class);
        requestBody.addParameter("oldPassword", MD5.digest(((EditText) findViewById(R.id.oldPassword)).getText().toString().trim()));
        requestBody.addParameter("newPassword", MD5.digest(((EditText) findViewById(R.id.newPassword)).getText().toString().trim()));
        HttpRequestLauncher.execute(requestBody, this);
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {

        hideProgressDialog();
        if (result.isSuccess()) {
            showToastView(R.string.tip_save_complete);
            user.password = ((EditText) findViewById(R.id.newPassword)).getText().toString().trim();
            Global.modifyAccount(user);
            finish();
        } else {
            showToastView(R.string.tip_oldpassword_error);
        }
    }


    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        hideProgressDialog();
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_modify_passord;
    }


    @Override
    public int getToolbarTitle() {
        return R.string.label_setting_modify_password;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
