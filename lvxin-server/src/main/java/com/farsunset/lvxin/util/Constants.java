/**
 * Copyright 2013-2033 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.farsunset.lvxin.util;

public interface Constants {

	public String SYSTEM = "system";

	public String DEF_PASSWORD = "000000";

	public String SEQUENCE_GROUP_ID = "SEQUENCE_GROUP_ID";

	public long LONG_10000 = 10000L;

	public long MAX_PUB_ROOT_MENU = 3;

	public long MAX_PUB_SUB_MENU = 5;
	
	public String LOCAL_BUCKET = "bucket";

	public static interface MessageAction {

		// 用户之间的普通消息
		public static final String ACTION_0 = "0";

		public static final String ACTION_1 = "1";

		// 系统向用户发送的普通消息
		public static final String ACTION_2 = "2";

		// 群里用户发送的 消息
		public static final String ACTION_3 = "3";

		/**
		 * ********************************************1开头统一为聊天消息**********************************************************
		 */

		// 系统定制消息---进群请求
		String ACTION_102 = "102";

		// 系统定制消息---同意进群请求
		String ACTION_103 = "103";

		// 系统定制消息---群解散消息
		String ACTION_104 = "104";

		// 系统定制消息---邀请入群请求
		String ACTION_105 = "105";

		// 系统定制消息---同意邀请入群请求
		String ACTION_106 = "106";

		// 系统定制消息---被剔除群
		String ACTION_107 = "107";

		// 系统定制消息---消息被阅读
		String ACTION_108 = "108";

		// 系统定制消息---被好友删除
		String ACTION_109 = "109";

		// 系统定制消息---好友替换了头像
		String ACTION_110 = "110";

		// 系统定制消息---好友修改了名称或者签名
		String ACTION_111 = "111";

		// 系统定制消息---用户退出了群
		String ACTION_112 = "112";

		// 系统定制消息---用户加入了群
		String ACTION_113 = "113";

		// 系统定制消息---群信息被修改
		String ACTION_114 = "114";
		/**
		 * ********************************************2开头统一为公众号消息**********************************************************
		 */
		// 系统定制消息---用户向公众号发消息
		String ACTION_200 = "200";

		// 系统定制消息---公众号向用户回复的消息
		String ACTION_201 = "201";

		// 系统定制消息---公众号向用户群发消息
		String ACTION_202 = "202";

		// 系统定制消息---公众号信息更新
		String ACTION_203 = "203";

		// 系统定制消息---公众号菜单信息更新
		String ACTION_204 = "204";
		// 系统定制消息---公众号LOGO更新
		String ACTION_205 = "205";

		/**
		 ********************************************* 4开头统一为系统控制消息**********************************************************
		 */
		// 系统定制消息---强制下线消息
		String ACTION_444 = "444";

		/**
		 * ********************************************7开头统一为漂流瓶消息**********************************************************
		 */
		// 系统定制消息---漂流瓶消息
		String ACTION_700 = "700";

		// 系统定制消息---删除漂流瓶
		String ACTION_701 = "701";

		/**
		 * ********************************************8开头统一为圈子动态消息**********************************************************
		 */
		// 系统定制消息---好友新动态消息
		String ACTION_800 = "800";

		// 系统定制消息---好友新动态评论消息
		String ACTION_801 = "801";

		// 系统定制消息---好友新动态评论回复评论消息
		String ACTION_802 = "802";

		// 系统定制消息---删除动态
		String ACTION_803 = "803";

		// 系统定制消息---删除评论或取消点赞
		String ACTION_804 = "804";

		/**
		 * ********************************************9开头统一为动作消息**********************************************************
		 */
		// 系统定制消息---好友下线消息
		String ACTION_900 = "900";

		// 系统定制消息---好友上线消息
		String ACTION_901 = "901";

		// 系统定制消息---更新用户数据
		String ACTION_998 = "998";

		// 系统定制消息---在另一台设备登录强制下线消息
		String ACTION_999 = "999";
	}

	interface MessageFormat {

		// 文字
		String FORMAT_TEXT = "0";

		// 图片
		String FORMAT_IMAGE = "1";

		// 语音
		String FORMAT_VOICE = "2";

		// 文件
		String FORMAT_FILE = "3";

		// 地图
		String FORMAT_MAP = "4";
		// 视频
		String FORMAT_VIDEO = "8";

	}
}
