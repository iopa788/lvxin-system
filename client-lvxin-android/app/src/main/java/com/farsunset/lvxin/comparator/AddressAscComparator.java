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

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.farsunset.lvxin.util.AppTools;

import java.io.Serializable;
import java.util.Comparator;

public class AddressAscComparator implements Comparator<PoiInfo>, Serializable {


    LatLng current;

    public AddressAscComparator(LatLng current)

    {
        this.current = current;
    }

    @Override
    public int compare(PoiInfo arg0, PoiInfo arg1) {
        if (arg0 == null) {
            return 0;
        }
        if (arg1 == null) {
            return 1;
        }
        double distance0 = AppTools.getDistance(current.longitude, current.latitude, arg0.location.longitude, arg0.location.latitude);
        double distance1 = AppTools.getDistance(current.longitude, current.latitude, arg1.location.longitude, arg1.location.latitude);
        return (int) ((distance0 - distance1));
    }

}
