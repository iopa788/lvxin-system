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
package com.farsunset.lvxin.activity.contact;


import android.annotation.SuppressLint;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.database.MomentRuleRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.MomentRule;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.pro.R;

/**
 * 消息配置
 */
public class MomentRuleActivity extends BaseActivity implements OnCheckedChangeListener, HttpRequestListener {

    public static final int REQUEST_CODE = 7583;
    private Friend mFriend;
    private String mSelfAccount;
    private MomentRule mRule;

    @Override
    @SuppressLint("NewApi")
    public void initComponents() {

        mFriend = (Friend) getIntent().getSerializableExtra(Friend.NAME);
        mSelfAccount = Global.getCurrentAccount();

        if (MomentRuleRepository.query(mSelfAccount, mFriend.account, MomentRule.TYPE_0) != null) {
            ((Switch) findViewById(R.id.momentShieldSwitch)).setChecked(true);
        }

        if (MomentRuleRepository.query(mSelfAccount, mFriend.account, MomentRule.TYPE_1) != null) {
            ((Switch) findViewById(R.id.momentIgnoreSwitch)).setChecked(true);
        }

        ((Switch) findViewById(R.id.momentIgnoreSwitch)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.momentShieldSwitch)).setOnCheckedChangeListener(this);
    }


    @Override
    public void onDestroy() {
        setResult(RESULT_OK);
        super.onDestroy();
    }


    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean flag) {
        if (arg0.getId() == R.id.momentShieldSwitch) {
            performSettingRuleRequest(flag, MomentRule.TYPE_0);
        }
        if (arg0.getId() == R.id.momentIgnoreSwitch) {
            performSettingRuleRequest(flag, MomentRule.TYPE_1);
        }
    }

    private void performSettingRuleRequest(boolean isOpen, String type) {

        showProgressDialog(getString(R.string.tip_save_loading));

        mRule = new MomentRule();
        mRule.type = type;
        mRule.account = mSelfAccount;
        mRule.otherAccount = mFriend.account;

        HttpRequestBody requestBody = new HttpRequestBody(isOpen ? URLConstant.MOMENT_RULE_SAVE : URLConstant.MOMENT_RULE_DELETE, BaseResult.class);
        requestBody.addParameter("otherAccount", mFriend.account);
        requestBody.addParameter("type", type);
        HttpRequestLauncher.execute(requestBody, this);
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_moment_rule;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_moment_rule;
    }


    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {

        if (url.equals(URLConstant.MOMENT_RULE_SAVE)) {
            MomentRuleRepository.add(mRule);
        }
        if (url.equals(URLConstant.MOMENT_RULE_DELETE)) {
            MomentRuleRepository.remove(mRule);
        }
        hideProgressDialog();
    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        hideProgressDialog();
    }

}
