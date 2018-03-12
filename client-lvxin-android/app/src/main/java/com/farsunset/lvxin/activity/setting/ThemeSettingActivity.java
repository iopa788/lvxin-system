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

import android.content.Intent;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.view.ViewGroup;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.SkinManager;
import com.farsunset.lvxin.pro.R;

public class ThemeSettingActivity extends BaseActivity {
    private GridLayout mGridLayout;

    @Override
    public void initComponents() {
        mGridLayout = (GridLayout) findViewById(R.id.gridLayout);
        showAngleMark();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_theme_setting;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_theme_setting;
    }


    public void onThemeChanged(View view) {
        SkinManager.onThemeChanged(view.getTag().toString());
        LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_THEME_CHANGED));
        clearAngleMark();
        showAngleMark();
    }

    private void showAngleMark() {
        String name = SkinManager.getThemeName();
        ((ViewGroup) mGridLayout.findViewWithTag(name)).getChildAt(0).setVisibility(View.VISIBLE);
    }

    private void clearAngleMark() {
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            ((ViewGroup) mGridLayout.getChildAt(i)).getChildAt(0).setVisibility(View.GONE);
        }
    }
}
