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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.activity.contact.UserDetailActivity;
import com.farsunset.lvxin.app.ClientConfig;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.GlobalMediaPlayer;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.database.ShakeRecordRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnShakePhoneListener;
import com.farsunset.lvxin.listener.OnShakePhoneListener.OnShakeListener;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.ShakeRecord;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.FriendResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;
import com.farsunset.lvxin.util.BackgroundThreadHandler;
import com.farsunset.lvxin.util.FileURLBuilder;

public class SNSShakeActivity extends BaseActivity implements HttpRequestListener, OnShakeListener {

    public Runnable saveRunable = new Runnable() {
        @Override
        public void run() {
            performGetNowRequest();
        }
    };
    private RelativeLayout mImgUp;
    private RelativeLayout mImgDn;
    private View shakeTopLine;
    private View shakeBottomLine;
    private View progressView;
    private View matchUserInfoPanel;
    private OnShakePhoneListener mShakeListener;
    private User self;
    private AnimationSet topAnimationSet;
    private AnimationSet bottomAnimationSet;
    private Animation matchApperAnimation;
    private Animation matchDisApperAnimation;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.userpanel) {
            Intent intent = new Intent(SNSShakeActivity.this, UserDetailActivity.class);
            intent.putExtra(Friend.class.getName(), (Friend) matchUserInfoPanel.getTag());
            startActivity(intent);
        }
    }

    @Override
    public void initComponents() {

        initAnimations();
        self = Global.getCurrentUser();
        matchUserInfoPanel = findViewById(R.id.userpanel);
        shakeTopLine = findViewById(R.id.shakeTopLine);
        shakeBottomLine = findViewById(R.id.shakeBottomLine);
        progressView = findViewById(R.id.progress_view);
        mImgUp = (RelativeLayout) findViewById(R.id.shakeImgUp);
        mImgDn = (RelativeLayout) findViewById(R.id.shakeImgDown);
        mShakeListener = new OnShakePhoneListener(this);
        mShakeListener.setOnShakeListener(this);
        matchUserInfoPanel.setOnClickListener(this);
    }

    public void initAnimations() {
        matchApperAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom);
        matchApperAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                matchUserInfoPanel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });

        matchDisApperAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_bottom);
        matchDisApperAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                matchUserInfoPanel.setVisibility(View.GONE);
            }
        });

        topAnimationSet = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.shake_top_translate);
        topAnimationSet.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                shakeTopLine.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                shakeTopLine.setVisibility(View.GONE);
            }
        });
        bottomAnimationSet = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.shake_bottom_translate);
        bottomAnimationSet.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                shakeBottomLine.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                shakeBottomLine.setVisibility(View.GONE);
                progressView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onPhoneShake() {
        mShakeListener.unregisterListener();
        matchUserInfoPanel.startAnimation(matchDisApperAnimation);
        startAnimations();  //开始 摇一摇手掌动画
        payShakeSound(R.raw.shake_sound_male);
        performsSaveRequest();
    }

    private void performsSaveRequest() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.SHAKE_SAVE_URL, BaseResult.class);
        HttpRequestLauncher.execute(requestBody, this);
    }

    private void performGetNowRequest() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.SHAKE_GET_NOW_URL, FriendResult.class);
        HttpRequestLauncher.execute(requestBody, this);
    }

    public void payShakeSound(int i) {
        if (ClientConfig.getShakeSoundEnable()) {
            GlobalMediaPlayer.play(i);
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_snsshake;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_function_shake;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_setting) {
            startActivity(new Intent(this, ShakeSettingActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void startAnimations() {   //定义摇一摇动画动画
        mImgUp.startAnimation(topAnimationSet);
        mImgDn.startAnimation(bottomAnimationSet);
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {

        if (result.isSuccess() && url.equals(URLConstant.SHAKE_SAVE_URL)) {
            BackgroundThreadHandler.postDelayed(saveRunable, 5000);
            return;
        }
        if (url.equals(URLConstant.SHAKE_GET_NOW_URL)) {
            mShakeListener.registerListener();
            progressView.setVisibility(View.GONE);
            if (result.isSuccess()) {
                payShakeSound(R.raw.shake_match);
                Friend target = ((FriendResult) result).data;
                ((WebImageView) findViewById(R.id.icon)).load(FileURLBuilder.getUserIconUrl(target.account), R.drawable.icon_def_head);
                matchUserInfoPanel.startAnimation(matchApperAnimation);
                matchUserInfoPanel.setTag(target);
                ((TextView) findViewById(R.id.username)).setText(target.name);
                if (target.longitude != null || target.latitude != null) {
                    ((TextView) findViewById(R.id.location)).setText(AppTools.transformDistance(self.longitude, self.latitude, target.longitude, target.latitude));
                }

                if (User.GENDER_FEMALE.equals(target.gender)) {
                    ((ImageView) findViewById(R.id.gender)).setImageResource(R.drawable.icon_lady);
                }
                if (User.GENDER_MAN.equals(target.gender)) {
                    ((ImageView) findViewById(R.id.gender)).setImageResource(R.drawable.icon_man);
                }
                ShakeRecordRepository.saveShakeRecord(ShakeRecord.toShakeRecord(target));
            } else {
                payShakeSound(R.raw.shake_nomatch);
            }
        }

    }


    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        payShakeSound(R.raw.shake_nomatch);
        mShakeListener.registerListener();
        progressView.setVisibility(View.GONE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mShakeListener.unregisterListener();
        mShakeListener = null;
        BackgroundThreadHandler.removeCallback(saveRunable);
    }

    @Override
    public void onPause() {
        super.onPause();
        mShakeListener.unregisterListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        mShakeListener.registerListener();
    }

}
