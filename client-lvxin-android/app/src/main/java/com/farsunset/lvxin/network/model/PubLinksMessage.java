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
package com.farsunset.lvxin.network.model;

import java.io.Serializable;
import java.util.List;

public class PubLinksMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    public String title;//文字标题
    public String link;//链接地址
    public String image;//图片，只有第一行才显示横向横幅
    public List<PubLinksMessage> items; //更多链接

    public boolean hasMore() {
        return items != null && !items.isEmpty();
    }

    public PubLinksMessage getSubLink(int i) {
        return items.get(i);
    }
}
