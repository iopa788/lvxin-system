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


import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.ClientConfig;
import com.farsunset.lvxin.pro.R;

/**
 * 摇一摇配置
 */
public class ShakeSettingActivity extends BaseActivity implements OnCheckedChangeListener {
    @Override
    public void initComponents() {
        ((Switch) findViewById(R.id.soundSwitch)).setChecked(ClientConfig.getShakeSoundEnable());
        ((Switch) findViewById(R.id.soundSwitch)).setOnCheckedChangeListener(this);
        findViewById(R.id.item_records).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, ShakeRecordListActivity.class));
    }


    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean flag) {
        ClientConfig.setShakeSoundEnable(flag);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_shake_setting;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.common_setting;
    }
}
