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
package com.farsunset.lvxin.comparator;

import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.util.AppTools;

import java.io.Serializable;
import java.util.Comparator;

public class DistanceAscComparator implements Comparator<Friend>, Serializable {


    User self;

    public DistanceAscComparator(User self)

    {
        this.self = self;
    }

    @Override
    public int compare(Friend arg0, Friend arg1) {
        double distance0 = 0;
        double distance1 = 0;

        try {
            distance0 = AppTools.getDistance(self.longitude, self.latitude, arg0.longitude, arg0.latitude);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            distance1 = AppTools.getDistance(self.longitude, self.latitude, arg1.longitude, arg1.latitude);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (int) ((distance0 - distance1) * 100);
    }

}
