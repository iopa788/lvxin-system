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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farsunset.lvxin.activity.HomeActivity;
import com.farsunset.lvxin.activity.base.CIMMonitorFragment;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;

public class TrendCenterFragment extends CIMMonitorFragment implements OnClickListener {

    private TextView mTabBadge;
    private TextView mMomentBadge;
    private TextView mBottleBadge;
    private View mTabRedDot;
    private View mMomentHintView;
    private WebImageView mMomentIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_trend, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        findViewById(R.id.nearbyItem).setOnClickListener(this);
        findViewById(R.id.circleItem).setOnClickListener(this);
        findViewById(R.id.bottleItem).setOnClickListener(this);
        findViewById(R.id.shakeItem).setOnClickListener(this);
        mMomentHintView = findViewById(R.id.newMsgHintView);
        mMomentIcon = (WebImageView) findViewById(R.id.newMsgUserIcon);
        View tabView = ((HomeActivity) getActivity()).getDiscoverTab();
        mTabRedDot = tabView.findViewById(R.id.reddot);
        mTabBadge = (TextView) tabView.findViewById(R.id.badge);
        mBottleBadge = (TextView) findViewById(R.id.bottle_new_msg_count_label);
        mMomentBadge = (TextView) findViewById(R.id.new_msg_count_label);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshNewMessageHintView();
    }


    private void refreshNewMessageHintView() {
        long momentCount = MessageRepository.countNewMessage(new String[]{Constant.MessageAction.ACTION_801, Constant.MessageAction.ACTION_802});
        mMomentBadge.setVisibility(momentCount > 0 ? View.VISIBLE : View.GONE);
        mMomentBadge.setText(momentCount > 0 ? String.valueOf(momentCount) : null);

        Message momentMsg = MessageRepository.queryNewMomentMessage();
        mTabRedDot.setVisibility(momentMsg != null ? View.VISIBLE : View.GONE);
        mMomentHintView.setVisibility(momentMsg != null ? View.VISIBLE : View.GONE);
        if (momentMsg != null) {
            mMomentIcon.load(FileURLBuilder.getUserIconUrl(momentMsg.sender), R.drawable.icon_def_head);
        }


        long bottleCount = MessageRepository.countNewByType(Constant.MessageAction.ACTION_700);
        long sumCount = bottleCount + momentCount;
        mTabBadge.setVisibility(sumCount > 0 ? View.VISIBLE : View.GONE);
        mTabBadge.setText(sumCount > 0 ? String.valueOf(sumCount) : null);


        mBottleBadge.setVisibility(bottleCount > 0 ? View.VISIBLE : View.GONE);
        mBottleBadge.setText(bottleCount > 0 ? String.valueOf(bottleCount) : null);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shakeItem:
                this.startActivity(new Intent(this.getActivity(), SNSShakeActivity.class));
                break;
            case R.id.nearbyItem:
                this.startActivity(new Intent(this.getActivity(), NearbyUserListActivity.class));
                break;
            case R.id.bottleItem:
                this.startActivity(new Intent(this.getActivity(), DriftBottleActivity.class));
                break;
            case R.id.circleItem:
                Intent cintent = new Intent(this.getActivity(), SNSMomentActivity.class);
                this.startActivity(cintent);
                mMomentHintView.setVisibility(View.GONE);
                mTabRedDot.setVisibility(View.GONE);
                mTabBadge.setVisibility(View.GONE);
                mTabBadge.setText(null);
                mMomentBadge.setVisibility(View.GONE);
                mMomentBadge.setText(null);
                break;
        }
    }

    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message msg) {

        if (Constant.MessageAction.ACTION_700.equals(msg.getAction())
                || Constant.MessageAction.ACTION_800.equals(msg.getAction())
                || Constant.MessageAction.ACTION_801.equals(msg.getAction())
                || Constant.MessageAction.ACTION_802.equals(msg.getAction())
                || Constant.MessageAction.ACTION_803.equals(msg.getAction())
                || Constant.MessageAction.ACTION_804.equals(msg.getAction())) {
            refreshNewMessageHintView();
        }

    }
}
