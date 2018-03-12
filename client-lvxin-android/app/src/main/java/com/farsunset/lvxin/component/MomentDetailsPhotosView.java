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
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.pro.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class MomentDetailsPhotosView extends MomentDetailsView {
    private int cellWidth;
    private int spacing;
    private GridLayout gridLayout;

    public MomentDetailsPhotosView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        spacing = getResources().getDimensionPixelOffset(R.dimen.sns_photos_spacing);
    }

    private void displayImageOnMeasured() {
        List<SNSImage> images = new Gson().fromJson(moment.thumbnail, new TypeToken<List<SNSImage>>() {}.getType());
        for (int i = 0; i < images.size(); i++) {

            SNSImageView itemView = new SNSImageView(getContext());
            itemView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            itemView.display(moment,images.get(i));


            gridLayout.addView(itemView, cellWidth, cellWidth);


            boolean isRowFirst = i % gridLayout.getColumnCount() != 0;
            ((GridLayout.LayoutParams) itemView.getLayoutParams()).leftMargin = isRowFirst ? spacing : 0;
            boolean isFirstRow = i < gridLayout.getColumnCount();
            ((GridLayout.LayoutParams) itemView.getLayoutParams()).topMargin = isFirstRow ? 0 : spacing;


        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (cellWidth == 0 && gridLayout.getMeasuredWidth() > 0) {
            cellWidth = (gridLayout.getMeasuredWidth() - (gridLayout.getColumnCount() - 1) * spacing) / gridLayout.getColumnCount();
            displayImageOnMeasured();
        }
    }

}
