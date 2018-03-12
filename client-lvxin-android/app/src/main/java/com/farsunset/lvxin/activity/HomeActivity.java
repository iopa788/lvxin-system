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

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.farsunset.lvxin.activity.base.CIMMonitorActivity;
import com.farsunset.lvxin.activity.contact.SearchActivity;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.BackgroundThreadHandler;

public class HomeActivity extends CIMMonitorActivity implements OnTabChangeListener {

    private View mConversationTab;
    private View mContactsTab;
    private View mDiscoverTab;
    private View mSettingTab;

    @Override
    public void initComponents() {
        setDisplayHomeAsUpEnabled(false);
        TabHost mHost = (TabHost) this.findViewById(android.R.id.tabhost);
        mHost.setup();
        mHost.setOnTabChangedListener(this);
        initBottomTabs();

        ((TextView) getConversationTab().findViewWithTag("TAB_NAME")).setText(R.string.common_message);
        ((TextView) getContactsTab().findViewWithTag("TAB_NAME")).setText(R.string.common_contacts);
        ((TextView) getDiscoverTab().findViewWithTag("TAB_NAME")).setText(R.string.common_trend);
        ((TextView) getSettingTab().findViewWithTag("TAB_NAME")).setText(R.string.common_my);
        mHost.addTab(mHost.newTabSpec(getString(R.string.common_message)).setIndicator(mConversationTab).setContent(R.id.conversationFragment));
        mHost.addTab(mHost.newTabSpec(getString(R.string.common_contacts)).setIndicator(mContactsTab).setContent(R.id.contactFragment));
        mHost.addTab(mHost.newTabSpec(getString(R.string.common_trend)).setIndicator(mDiscoverTab).setContent(R.id.trendCenterFragment));
        mHost.addTab(mHost.newTabSpec(getString(R.string.common_my)).setIndicator(mSettingTab).setContent(R.id.settingCenterFragment));
        MessageRepository.resetSendingStatus();

        showBetterySavingTipsFirst();

    }

    private void showBetterySavingTipsFirst() {
        if (!Global.getBetterySavingHasShow())
        {
            BackgroundThreadHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(HomeActivity.this, BetterySavingActivity.class));
                }
            }, 3000);
        }
    }

    @Override
    public void onThemeChanged() {
        super.onThemeChanged();
        initBottomTabs();
    }

    @Override
    public boolean getSwipeBackEnable(){
        return false;
    }

    private void initBottomTabs() {
        ((ImageView) getConversationTab().findViewWithTag("TAB_ICON")).setImageResource(R.drawable.tab_message_selector);
        ((ImageView) getContactsTab().findViewWithTag("TAB_ICON")).setImageResource(R.drawable.tab_contacts_selector);
        ((ImageView) getDiscoverTab().findViewWithTag("TAB_ICON")).setImageResource(R.drawable.tab_trend_selector);
        ((ImageView) getSettingTab().findViewWithTag("TAB_ICON")).setImageResource(R.drawable.tab_my_selector);
        ((TextView) getConversationTab().findViewWithTag("TAB_NAME")).setTextColor(ContextCompat.getColorStateList(this, R.color.tab_text_selector));
        ((TextView) getContactsTab().findViewWithTag("TAB_NAME")).setTextColor(ContextCompat.getColorStateList(this, R.color.tab_text_selector));
        ((TextView) getDiscoverTab().findViewWithTag("TAB_NAME")).setTextColor(ContextCompat.getColorStateList(this, R.color.tab_text_selector));
        ((TextView) getSettingTab().findViewWithTag("TAB_NAME")).setTextColor(ContextCompat.getColorStateList(this, R.color.tab_text_selector));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_home;
    }

    @Override
    public int getToolbarTitle() {

        return 0;
    }

    @Override
    public void onBackPressed() {

        this.moveTaskToBack(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        startActivity(new Intent(this, SearchActivity.class));
        return super.onOptionsItemSelected(item);
    }


    public View getConversationTab() {
        if (mConversationTab == null) {
            mConversationTab = LayoutInflater.from(this).inflate(R.layout.layout_home_tab_item, null);
        }
        return mConversationTab;
    }

    public View getContactsTab() {
        if (mContactsTab == null) {
            mContactsTab = LayoutInflater.from(this).inflate(R.layout.layout_home_tab_item, null);
        }
        return mContactsTab;
    }

    public View getDiscoverTab() {
        if (mDiscoverTab == null) {
            mDiscoverTab = LayoutInflater.from(this).inflate(R.layout.layout_home_tab_item, null);
        }
        return mDiscoverTab;
    }

    public View getSettingTab() {
        if (mSettingTab == null) {
            mSettingTab = LayoutInflater.from(this).inflate(R.layout.layout_home_tab_item, null);
        }
        return mSettingTab;
    }

    @Override
    public void onTabChanged(String tag) {
        setTitle(tag);
    }


}
