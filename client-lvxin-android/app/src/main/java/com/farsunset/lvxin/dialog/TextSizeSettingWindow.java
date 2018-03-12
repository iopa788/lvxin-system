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
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.farsunset.lvxin.pro.R;

public class TextSizeSettingWindow extends PopupWindow implements OnClickListener, CompoundButton.OnCheckedChangeListener {
    private OnSizeSelectedListener clickListener;
    private RadioButton radioSmall;
    private RadioButton radioNormal;
    private RadioButton radioBig;
    private RadioButton radioLarge;

    public TextSizeSettingWindow(Context context, OnSizeSelectedListener listener) {
        super(context, null);
        clickListener = listener;
        setContentView(LayoutInflater.from(context).inflate(R.layout.layout_text_size_setting, null));
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight((int) (Resources.getSystem().getDisplayMetrics().density * 53));
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        radioSmall = ((RadioButton) getContentView().findViewById(R.id.radioButton1));
        radioNormal = ((RadioButton) getContentView().findViewById(R.id.radioButton2));
        radioBig = ((RadioButton) getContentView().findViewById(R.id.radioButton3));
        radioLarge = ((RadioButton) getContentView().findViewById(R.id.radioButton4));
        radioSmall.setOnCheckedChangeListener(this);
        radioNormal.setOnCheckedChangeListener(this);
        radioBig.setOnCheckedChangeListener(this);
        radioLarge.setOnCheckedChangeListener(this);
        getContentView().findViewById(R.id.item_large).setOnClickListener(this);
        getContentView().findViewById(R.id.item_big).setOnClickListener(this);
        getContentView().findViewById(R.id.item_normal).setOnClickListener(this);
        getContentView().findViewById(R.id.item_small).setOnClickListener(this);
        setAnimationStyle(R.style.bottomPopupWindowAnimation);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked && buttonView.getId() == radioSmall.getId()) {
            radioLarge.setChecked(false);
            radioBig.setChecked(false);
            radioNormal.setChecked(false);
            clickListener.onSizeSelected(80);
        }
        if (isChecked && buttonView.getId() == radioNormal.getId()) {
            radioSmall.setChecked(false);
            radioBig.setChecked(false);
            radioLarge.setChecked(false);
            clickListener.onSizeSelected(100);
        }
        if (isChecked && buttonView.getId() == radioBig.getId()) {
            radioSmall.setChecked(false);
            radioLarge.setChecked(false);
            radioNormal.setChecked(false);
            clickListener.onSizeSelected(120);
        }
        if (isChecked && buttonView.getId() == radioLarge.getId()) {
            clickListener.onSizeSelected(150);
            radioSmall.setChecked(false);
            radioBig.setChecked(false);
            radioNormal.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.item_large) {
            radioSmall.setChecked(false);
            radioBig.setChecked(false);
            radioNormal.setChecked(false);
            radioLarge.setChecked(true);
        }
        if (v.getId() == R.id.item_big) {
            radioSmall.setChecked(false);
            radioBig.setChecked(true);
            radioNormal.setChecked(false);
            radioLarge.setChecked(false);

        }
        if (v.getId() == R.id.item_normal) {
            radioSmall.setChecked(false);
            radioBig.setChecked(false);
            radioNormal.setChecked(true);
            radioLarge.setChecked(false);
        }
        if (v.getId() == R.id.item_small) {
            radioSmall.setChecked(true);
            radioBig.setChecked(false);
            radioNormal.setChecked(false);
            radioLarge.setChecked(false);
        }
    }


    public interface OnSizeSelectedListener {
        void onSizeSelected(int size);
    }


}
