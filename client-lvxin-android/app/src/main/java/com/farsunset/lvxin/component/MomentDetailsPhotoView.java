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

import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.pro.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class MomentDetailsPhotoView extends MomentDetailsView {
    private SNSImageView imageView;

    public MomentDetailsPhotoView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        imageView = (SNSImageView) findViewById(R.id.singleImageView);
    }

    @Override
    public void displayMoment(Moment moment) {
        super.displayMoment(moment);
        List<SNSImage> snsImageList = new Gson().fromJson(moment.thumbnail, new TypeToken<List<SNSImage>>() {}.getType());
        SNSImage snsImage = snsImageList.get(0);
        imageView.displayFitSize(moment, snsImage);
    }


}
