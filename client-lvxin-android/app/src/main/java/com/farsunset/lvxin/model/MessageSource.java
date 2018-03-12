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
package com.farsunset.lvxin.model;

import android.support.v4.app.NotificationCompat;

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.pro.R;

import java.io.Serializable;

public abstract class MessageSource implements Serializable {

    public static final String NAME = MessageSource.class.getSimpleName();
    public static final String SOURCE_USER = Constant.MessageAction.ACTION_0;
    public static final String SOURCE_GROUP = Constant.MessageAction.ACTION_1;
    public static final String SOURCE_BOTTLE = Constant.MessageAction.ACTION_700;
    public static final String SOURCE_SYSTEM = Constant.MessageAction.ACTION_2;
    public static final String SOURCE_PUBACCOUNT = Constant.MessageAction.ACTION_200;
    private static final long serialVersionUID = 1L;

    public abstract String getSourceType();

    public abstract String getWebIcon();

    public abstract String getTitle();

    public abstract String getName();



    //消息列表展示的消息类型
    public abstract String[] getMessageAction();

    public abstract String getId();

    public abstract int getDefaultIconRID();

    public int getNotifyIcon() {
        return getDefaultIconRID();
    }


    public int getTitleColor() {
        return android.R.color.black;
    }

    public int getThemeColor() {
        return R.color.theme_orange;
    }

    public  int getNotificationPriority(){
        return NotificationCompat.PRIORITY_DEFAULT;
    }

    public  String getIdentityId(){

        return  getSourceType() + "#" +getId();
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MessageSource) {
            MessageSource target = (MessageSource) o;
            return getSourceType().equals(target.getSourceType()) && target.getId().equals(getId());
        }
        return false;
    }


}
