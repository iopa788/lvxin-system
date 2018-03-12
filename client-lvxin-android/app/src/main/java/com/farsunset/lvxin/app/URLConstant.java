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

public class URLConstant {
    //用户登录
    public static String USER_LOGIN_URL;
    //用户退出
    public static String USER_LOGOUT_URL;

    //用户详情
    public static String USER_DETAILED_URL;
    //公众号详情
    public static String PUBACCOUNT_DETAILED_URL;
    //关注的公众号列表
    public static String PUBACCOUNT_LIST_URL;

    //查看所有公众号列表
    public static String PUBACCOUNT_LOOK_URL;

    //关注公众号
    public static String PUBACCOUNT_SUBSCRIBE_URL;
    //取消关注公众号
    public static String PUBACCOUNT_UNSUBSCRIBE_URL;
    //获取公众号菜单
    public static String PUBACCOUNT_MENU_URL;
    //修改密码
    public static String USER_MODIFYPASSWORD_URL;
    //获取附近在线的人
    public static String USER_NEARBY_URL;
    //群组详情
    public static String GROUP_DETAILED_URL;
    //我的群组列表
    public static String GROUP_LIST_URL;
    //加入用户到群组
    public static String GROUPMEMBER_ADD_URL;
    //退出群组
    public static String GROUPMEMBER_QUIT_URL;
    //将用户移除群组
    public static String GROUPMEMBER_GETOUT_URL;
    //创建群组
    public static String GROUP_CREATE_URL;
    //更新群组
    public static String GROUP_UPDATE_URL;
    //解散群组
    public static String GROUP_DISBAND_URL;
    //群组成员列表
    public static String GROUPMEMBER_LIST_URL;
    //邀请用户加入群组
    public static String GROUPMEMBER_INVITE_URL;
    //发送消息
    public static String MESSAGE_SEND_URL;
    //批量消息接收回执
    public static String MESSAGE_BATCH_RECEIVE_URL;
    //单个消息接收回执
    public static String MESSAGE_RECEIVED_URL;
    //阅读消息接收回执
    public static String MESSAGE_READ_URL;
    //转发消息
    public static String MESSAGE_FORWARD_URL;
    //撤回消息
    public static String MESSAGE_REVOKE_URL;
    //获取批量离线消息
    public static String MESSAGE_OFFLINELIST_URL;
    //阅读消息接收回执
    public static String ARTICLE_PUBLISH_URL;
    //获取空间内容列表
    public static String ARTICLE_RELEVANTLIST_URL;
    //获取别人的空间内容列表
    public static String ARTICLE_LIST_URL;
    //获取我的空间内容列表
    public static String ARTICLE_MY_LIST_URL;
    //获取文章内容详情
    public static String ARTICLE_DETAILED_URL;
    //删除文章内
    public static String ARTICLE_DELETE_URL;
    //发表评论
    public static String COMMENT_PUBLISH_URL;
    //发表点赞
    public static String COMMENT_PRAISE_URL;
    //删除评论，点赞
    public static String COMMENT_DELETE_URL;
    //获取系统配置
    public static String CONFIG_LIST_URL;
    //获取系统配置
    public static String CHECK_NEW_VERSION_URL;
    //用户反馈
    public static String FEEDBACK_PUBLISH_URL;
    public static String HOST_DISPENSE_URL;
    public static String BOTTLE_THROW_URL;
    public static String BOTTLE_RECEIVED_URL;
    public static String BOTTLE_GET_URL;
    public static String BOTTLE_DETAILED_URL;
    public static String BOTTLE_DISCARD_URL;
    public static String BOTTLE_DELETE_URL;
    public static String BOTTLE_LIST_URL;

    public static String SHAKE_SAVE_URL;
    public static String SHAKE_GET_NOW_URL;

    //同步后台基础数据
    public static String CONTACTS_SYNC_URL;
    //文件路径
    public static String FILE_PATH_URL;
    //上传文件
    public static String FILE_UPLOAD_URL;
    //db文件路径
    public static String DATABASE_FILE_URL;
    //获取我的空间权限设置
    public static String MOMENT_RULE_LIST;
    //保持空间权限设置
    public static String MOMENT_RULE_SAVE;
    //删除空间权限设置
    public static String MOMENT_RULE_DELETE;

    static {
        initialize();
    }

    public static void initialize() {
        String API_URL = ClientConfig.getServerPath() + "/cgi/";

        USER_LOGIN_URL = API_URL + "user/login.api";

        USER_LOGOUT_URL = API_URL + "user/logout.api";

        USER_DETAILED_URL = API_URL + "user/detailed.api";

        PUBACCOUNT_DETAILED_URL = API_URL + "publicAccount/detailed.api";

        PUBACCOUNT_LIST_URL = API_URL + "publicAccount/list.api";

        PUBACCOUNT_SUBSCRIBE_URL = API_URL + "subscriber/subscribe.api";

        PUBACCOUNT_UNSUBSCRIBE_URL = API_URL + "subscriber/unsubscribe.api";

        PUBACCOUNT_MENU_URL = API_URL + "publicMenu/list.api";
        PUBACCOUNT_LOOK_URL = API_URL + "publicAccount/look.api";
        USER_MODIFYPASSWORD_URL = API_URL + "user/modifyPassword.api";

        USER_NEARBY_URL = API_URL + "user/nearby.api";


        GROUP_DETAILED_URL = API_URL + "group/detailed.api";

        GROUP_LIST_URL = API_URL + "group/list.api";

        GROUPMEMBER_ADD_URL = API_URL + "groupMember/add.api";

        GROUPMEMBER_QUIT_URL = API_URL + "groupMember/quit.api";

        GROUPMEMBER_GETOUT_URL = API_URL + "groupMember/getout.api";

        GROUP_CREATE_URL = API_URL + "group/create.api";
        GROUP_UPDATE_URL = API_URL + "group/update.api";
        GROUP_DISBAND_URL = API_URL + "group/disband.api";

        GROUPMEMBER_LIST_URL = API_URL + "groupMember/list.api";

        GROUPMEMBER_INVITE_URL = API_URL + "groupMember/invite.api";

        MESSAGE_SEND_URL = API_URL + "message/send.api";

        MESSAGE_RECEIVED_URL = API_URL + "message/received.api";

        MESSAGE_READ_URL = API_URL + "message/read.api";

        MESSAGE_FORWARD_URL = API_URL + "message/forward.api";
        MESSAGE_BATCH_RECEIVE_URL = API_URL + "message/batchReceive.api";
        MESSAGE_REVOKE_URL = API_URL + "message/revoke.api";
        MESSAGE_OFFLINELIST_URL = API_URL + "message/offlineList.api";

        ARTICLE_PUBLISH_URL = API_URL + "article/publish.api";

        ARTICLE_RELEVANTLIST_URL = API_URL + "article/relevantList.api";

        ARTICLE_LIST_URL = API_URL + "article/list.api";

        ARTICLE_DETAILED_URL = API_URL + "article/detailed.api";

        ARTICLE_DELETE_URL = API_URL + "article/delete.api";
        ARTICLE_MY_LIST_URL = API_URL + "article/myList.api";
        COMMENT_PUBLISH_URL = API_URL + "comment/publish.api";
        COMMENT_PRAISE_URL = API_URL + "comment/praise.api";
        COMMENT_DELETE_URL = API_URL + "comment/delete.api";

        CONFIG_LIST_URL = API_URL + "config/list.api";


        FEEDBACK_PUBLISH_URL = API_URL + "feedback/publish.api";


        HOST_DISPENSE_URL = API_URL + "host/dispense.api";

        BOTTLE_RECEIVED_URL = API_URL + "bottle/received.api";
        BOTTLE_THROW_URL = API_URL + "bottle/release.api";
        BOTTLE_LIST_URL = API_URL + "bottle/list.api";
        BOTTLE_GET_URL = API_URL + "bottle/get.api";
        BOTTLE_DETAILED_URL = API_URL + "bottle/detailed.api";
        BOTTLE_DISCARD_URL = API_URL + "bottle/discard.api";
        BOTTLE_DELETE_URL = API_URL + "bottle/delete.api";

        SHAKE_SAVE_URL = API_URL + "snsshake/save.api";
        SHAKE_GET_NOW_URL = API_URL + "snsshake/getNow.api";

        CONTACTS_SYNC_URL = API_URL + "contacts/sync.api";

        DATABASE_FILE_URL = API_URL + "contacts/database.api";
        FILE_UPLOAD_URL = API_URL + "file/upload.api";

        FILE_PATH_URL = ClientConfig.getServerPath() + "/files/%1$s/%2$s";

        MOMENT_RULE_LIST = API_URL + "momentRule/list.api";
        MOMENT_RULE_SAVE = API_URL + "momentRule/save.api";
        MOMENT_RULE_DELETE = API_URL + "momentRule/delete.api";

        CHECK_NEW_VERSION_URL = API_URL + "config/getNewVersion.api";
    }
}
