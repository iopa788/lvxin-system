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
package com.farsunset.lvxin.network;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.dialog.CustomProgressDialog;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.network.result.AppVersionResult;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.pro.BuildConfig;
import com.farsunset.lvxin.pro.R;

import java.io.File;

/**
 *
 *
 */
public class UpgradeManger implements HttpRequestListener, OnDialogButtonClickListener {
    private CustomProgressDialog progressDialog;
    private CustomDialog customDialog;
    private Context context;
    private AppVersionResult versionResult;

    public UpgradeManger(Context ctx) {

        context = ctx;
        customDialog = new CustomDialog(ctx);
        customDialog.setButtonsText(context.getString(R.string.common_cancel), context.getString(R.string.common_upgrade));
        customDialog.setTitle(R.string.tip_find_newversion);
        customDialog.setOnDialogButtonClickListener(this);
        progressDialog = new CustomProgressDialog(ctx);

    }


    public void excute() {

        progressDialog.setMessage(context.getString(R.string.tip_loading, context.getString(R.string.common_detection)));
        progressDialog.show();

        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.CHECK_NEW_VERSION_URL, AppVersionResult.class);
        requestBody.addParameter("versionCode", String.valueOf(BuildConfig.VERSION_CODE));
        requestBody.addParameter("domain", "lvxin_android");
        HttpRequestLauncher.execute(requestBody, this);
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {
        progressDialog.dismiss();
        versionResult = (AppVersionResult) result;

        if (versionResult.code.equals(Constant.ReturnCode.CODE_404)) {
            ((BaseActivity) context).showToastView(context.getString(R.string.tip_no_new_version));

        } else {
            SpannableStringBuilder text = new SpannableStringBuilder(context.getString(R.string.label_setting_newversion, versionResult.data.versionName) + "\n\n" + versionResult.data.description);
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#3C568B")), 4, versionResult.data.versionName.length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new StyleSpan(Typeface.BOLD), 4, versionResult.data.versionName.length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            customDialog.setMessage(text);
            customDialog.show();
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        progressDialog.dismiss();
    }


    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        customDialog.dismiss();

        File file = new File(LvxinApplication.DOWNLOAD_DIR, LvxinApplication.getInstance().getPackageName() + "-" + versionResult.data.versionName + ".apk");
        LvxinApplication.getInstance().startDownloadService(versionResult.data.url, file);
    }


}
