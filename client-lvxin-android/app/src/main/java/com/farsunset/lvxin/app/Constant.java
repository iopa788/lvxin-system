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
package com.farsunset.lvxin.app;


import android.os.Environment;

public interface Constant {

    int CIM_SERVER_PORT = 23456;

    String SERVER_URL = "http://lvxin.farsunset.com";

    long MOMENT_PAGE_SIZE = 10;

    long MESSAGE_PAGE_SIZE = 20;

    String SYSTEM = "system";

    int EMOTION_FACE_SIZE = 24;

    String CHAT_OTHRES_ID = "othersId";

    String CHAT_OTHRES_NAME = "othersName";

    String NEED_RECEIPT = "NEED_RECEIPT";

    int RESULT_ZOOM = 11;
    int RESULT_CAMERA = 1920;

    int MAX_IMAGE_PIXEL = 1080;

    int THUMBNAIL_MAX_IMAGE_PIXEL = 240;

    //对话页面消息时间显示间隔
    int CHATTING_TIME_SPACE =  22 * 60 * 1000;

    String SYSTEM_PHOTO_DIR = Environment.getExternalStorageDirectory() + "/DCIM/Camera";


    interface ReturnCode {
        //帐号已经存在
        String CODE_101 = "101";
        String CODE_403 = "403";
        String CODE_401 = "401";
        String CODE_404 = "404";

    }

    interface MessageAction {

        //用户之间的普通消息
        String ACTION_0 = "0";

        String ACTION_1 = "1";

        //系统向用户发送的普通消息
        String ACTION_2 = "2";

        //群里用户发送的  消息
        String ACTION_3 = "3";

        /**
         * ********************************************1开头统一为聊天消息**********************************************************
         */

        //系统定制消息---进群请求
        String ACTION_102 = "102";

        //系统定制消息---同意进群请求
        String ACTION_103 = "103";

        //系统定制消息---群解散消息
        String ACTION_104 = "104";

        //系统定制消息---邀请入群请求
        String ACTION_105 = "105";

        //系统定制消息---同意邀请入群请求
        String ACTION_106 = "106";

        //系统定制消息---被剔除群
        String ACTION_107 = "107";

        //系统定制消息---消息被阅读
        String ACTION_108 = "108";

        //系统定制消息---好友替换了头像
        String ACTION_110 = "110";

        //系统定制消息---好友修改了名称或者签名
        String ACTION_111 = "111";

        //系统定制消息---用户退出了群
        String ACTION_112 = "112";

        //系统定制消息---用户加入了群
        String ACTION_113 = "113";

        //系统定制消息---群信息被修改
        String ACTION_114 = "114";


        /**
         * ********************************************2开头统一为公众号消息**********************************************************
         */
        //系统定制消息---用户向公众号发消息
        String ACTION_200 = "200";

        //系统定制消息---公众号向用户回复的消息
        String ACTION_201 = "201";

        //系统定制消息---公众号向用户群发消息
        String ACTION_202 = "202";

        //系统定制消息---公众号信息更新
        String ACTION_203 = "203";

        //系统定制消息---公众号菜单信息更新
        String ACTION_204 = "204";

        //系统定制消息---公众号LOGO更新
        String ACTION_205 = "205";


        /**
         * ********************************************4开头统一为系统控制消息**********************************************************
         */
        //系统定制消息---强制下线消息
        String ACTION_444 = "444";

        //系统定制消息---聊天背景图片更新
        String ACTION_400 = "400";

        //系统定制消息---我的页面背景图片更新
        String ACTION_401 = "401";

        //系统定制消息---用户详情页背景图片更新
        String ACTION_402 = "402";
        /**
         * ********************************************7开头统一为漂流瓶消息**********************************************************
         */
        //系统定制消息---漂流瓶消息
        String ACTION_700 = "700";

        //系统定制消息---删除漂流瓶
        String ACTION_701 = "701";

        /**
         * ********************************************8开头统一为圈子动态消息**********************************************************
         */
        //系统定制消息---好友新动态消息
        String ACTION_800 = "800";

        //系统定制消息---好友新动态评论消息
        String ACTION_801 = "801";

        //系统定制消息---好友新动态评论回复评论消息
        String ACTION_802 = "802";

        //系统定制消息---好友删除新动态
        String ACTION_803 = "803";

        //系统定制消息---好友删除评论或者取消点赞
        String ACTION_804 = "804";

        /**
         * ********************************************9开头统一为动作消息**********************************************************
         */
        //系统定制消息---好友下线消息
        String ACTION_900 = "900";

        //系统定制消息---好友上线消息
        String ACTION_901 = "901";

        //系统定制消息---更新用户数据
        String ACTION_998 = "998";

        //系统定制消息---强制下线消息
        String ACTION_999 = "999";

    }


    interface CIMRequestKey {

        //用户修改了名称或签名，向服务器发请求，通知其他好友及时更新
        String CLIENT_MODIFY_LOGO = "client_modify_logo";

        //用户修改了头像，向服务器发请求推送其他好友及时更新
        String CLIENT_MODIFY_PROFILE = "client_modify_profile";
    }

    interface MessageFormat {

        //文字
        String FORMAT_TEXT = "0";

        //图片
        String FORMAT_IMAGE = "1";

        //语音
        String FORMAT_VOICE = "2";


        //文件
        String FORMAT_FILE = "3";

        //地图
        String FORMAT_MAP = "4";


        //链接
        String FORMAT_LINK = "5";

        //多条链接
        String FORMAT_LINKLIST = "6";

        //文字面板
        String FORMAT_TEXTPANEL = "7";

        //视频
        String FORMAT_VIDEO = "8";
    }

    interface MessageStatus {

        //正在发送
        String STATUS_SENDING = "-2";

        //还未发送
        String STATUS_NO_SEND = "-1";

        //发送失败
        String STATUS_SEND_FAILURE = "-3";

        //延迟发送
        String STATUS_DELAY_SEND = "-4";

        String STATUS_OTHERS_READ = "9";//别人已经阅读


        //消息已经发送
        String STATUS_SEND = "1";
    }

    interface Action {
        String ACTION_DELETE_MOMENT = "com.farsunset.lvxin.DELETE_MOMENT";
        String ACTION_REFRESH_MOMENT = "com.farsunset.lvxin.REFRESH_MOMENT";

        String ACTION_WINDOW_REFRESH_MESSAGE = "com.farsunset.lvxin.WINDOW_REFRESH_MESSAGE";
        String ACTION_RECENT_APPEND_CHAT = "com.farsunset.lvxin.RECENT_APPEND_CHAT";
        String ACTION_RECENT_DELETE_CHAT = "com.farsunset.lvxin.RECENT_DELETE_CHAT";
        String ACTION_RECENT_REFRESH_CHAT = "com.farsunset.lvxin.RECENT_REFRESH_CHAT";
        String ACTION_RECENT_REFRESH_LOGO = "com.farsunset.lvxin.RECENT_REFRESH_LOGO";
        String ACTION_UPLOAD_PROGRESS = "com.farsunset.lvxin.UPLOAD_PROGRESS";
        String ACTION_RELOAD_CONTACTS = "com.farsunset.lvxin.RELOAD_CONTACTS";
        String ACTION_LOGO_CHANGED = "com.farsunset.lvxin.ACTION_LOGO_CHANGED";

        String ACTION_FINISH_ACTIVITY = "com.farsunset.lvxin.ACTION_FINISH_ACTIVITY";
        String ACTION_PUB_MENU_INVOKED = "com.farsunset.lvxin.ACTION_PUB_MENU_INVOKED";
        String ACTION_THEME_CHANGED = "com.farsunset.lvxin.ACTION_THEME_CHANGED";

    }
}
