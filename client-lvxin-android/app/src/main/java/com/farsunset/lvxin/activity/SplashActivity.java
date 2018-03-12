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
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.network.CloudImageLoaderFactory;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.BackgroundThreadHandler;

public class SplashActivity extends BaseActivity {
    private Button loginButton;
    private User user;

    @Override
    public void initComponents() {
        setStatusBarColor(0x40000000);
        setWindowFullscreen();
        loginButton = (Button) findViewById(R.id.login_btn);
        setDisplayHomeAsUpEnabled(false);
        user = Global.getCurrentUser();
        loginButton.setOnClickListener(SplashActivity.this);
        BackgroundThreadHandler.postUIThreadDelayed(new Runnable() {
            @Override
            public void run() {
                if (user == null) {
                    loginButton.setVisibility(View.VISIBLE);
                    loginButton.setOnClickListener(SplashActivity.this);
                    loginButton.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, android.R.anim.fade_in));
                } else {
                    startActivity(new Intent(SplashActivity.this, user.password == null ? LoginActivity.class : HomeActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }
        }, 800);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }

    @Override
    public boolean getSwipeBackEnable(){
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        intent.setAction(SplashActivity.class.getName());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        CIMPushManager.destroy(this);
        CloudImageLoaderFactory.get().clearMemory();
        super.onBackPressed();
    }
}
