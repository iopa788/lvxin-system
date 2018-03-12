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
package com.farsunset.lvxin.activity.chat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.activity.contact.MessageForwardActivity;
import com.farsunset.lvxin.activity.trend.MomentPublishActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.dialog.TextSizeSettingWindow;
import com.farsunset.lvxin.dialog.TextSizeSettingWindow.OnSizeSelectedListener;
import com.farsunset.lvxin.network.model.PubLinkMessage;
import com.farsunset.lvxin.pro.R;

import java.net.MalformedURLException;
import java.net.URL;

public class MMWebViewActivity extends BaseActivity implements OnSizeSelectedListener {
    private WebView webview;
    private WebSettings settings;
    private String mCurrentUrl;
    WebViewClient client = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            mCurrentUrl = url;
        }

    };
    private ProgressBar loadingProgressBar;
    Handler progressHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            Animation animation = AnimationUtils.loadAnimation(MMWebViewActivity.this, R.anim.disappear);
            animation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    loadingProgressBar.setVisibility(View.GONE);
                }
            });
            loadingProgressBar.startAnimation(animation);
        }
    };
    private TextSizeSettingWindow textSizeWindow;
    private View backgroundView;
    WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            loadingProgressBar.setProgress(newProgress);
            loadingProgressBar.setVisibility(View.VISIBLE);
            if (newProgress == 100) {
                progressHandler.sendEmptyMessage(0);
            }
            if (newProgress > 50) {
                backgroundView.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            setTitle(title);
        }
    };

    @Override
    public boolean getSwipeBackEnable(){
        return false;
    }

    @Override
    public void initComponents() {

        setBackIcon(R.drawable.abc_ic_clear_material);
        mCurrentUrl = getIntent().getData().toString();
        webview = (WebView) findViewById(R.id.webview);
        TextView provider = (TextView) findViewById(R.id.provider);
        settings = webview.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        webview.setWebViewClient(client);
        webview.setWebChromeClient(chromeClient);
        webview.loadUrl(mCurrentUrl);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        textSizeWindow = new TextSizeSettingWindow(this, this);
        backgroundView = findViewById(R.id.background);
        try {
            provider.setText(getString(R.string.label_web_provider, new URL(mCurrentUrl).getHost()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_webview;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }

    @Override
    public void onClick(View v) {


    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_copy_url:
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setPrimaryClip(ClipData.newPlainText(null, mCurrentUrl));
                Snackbar.make(webview, R.string.tip_copy_successed, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.menu_open_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mCurrentUrl));
                startActivity(intent);
                break;
            case R.id.menu_share_moment:
                PubLinkMessage link = new PubLinkMessage();
                link.title = webview.getTitle();
                link.link = webview.getUrl();
                Intent intent1 = new Intent(this, MomentPublishActivity.class);
                intent1.putExtra(PubLinkMessage.NAME, link);
                startActivity(intent1);
                break;

            case R.id.menu_share_friend:
                Intent intent2 = new Intent(this, MessageForwardActivity.class);
                com.farsunset.lvxin.model.Message message = new com.farsunset.lvxin.model.Message();
                message.sender = Global.getCurrentAccount();
                message.format = Constant.MessageFormat.FORMAT_TEXT;
                message.content = webview.getTitle() + "\n" + webview.getUrl();
                intent2.putExtra(com.farsunset.lvxin.model.Message.NAME, message);
                startActivity(intent2);
                break;
            case R.id.menu_text_size:
                textSizeWindow.showAtLocation(webview, Gravity.BOTTOM, 0, 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSizeSelected(int size) {
        settings.setTextZoom(size);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webview.destroy();
        webview = null;
        chromeClient = null;
        client = null;
        textSizeWindow = null;
        progressHandler.removeCallbacksAndMessages(null);
    }

}
