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

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.ClientConfig;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.model.Bottle;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.pro.R;


public class BottlePublishActivity extends BaseActivity implements HttpRequestListener {

    private EditText content;
    private User self;

    @Override
    public void initComponents() {


        self = Global.getCurrentUser();
        content = (EditText) findViewById(R.id.content);
    }


    private void performThrowBottleRequest() {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.label_throw)));
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.BOTTLE_THROW_URL, BaseResult.class);
        requestBody.addParameter("content", content.getText().toString());
        requestBody.addParameter("type", String.valueOf(Bottle.TYPE_TXT));
        requestBody.addParameter("sender", self.account);
        requestBody.addParameter("region", ClientConfig.getCurrentRegion());
        HttpRequestLauncher.execute(requestBody, this);
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {

        hideProgressDialog();
        if (result.isSuccess()) {
            showToastView(R.string.tip_bottle_publish_complete);
            finish();
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        hideProgressDialog();
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_bottle_publish;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_function_bottle;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_send && !TextUtils.isEmpty(content.getText())) {
            performThrowBottleRequest();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
