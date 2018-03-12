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

import android.os.Build;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.pro.BuildConfig;
import com.farsunset.lvxin.pro.R;

public class FeedbackActivity extends BaseActivity {


    @Override
    public void initComponents() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String text = ((EditText) findViewById(R.id.text)).getText().toString();
        if (item.getItemId() == R.id.menu_send && !TextUtils.isEmpty(text)) {
            performFeedbackRequest(text);
            showToastView(R.string.tip_feedback_completed);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void performFeedbackRequest(String text) {

        showToastView(R.string.tip_feedback_completed);
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.FEEDBACK_PUBLISH_URL, BaseResult.class);
        requestBody.addParameter("appVersion", BuildConfig.VERSION_NAME);
        requestBody.addParameter("sdkVersion", Build.VERSION.RELEASE);
        requestBody.addParameter("deviceModel", Build.MODEL);
        requestBody.addParameter("content", text);
        HttpRequestLauncher.executeQuietly(requestBody);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_feedback;
    }


    @Override
    public int getToolbarTitle() {
        return R.string.common_feedback;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
