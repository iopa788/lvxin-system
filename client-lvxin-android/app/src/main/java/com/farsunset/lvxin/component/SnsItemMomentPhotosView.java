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
package com.farsunset.lvxin.component;


import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.listener.OnCommentSelectedListener;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.pro.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SnsItemMomentPhotosView extends SnsItemMomentView {
    private GridLayout gridLayout;
    private int cellWidth;
    private int spacing;

    public SnsItemMomentPhotosView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        gridLayout = (GridLayout) findViewById(R.id.imageGridLayout);
        spacing = getResources().getDimensionPixelOffset(R.dimen.sns_photos_spacing);
        float density = Resources.getSystem().getDisplayMetrics().density;
        cellWidth = (int) ((Resources.getSystem().getDisplayMetrics().widthPixels - 90 * density) / gridLayout.getColumnCount());
    }

    @Override
    public void displayMoment(Moment moment, User self, OnCommentSelectedListener commentSelectedListener) {
        super.displayMoment(moment, self, commentSelectedListener);
        gridLayout.removeAllViews();
        List<SNSImage> images = new Gson().fromJson(moment.thumbnail, new TypeToken<List<SNSImage>>() {}.getType());
        for (int i = 0; i < images.size(); i++) {
            SNSImageView itemView = new SNSImageView(getContext());
            itemView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            itemView.display(moment, images.get(i));

            gridLayout.addView(itemView, cellWidth, cellWidth);

            boolean isRowFirst = i % gridLayout.getColumnCount() != 0;
            ((GridLayout.LayoutParams) itemView.getLayoutParams()).leftMargin = isRowFirst ? spacing : 0;

            boolean isFirstRow = i < gridLayout.getColumnCount();
            ((GridLayout.LayoutParams) itemView.getLayoutParams()).topMargin = isFirstRow ? 0 : spacing;


        }
    }

}
