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


import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.activity.chat.FriendChatActivity;
import com.farsunset.lvxin.activity.trend.FriendMomentActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.database.MomentRuleRepository;
import com.farsunset.lvxin.database.OrganizationRepository;
import com.farsunset.lvxin.database.StarMarkRepository;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.MomentRule;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;

public class UserDetailActivity extends BaseActivity {
    private Friend friend;
    private User self;

    @Override
    public void initComponents() {
        super.setStatusBarColor(Color.TRANSPARENT);
        super.setWindowFullscreen();
        friend = (Friend) this.getIntent().getSerializableExtra(Friend.class.getName());
        self = Global.getCurrentUser();

        if (!self.account.equals(friend.account)) {

            findViewById(R.id.bar_album).setOnClickListener(this);
            findViewById(R.id.bar_menu_starmark).setOnClickListener(this);
            findViewById(R.id.bar_chat).setOnClickListener(this);
            findViewById(R.id.bar_menu_momentrule).setOnClickListener(this);

            if (MomentRuleRepository.query(self.account, friend.account, MomentRule.TYPE_0) != null) {
                findViewById(R.id.icon_moment_limited).setSelected(true);
            }
            if (StarMarkRepository.isStarMark(friend.account)) {
                findViewById(R.id.icon_starmark).setSelected(true);
            }

        } else {
            findViewById(R.id.bar_panel).setVisibility(View.GONE);
        }

        WebImageView icon = ((WebImageView) findViewById(R.id.icon));
        icon.setPopuShowAble();
        ((TextView) findViewById(R.id.account)).setText(friend.account);
        ((TextView) findViewById(R.id.email)).setText(friend.email);
        ((TextView) findViewById(R.id.telephone)).setText(friend.telephone);
        ((TextView) findViewById(R.id.org)).setText(OrganizationRepository.queryOrgName(friend.orgCode));

        icon.load(FileURLBuilder.getUserIconUrl(friend.account), R.drawable.icon_def_head, 999);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(friend.name);
        ((TextView) findViewById(R.id.motto)).setText(friend.motto);

        ImageView genderIcon = ((ImageView) findViewById(R.id.icon_gender));
        if (User.GENDER_MAN.equals(friend.gender)) {
            genderIcon.setVisibility(View.VISIBLE);
            genderIcon.setImageResource(R.drawable.icon_man);
        }
        if (User.GENDER_FEMALE.equals(friend.gender)) {
            genderIcon.setVisibility(View.VISIBLE);
            genderIcon.setImageResource(R.drawable.icon_lady);
        }

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.bar_album:
                Intent uintent = new Intent(this, FriendMomentActivity.class);
                uintent.putExtra(Friend.class.getName(), friend);
                startActivity(uintent);
                break;
            case R.id.bar_menu_momentrule:
                Intent intent = new Intent(this, MomentRuleActivity.class);
                intent.putExtra(Friend.NAME, friend);
                startActivityForResult(intent, MomentRuleActivity.REQUEST_CODE);
                break;
            case R.id.bar_chat:
                Intent chatIntent = new Intent(this, FriendChatActivity.class);
                chatIntent.putExtra(Constant.CHAT_OTHRES_ID, friend.account);
                chatIntent.putExtra(Constant.CHAT_OTHRES_NAME, friend.name);
                startActivity(chatIntent);
                break;
            case R.id.bar_menu_starmark:
                if (StarMarkRepository.isStarMark(friend.account)) {
                    StarMarkRepository.delete(friend.account);
                    findViewById(R.id.icon_starmark).setSelected(false);
                } else {
                    StarMarkRepository.save(friend.account);
                    findViewById(R.id.icon_starmark).setSelected(true);
                }
                LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (MomentRuleRepository.query(self.account, friend.account, MomentRule.TYPE_0) != null) {
            findViewById(R.id.icon_moment_limited).setSelected(true);
        } else {
            findViewById(R.id.icon_moment_limited).setSelected(false);
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_user_detail;
    }


    @Override
    public int getToolbarTitle() {
        return R.string.common_detailed;
    }

}
