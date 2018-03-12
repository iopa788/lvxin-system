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

import android.widget.TextView;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.pro.BuildConfig;
import com.farsunset.lvxin.pro.R;

public class AboutActivity extends BaseActivity {


    @Override
    public void initComponents() {

        ((TextView) findViewById(R.id.version)).setText(BuildConfig.VERSION_NAME);
        ((TextView) findViewById(R.id.summary)).setText(R.string.about_text);

        findViewById(R.id.item_name).setOnClickListener(this);
        findViewById(R.id.item_qq).setOnClickListener(this);
        findViewById(R.id.item_weixin).setOnClickListener(this);
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_about;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.common_about;
    }


}
