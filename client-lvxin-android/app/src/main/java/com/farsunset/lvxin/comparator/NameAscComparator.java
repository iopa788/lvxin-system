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

import com.farsunset.lvxin.model.Friend;

import java.io.Serializable;
import java.util.Comparator;

public class NameAscComparator implements Comparator<Friend>, Serializable {

    @Override
    public int compare(Friend o1, Friend o2) {
        if ('#' == (o2.fristChar)) {
            return -1;
        }
        if ('#' == (o1.fristChar)) {
            return 1;
        }

        if ('☆' == (o2.fristChar)) {
            return 1;
        }
        if ('☆' == (o1.fristChar)) {
            return -1;
        }

        return Character.compare(o1.fristChar,o2.fristChar);
    }
}
