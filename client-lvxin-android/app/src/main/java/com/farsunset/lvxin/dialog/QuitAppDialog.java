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
package com.farsunset.lvxin.dialog;


import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.Window;

import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.pro.R;

public class QuitAppDialog extends BottomSheetDialog implements View.OnClickListener, OnDialogButtonClickListener {
    private CustomDialog dialog;

    public QuitAppDialog(Activity activity) {
        super(activity);
        setOwnerActivity(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_quit_app_dialog);
        findViewById(R.id.menu_logout).setOnClickListener(this);
        findViewById(R.id.menu_killapp).setOnClickListener(this);
        dialog = new CustomDialog(activity);
        dialog.setOwnerActivity(activity);
        dialog.setOnDialogButtonClickListener(this);
    }

    public void doLogout() {
        Global.removePassword();
        CIMPushManager.stop(LvxinApplication.getInstance());
        HttpRequestLauncher.executeQuietly(new HttpRequestBody(URLConstant.USER_LOGOUT_URL, BaseResult.class));
        LvxinApplication.getInstance().restartSelf();

        NotificationManager notificationMgr =  (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationMgr.cancelAll();
    }

    public void doKillApp() {
        CIMPushManager.destroy(LvxinApplication.getInstance());
        getOwnerActivity().finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    @Override
    public void onClick(View v) {
        this.dismiss();
       if (v.getId() == R.id.menu_logout)
       {
           dialog.setTitle(R.string.label_logout);
           dialog.setMessage(R.string.label_logout_hint);
           dialog.setTag(R.id.menu_logout);
           dialog.show();
       }
        if (v.getId() == R.id.menu_killapp)
        {
            dialog.setTitle(R.string.label_killapp);
            dialog.setMessage(R.string.label_killapp_hint);
            dialog.setTag(R.id.menu_killapp);
            dialog.show();
        }
    }
    @Override
    public void onLeftButtonClicked() {
        dialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        dialog.dismiss();
        if (dialog.getTag().equals(R.id.menu_logout)) {
            doLogout();
        }
        if (dialog.getTag().equals(R.id.menu_killapp)) {
            doKillApp();
        }
    }
}
