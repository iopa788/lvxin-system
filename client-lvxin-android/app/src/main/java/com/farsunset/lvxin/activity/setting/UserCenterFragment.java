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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.farsunset.lvxin.activity.HomeActivity;
import com.farsunset.lvxin.activity.trend.SelfMomentActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.BannerView;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.dialog.QuitAppDialog;
import com.farsunset.lvxin.listener.CloudImageLoadListener;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.network.CloudImageLoaderFactory;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.UpgradeManger;
import com.farsunset.lvxin.network.result.AppVersionResult;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.pro.BuildConfig;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.BitmapUtils;
import com.farsunset.lvxin.util.FileURLBuilder;

public class UserCenterFragment extends Fragment implements OnClickListener, HttpRequestListener, CloudImageLoadListener {

    private User user;
    private WebImageView icon;
    private UpgradeManger upgradeManger;
    private View newVersionMark;
    private BannerView banner;
    private QuitAppDialog quitAppDialog;
    private LogoChangedReceiver logoChangedReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = Global.getCurrentUser();
        upgradeManger = new UpgradeManger(getActivity());

        quitAppDialog = new QuitAppDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_user_center, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.icon).setOnClickListener(this);
        view.findViewById(R.id.menu_about).setOnClickListener(this);
        view.findViewById(R.id.menu_album).setOnClickListener(this);
        view.findViewById(R.id.menu_server).setOnClickListener(this);
        view.findViewById(R.id.menu_quitapp).setOnClickListener(this);
        view.findViewById(R.id.menu_message).setOnClickListener(this);
        view.findViewById(R.id.menu_upgrade).setOnClickListener(this);
        view.findViewById(R.id.menu_profile).setOnClickListener(this);
        view.findViewById(R.id.menu_theme).setOnClickListener(this);

        banner =  (BannerView) view.findViewById(R.id.banner);
        icon = (WebImageView) view.findViewById(R.id.icon);
        TextView name = (TextView) view.findViewById(R.id.username);
        name.setText(user.name);
        newVersionMark = view.findViewById(R.id.newVersionMark);

        CloudImageLoaderFactory.get().downloadOnly(FileURLBuilder.getUserIconUrl(user.account), this);
        icon.load(FileURLBuilder.getUserIconUrl(user.account), R.drawable.icon_def_head, 999);

        performCheckAppNewVersion();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        logoChangedReceiver = new LogoChangedReceiver();
        LvxinApplication.registerLocalReceiver(logoChangedReceiver, logoChangedReceiver.getIntentFilter());
    }
    @Override
    public void onDetach() {
        super.onDetach();
        LvxinApplication.unregisterLocalReceiver(logoChangedReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.icon:
                startActivity(new Intent(this.getActivity(), ProfileEditActivity.class));
                break;
            case R.id.menu_profile:
                startActivity(new Intent(this.getActivity(), ProfileEditActivity.class));
                break;
            case R.id.menu_album:
                startActivity(new Intent(this.getActivity(), SelfMomentActivity.class));
                break;

            case R.id.menu_server:
                startActivity(new Intent(this.getActivity(), ServerSettingActivity.class));
                break;
            case R.id.menu_message:
                startActivity(new Intent(this.getActivity(), MessageSettingActivity.class));
                break;
            case R.id.menu_theme:
                startActivity(new Intent(this.getActivity(), ThemeSettingActivity.class));
                break;
            case R.id.menu_about:
                startActivity(new Intent(this.getActivity(), AboutActivity.class));
                break;

            case R.id.menu_quitapp:
                quitAppDialog.show();
                break;
            case R.id.menu_upgrade:
                upgradeManger.excute();
                hideNewVersionMind();
                break;

        }
    }


    private void performCheckAppNewVersion() {

        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.CHECK_NEW_VERSION_URL, AppVersionResult.class);
        requestBody.addParameter("domain", "lvxin_android");
        requestBody.addParameter("versionCode", String.valueOf(BuildConfig.VERSION_CODE));
        HttpRequestLauncher.execute(requestBody, this);
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {
        if (result.isSuccess()) {
            showNewVersionMind();
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {
    }

    public void showNewVersionMind() {
        View tabView = ((HomeActivity) getActivity()).getSettingTab();
        tabView.findViewById(R.id.reddot).setVisibility(View.VISIBLE);
        newVersionMark.setVisibility(View.VISIBLE);
    }

    public void hideNewVersionMind() {
        View tabView = ((HomeActivity) getActivity()).getSettingTab();
        tabView.findViewById(R.id.reddot).setVisibility(View.GONE);
        newVersionMark.setVisibility(View.GONE);
    }



    @Override
    public void onImageLoadFailure(Object key) {

    }

    @Override
    public void onImageLoadSucceed(Object key, Bitmap resource) {
        banner.setScaleType(ImageView.ScaleType.CENTER_CROP);
        banner.setImageBitmap(BitmapUtils.doBlur(resource, 10, false));
    }

    public class LogoChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            CloudImageLoaderFactory.get().downloadOnly(FileURLBuilder.getUserIconUrl(user.account), UserCenterFragment.this);
            icon.load(FileURLBuilder.getUserIconUrl(user.account), R.drawable.icon_def_head, 999);
        }

        public IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_LOGO_CHANGED);
            return filter;
        }

    }
}
