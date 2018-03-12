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
package com.farsunset.lvxin.activity.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.SkinManager;
import com.farsunset.lvxin.dialog.CustomProgressDialog;
import com.farsunset.lvxin.pro.R;
import com.jude.swipbackhelper.SwipeBackHelper;

public abstract class BaseActivity extends AppCompatActivity implements OnClickListener {
    protected Toolbar toolbar;
    protected TextView toolbarTitle;
    private CustomProgressDialog progressDialog;
    private BaseInnerReceiver mBaseInnerReceiver;
    private boolean mDestroyed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(SkinManager.getSkinTheme());
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());
        toolbar = (Toolbar) findViewById(R.id.TOOLBAR);
        if (toolbar != null) {
            toolbarTitle = (TextView) toolbar.findViewById(R.id.title);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            setDisplayHomeAsUpEnabled(true);
        }
        if (getToolbarTitle() > 0) {
            setTitle(getString(getToolbarTitle()));
        }

        mBaseInnerReceiver = new BaseInnerReceiver();
        LvxinApplication.registerLocalReceiver(mBaseInnerReceiver, mBaseInnerReceiver.getIntentFilter());
        initComponents();

        initSwipeBackHelper();
    }
    public boolean getSwipeBackEnable(){
        return true;
    }
    private void initSwipeBackHelper(){
        if (!getSwipeBackEnable())
        {
            return;
        }
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)//获取当前页面
                .setSwipeEdgePercent(0.1f)//可滑动的范围。百分比。0.2表示为左边20%的屏幕
                .setSwipeSensitivity(0.5f)//对横向滑动手势的敏感程度。0为迟钝 1为敏感
                .setScrimColor(Color.TRANSPARENT)//底层阴影颜色
                .setClosePercent(0.8f)//触发关闭Activity百分比
                .setSwipeRelateEnable(true)//是否与下一级activity联动(微信效果)。默认关
                .setSwipeRelateOffset((int) (Resources.getSystem().getDisplayMetrics().widthPixels * 0.2))//activity联动时的偏移量。默认500px。
                .setDisallowInterceptTouchEvent(false);//不抢占事件，默认关（事件将先由子View处理再由滑动关闭处理）

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (getSwipeBackEnable())
        {
            SwipeBackHelper.onPostCreate(this);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setBackIcon(int resId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(resId);
        }
    }

    public void setTitle(String t) {
        if (toolbarTitle != null) {
            toolbarTitle.setText(t);
        }
    }

    @Override
    public void setTitle(int t) {
        setTitle(getString(t));
    }

    public void setDisplayHomeAsUpEnabled(boolean f) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(f);
        }
    }


    public void clearTitleDrawableRight() {
        ((TextView) toolbar.findViewById(R.id.title)).setCompoundDrawables(null, null, null, null);

    }

    public void setTitleDrawableRight(@DrawableRes int resId) {
        Drawable image = ContextCompat.getDrawable(this, resId);
        image.setBounds(0, 0, (int) (Resources.getSystem().getDisplayMetrics().density * 20), (int) (Resources.getSystem().getDisplayMetrics().density * 20));
        ((TextView) toolbar.findViewById(R.id.title)).setCompoundDrawables(null, null, image, null);
    }

    public void setLogo(int rid) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setLogo(rid);
        }
    }

    public abstract void initComponents();

    public abstract int getContentLayout();

    public abstract int getToolbarTitle();

    public void showProgressDialog(String content, boolean cancancelable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setMessage(content);
            progressDialog.setCancelable(cancancelable);
            return;
        }
        progressDialog = new CustomProgressDialog(this);
        progressDialog.setMessage(content);
        progressDialog.setCancelable(cancancelable);
        progressDialog.show();
    }

    public void showProgressDialog(String content) {
        showProgressDialog(content, false);
    }

    public void hideProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    public void showToastView(String text) {
        View toastView = LayoutInflater.from(this).inflate(R.layout.layout_toast_view, null);
        ((TextView) toastView.findViewById(R.id.text)).setText(text);
        Toast toast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
        toast.setView(toastView);
        toast.show();
    }

    public void showToastView(@StringRes int id) {
        showToastView(getString(id));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getSwipeBackEnable())
        {
            SwipeBackHelper.onDestroy(this);
        }
        mDestroyed = true;
        LvxinApplication.unregisterLocalReceiver(mBaseInnerReceiver);
        hideProgressDialog();
    }

    @Override
    public boolean isDestroyed() {
        return mDestroyed;
    }

    @Override
    public void finish() {
        super.finish();
        Global.saveAppInBackground(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Global.saveAppInBackground(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Global.saveAppInBackground(false);
        Global.saveTopActivityClassName(this.getClass());
    }

    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
    public void setWindowFullscreen(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public void setTheme(int resid) {
       super.setTheme(resid);
       onThemeChanged();
    }

    protected void onThemeChanged() {
        int[] attrsArray = {android.R.attr.colorPrimary};
        TypedArray typedArray = obtainStyledAttributes(attrsArray);
        int accentColor = typedArray.getColor(0, 0);
        setStatusBarColor(accentColor);
        if (toolbar != null) {
            toolbar.setBackgroundColor(accentColor);
        }
        typedArray.recycle();
    }


    public class BaseInnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Constant.Action.ACTION_FINISH_ACTIVITY)) {
                finish();
            } else if (intent.getAction().equals(Constant.Action.ACTION_THEME_CHANGED)) {

                setTheme(SkinManager.getSkinTheme());
                onThemeChanged();
            }

        }

        public IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_FINISH_ACTIVITY);
            filter.addAction(Constant.Action.ACTION_THEME_CHANGED);
            return filter;
        }
    }
}
