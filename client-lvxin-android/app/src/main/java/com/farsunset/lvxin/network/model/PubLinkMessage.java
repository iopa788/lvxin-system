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

public class PubLinkMessage implements Serializable {
    public transient static final String NAME = PubLinkMessage.class.getSimpleName();
    private static final long serialVersionUID = 1L;
    public String title;//文字标题
    public String content;//文字内容
    public String link;//链接地址
    public String image;//图片

    @Override
    public String toString() {
        return title == null ? "" : title + "\n" + link;
    }
}
