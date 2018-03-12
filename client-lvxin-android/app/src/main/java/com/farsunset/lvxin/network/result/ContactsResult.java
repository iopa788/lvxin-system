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
package com.farsunset.lvxin.network.result;

import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.PublicAccount;

import java.util.ArrayList;


public class ContactsResult extends BaseResult {
    public ArrayList<Friend> friendList;
    public ArrayList<Group> groupList;
    public ArrayList<PublicAccount> pubAccountList;

    public boolean isNotEmpty(Class cls) {
        if (cls == Friend.class) {
            return friendList != null && !friendList.isEmpty();
        }
        if (cls == Group.class) {
            return groupList != null && !groupList.isEmpty();
        }
        if (cls == PublicAccount.class) {
            return pubAccountList != null && !pubAccountList.isEmpty();
        }

        return true;
    }
}
