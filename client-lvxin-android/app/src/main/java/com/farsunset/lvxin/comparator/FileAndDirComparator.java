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

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

public class FileAndDirComparator implements Comparator<File>, Serializable {
    @Override
    public int compare(File f1, File f2) {

        if ((f1.isDirectory() && f2.isDirectory()) || (f1.isFile() && f2.isFile())) {
            return f1.getName().compareToIgnoreCase(f2.getName());
        }

        if (f1.isDirectory() && f2.isFile()) {
            return -1;
        }

        if (f1.isFile() && f2.isDirectory()) {
            return 1;
        }
        return f1.compareTo(f2);
    }

}
