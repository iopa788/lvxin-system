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
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class SNSImageView extends WebImageView implements OnClickListener {

    private SNSImage image;

    public SNSImageView(Context context) {
        super(context);
        this.setOnClickListener(this);
    }

    public SNSImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnClickListener(this);
    }


    public void display(Moment target, SNSImage image) {
        this.setTag(R.id.target, target);
        this.image = image;
        load(FileURLBuilder.getFileUrl(image.thumbnail), R.color.def_imageview_color);
    }

    public void displayFitSize(Moment target, SNSImage image) {
        this.setTag(R.id.target, target);
        this.image = image;

        int w = image.ow;
        int h = image.oh;
        int circleSinglePhotoSide = getContext().getResources().getDimensionPixelOffset(R.dimen.circle_single_photo_side);

        if (w < circleSinglePhotoSide && h < circleSinglePhotoSide) {
            this.getLayoutParams().width = w;
            this.getLayoutParams().height = h;
        } else {
            if (w >= h) {
                this.getLayoutParams().width = circleSinglePhotoSide;
                this.getLayoutParams().height = circleSinglePhotoSide * h / w;
            } else {
                this.getLayoutParams().height = circleSinglePhotoSide;
                this.getLayoutParams().width = circleSinglePhotoSide * w / h;
            }
        }
        load(FileURLBuilder.getFileUrl(image.image), R.color.def_imageview_color);
    }

    @Override
    public void onClick(View view) {
        Moment target = (Moment) view.getTag(R.id.target);
        ArrayList<SNSImage> snsImageList = new Gson().fromJson(target.thumbnail, new TypeToken<ArrayList<SNSImage>>() {}.getType());
        snsImageList.remove(image);
        snsImageList.add(0, image);
        if (snsImageList.size() == 1) {
            image.thumbnail = image.image;
            LvxinApplication.getInstance().startPhotoActivity(getContext(), image, this);

        } else {
            LvxinApplication.getInstance().startGalleryPhotoActivity(getContext(), snsImageList, this);
        }

    }
}
