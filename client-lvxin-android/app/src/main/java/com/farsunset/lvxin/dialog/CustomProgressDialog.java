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

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.farsunset.lvxin.pro.R;

public class CustomProgressDialog extends Dialog implements DialogInterface.OnDismissListener {
    private ImageView progress;

    public CustomProgressDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_progress_dialog);
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.85);
        getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        setOnDismissListener(this);
        progress = (ImageView) findViewById(R.id.progress);
    }

    public void setMessage(String content) {
        ((TextView) findViewById(R.id.title)).setText(content);
    }

    @Override
    public void show() {
        ((AnimationDrawable) progress.getDrawable()).start();
        super.show();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        ((AnimationDrawable) progress.getDrawable()).stop();
    }
}
