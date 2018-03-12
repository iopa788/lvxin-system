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

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.pro.R;

import java.io.Serializable;


public class SystemMessage extends MessageSource implements Serializable {

    public static final long serialVersionUID = 4733464888738356502L;

    public final static String[] MESSAGE_ACTION_ARRAY = new String[]{Constant.MessageAction.ACTION_2,
            Constant.MessageAction.ACTION_102,
            Constant.MessageAction.ACTION_103,
            Constant.MessageAction.ACTION_104,
            Constant.MessageAction.ACTION_105,
            Constant.MessageAction.ACTION_106,
            Constant.MessageAction.ACTION_107,
            Constant.MessageAction.ACTION_112,};


    public final static String RESULT_AGREE = "1";

    public final static String RESULT_REFUSE = "2";

    public final static String RESULT_IGNORE = "3";

    public final static String ID = Constant.SYSTEM;

    public String name;

    String type;

    public SystemMessage(String msgType) {
        type = msgType;
        name = getTypeText(msgType);
    }


    public static String getTypeText(String msgType) {

        if (Constant.MessageAction.ACTION_2.equals(msgType)) {
            return LvxinApplication.getInstance().getString(R.string.common_sysmessage);
        }
        if (Constant.MessageAction.ACTION_102.equals(msgType) || Constant.MessageAction.ACTION_105.equals(msgType)) {
            return LvxinApplication.getInstance().getString(R.string.tip_title_groupmessage);
        }

        return LvxinApplication.getInstance().getString(R.string.common_sysmessage);

    }

    @Override
    public int getDefaultIconRID() {

    	/*if(Constant.MessageAction.ACTION_2.equals(type))
        {
    		return R.drawable.icon_recent_sysmsg;
    	}
    	if(Constant.MessageAction.ACTION_100.equals(type))
    	{
    		return R.drawable.icon_addfriend;
    	}
    	if(Constant.MessageAction.ACTION_103.equals(type))
    	{
    		return R.drawable.icon_joinfroup;
    	}
    	*/
        return R.drawable.icon_system_notify;
    }

    @Override
    public int getNotifyIcon() {
        return R.drawable.icon_system_notify;
    }

    @Override
    public String getTitle() {

        return name;
    }

    @Override
    public int getThemeColor() {
        return R.color.theme_orange;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getWebIcon() {
        return null;
    }

    @Override
    public String getSourceType() {
        return SOURCE_SYSTEM;
    }


    @Override
    public String getName() {

        return LvxinApplication.getInstance().getString(R.string.common_system);
    }

    @Override
    public String[] getMessageAction() {
        return MESSAGE_ACTION_ARRAY;
    }
}
