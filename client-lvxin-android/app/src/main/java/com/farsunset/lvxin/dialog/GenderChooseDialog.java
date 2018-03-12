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
import android.view.View;
import android.view.Window;

import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.pro.R;

public class GenderChooseDialog extends Dialog implements View.OnClickListener {

    private OnChooseListener operationListener;

    public GenderChooseDialog(Context context, OnChooseListener listener) {

        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_gender_selector);
        findViewById(R.id.man).setOnClickListener(this);
        findViewById(R.id.female).setOnClickListener(this);
        this.setCanceledOnTouchOutside(true);
        operationListener = listener;
    }

    public void show(String gender) {
        findViewById(R.id.man_mark).setVisibility(View.GONE);
        findViewById(R.id.female_mark).setVisibility(View.GONE);
        if (User.GENDER_MAN.equals(gender)) {
            findViewById(R.id.man_mark).setVisibility(View.VISIBLE);
        }
        if (User.GENDER_FEMALE.equals(gender)) {
            findViewById(R.id.female_mark).setVisibility(View.VISIBLE);
        }
        super.show();
    }

    @Override
    public void onClick(View v) {
        findViewById(R.id.man_mark).setVisibility(View.GONE);
        findViewById(R.id.female_mark).setVisibility(View.GONE);
        if (v.getId() == R.id.man) {
            operationListener.onGenderChecked(User.GENDER_MAN);
            findViewById(R.id.man_mark).setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.female) {
            operationListener.onGenderChecked(User.GENDER_FEMALE);
            findViewById(R.id.female_mark).setVisibility(View.VISIBLE);
        }
        dismiss();
    }

    public interface OnChooseListener {
        void onGenderChecked(String gender);
    }
}
