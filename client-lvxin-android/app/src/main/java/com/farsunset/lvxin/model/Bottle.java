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
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.pro.R;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Objects;

//漂流瓶
@DatabaseTable(tableName = "t_lvxin_bottle")
public class Bottle extends MessageSource implements Serializable {


    public transient static final int TYPE_TXT = 0;
    public transient static final int TYPE_VOICE = 1;

    /**
     *
     */
    private transient static final long serialVersionUID = 1L;
    private transient static final String[] MESSAGE_ACTION = new String[]{Constant.MessageAction.ACTION_700, Constant.MessageAction.ACTION_0};
    @DatabaseField(id = true)
    public String gid;
    @DatabaseField
    public String sender;
    @DatabaseField
    public String receiver;
    @DatabaseField
    public int type;
    @DatabaseField
    public String content;
    @DatabaseField
    public String region;
    @DatabaseField
    public int status;
    @DatabaseField
    public String length;
    @DatabaseField
    public long timestamp;

    @Override
    public String getSourceType() {

        return SOURCE_BOTTLE;
    }

    @Override
    public String getWebIcon() {
        return null;
    }

    @Override
    public String getTitle() {
        return LvxinApplication.getInstance().getString(R.string.label_function_bottle);
    }

    @Override
    public String getName() {
        return region == null ? "未知区域" : region;
    }

    @Override
    public String getId() {
        if (Objects.equals(Global.getCurrentAccount(),sender)) {
            return receiver;
        }
        return sender;
    }

    @Override
    public int getDefaultIconRID() {
        return R.drawable.icon_secret_header;
    }

    @Override
    public String[] getMessageAction() {

        return Bottle.MESSAGE_ACTION;
    }


}
