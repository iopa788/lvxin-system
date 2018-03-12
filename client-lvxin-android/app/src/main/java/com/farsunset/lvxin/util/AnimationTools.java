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
package com.farsunset.lvxin.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.farsunset.lvxin.app.LvxinApplication;


public class AnimationTools {
    private AnimationTools() {
    }

    public static void start(int aid, final View view, final AnimationListener listener) {

        Animation a = android.view.animation.AnimationUtils.loadAnimation(LvxinApplication.getInstance(), aid);
        start(a, view, listener);
    }

    public static void start(Animation a, final View view, final AnimationListener listener) {

        a.setAnimationListener(listener);
        view.startAnimation(a);
    }

    public static void start(View view, int aid) {
        Animation a = android.view.animation.AnimationUtils.loadAnimation(LvxinApplication.getInstance(), aid);
        view.startAnimation(a);
    }
}
