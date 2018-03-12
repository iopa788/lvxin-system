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

public class OSSImage implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String image;//原图key
    public String thumbnail;//缩略图key

    @Override
    public boolean equals(Object o) {
        if (o instanceof OSSImage) {
            OSSImage bean = (OSSImage) o;
            return bean.image.equals(this.image);
        }
        return false;
    }


    @Override
    public int hashCode() {
        return OSSImage.class.hashCode();
    }
}
