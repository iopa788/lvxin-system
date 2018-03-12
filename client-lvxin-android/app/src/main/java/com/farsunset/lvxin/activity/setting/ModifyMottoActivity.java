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


import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.model.SentBody;
import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.pro.R;

/**
 * 修改签名
 */
public class ModifyMottoActivity extends BaseActivity {
    private User user;

    @Override
    public void initComponents() {
        user = Global.getCurrentUser();

        ((EditText) findViewById(R.id.motto)).setText(user.motto);
        findViewById(R.id.motto).requestFocus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_save) {
            save();
        }
        return super.onOptionsItemSelected(item);
    }

    public void save() {
        if (!TextUtils.isEmpty(((EditText) findViewById(R.id.motto)).getText().toString())) {
            user.motto = ((EditText) findViewById(R.id.motto)).getText().toString();
            Global.modifyAccount(user);

            SentBody sent = new SentBody();
            sent.setKey(Constant.CIMRequestKey.CLIENT_MODIFY_PROFILE);
            sent.put("account", user.account);
            sent.put("motto", user.motto);
            sent.put("name", user.name);
            CIMPushManager.sendRequest(this, sent);

            showToastView(R.string.tip_save_complete);
            finish();
        }
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_modify_motto;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_setting_modify_motto;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
