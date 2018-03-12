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


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.pro.R;

public class CustomDialog extends AppCompatDialog implements View.OnClickListener {

    private OnDialogButtonClickListener onDialogButtonClickListener;

    public CustomDialog(Context context) {

        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_custom_confirm);
        findViewById(R.id.leftButton).setOnClickListener(this);
        findViewById(R.id.rightButton).setOnClickListener(this);
        setCanceledOnTouchOutside(false);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (int) ((Resources.getSystem().getDisplayMetrics().widthPixels) * 0.9f);
        getWindow().setAttributes(p);
    }

    public void setOnDialogButtonClickListener(OnDialogButtonClickListener onDialogButtonClickListener) {
        this.onDialogButtonClickListener = onDialogButtonClickListener;
    }

    public Object getTag() {

        return getWindow().getDecorView().getTag();
    }

    public void setTag(Object tag) {
        getWindow().getDecorView().setTag(tag);
    }

    public void setTitle(String title) {
        ((TextView) findViewById(R.id.title)).setText(title);
    }

    @Override
    public void setTitle(int sid) {
        ((TextView) findViewById(R.id.title)).setText(sid);
    }

    public void setMessage(int sid) {
        ((TextView) findViewById(R.id.message)).setText(sid);
    }

    public void setMessage(CharSequence mesage) {
        ((TextView) findViewById(R.id.message)).setText(mesage);
    }

    public void hideCancelButton() {
        findViewById(R.id.leftButton).setVisibility(View.GONE);
    }

    public void setButtonsText(CharSequence left, CharSequence right) {
        ((TextView) findViewById(R.id.leftButton)).setText(left);
        ((TextView) findViewById(R.id.rightButton)).setText(right);
    }

    @Override
    public void onClick(View view) {
        if (onDialogButtonClickListener==null)
        {
            dismiss();
            return;
        }
        if (view.getId() == R.id.leftButton) {
            onDialogButtonClickListener.onLeftButtonClicked();
        }
        if (view.getId() == R.id.rightButton) {
            onDialogButtonClickListener.onRightButtonClicked();
        }
    }
}
