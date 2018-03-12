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
package com.farsunset.lvxin.database;

import android.text.TextUtils;

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.bean.ChatItem;
import com.farsunset.lvxin.message.builder.BaseBuilder;
import com.farsunset.lvxin.message.parser.MessageParser;
import com.farsunset.lvxin.message.parser.MessageParserFactory;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.model.SystemMessage;
import com.google.gson.Gson;
import com.j256.ormlite.dao.GenericRawResults;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public  class MessageRepository extends BaseRepository<Message, String> {

    private static MessageRepository manager = new MessageRepository();

    public  static void add(Message msg) {

        //9开头的为无需记录的动作消息
        if (msg.isActionMessage()) {
            return;
        }
        manager.innerSave(msg);
    }

    public  static Message queryLastMessage(String otherId, Object[] action) {
        try {

            return manager.databaseDao.queryBuilder()
                    .orderBy("timestamp", false)
                    .where().raw(formatSQLString("(sender=?   or  receiver=?)", otherId, otherId))
                    .and().in("action", action).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Message queryReceivedLastMessage(String sender, Object[] action) {
        try {

            return manager.databaseDao.queryBuilder()
                    .orderBy("timestamp", false)
                    .where().eq("sender", sender)
                    .and().in("action", action).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteIncludedSystemMessageList(Object[] action) {

        String actions = TextUtils.join(",", action);
        String sql = "delete from " + Message.TABLE_NAME + "   where sender = ? and  action  in(" + actions + ") ";
        String[] params = new String[]{Constant.SYSTEM,};
        manager.innerExecuteSQL(sql, params);

    }

    public static List<Message> queryIncludedSystemMessageList(Object[] action) {

        try {
            return manager.databaseDao.queryBuilder().orderBy("timestamp", false)
                    .where()
                    .eq("sender", Constant.SYSTEM)
                    .and()
                    .in("action", action)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<Message>();
    }

    public static void updateStatus(String mid, String status) {

        String sql = "update " + Message.TABLE_NAME + " set status = ? where mid = ? ";
        manager.innerExecuteSQL(sql, new String[]{status, mid});
    }

    public static void resetSendingStatus() {

        String sql = "update " + Message.TABLE_NAME + " set status = ? where status = ? ";
        manager.innerExecuteSQL(sql, new String[]{Constant.MessageStatus.STATUS_SEND_FAILURE, Constant.MessageStatus.STATUS_SENDING});
    }

    public static List<MessageSource> getRecentContacts(Object[] includedTypes) {
        List<ChatItem> list = getRecentMessage(includedTypes);
        List<MessageSource> sourceList = new ArrayList<MessageSource>();
        for (ChatItem chat : list) {
            sourceList.add(chat.source);
        }
        return sourceList;
    }

    /**
     * 查询首页最近消息列表。按照用户进行分组
     * @param includedTypes 只查询包含这些类型的消息
     * @return
     */
    public static List<ChatItem> getRecentMessage(Object[] includedTypes) {
        List<ChatItem> chatList = new ArrayList<ChatItem>();
        try {
            String actions = TextUtils.join(",", includedTypes);
            String account = Global.getCurrentAccount();
            String sql = "SELECT mid,sender,receiver,action,format,title,content,extra,status,handleStatus,timestamp FROM ( SELECT receiver as account, * FROM " + Message.TABLE_NAME + " WHERE sender = ? AND receiver != ?  and action in("+actions+") UNION SELECT sender as account, * FROM " + Message.TABLE_NAME +" WHERE sender !=? AND receiver = ? and action in("+actions+")) GROUP BY account ORDER BY max(timestamp) DESC";
            GenericRawResults<String[]> rawResults = manager.databaseDao.queryRaw(sql,new String[]{account,account,account,account});
            while (rawResults.iterator().hasNext()) {
                String [] row = rawResults.iterator().next();
                Message message = new Message();
                message.mid = row[0];
                message.sender = row[1];
                message.receiver = row[2];
                message.action = row[3];
                message.format = row[4];
                message.title = row[5];
                message.content = row[6];
                message.extra = row[7];
                message.status = row[8];
                message.handleStatus = row[9];
                message.timestamp = Long.parseLong(row[10]);

                MessageParser messageParser = MessageParserFactory.getFactory().getMessageParser(message.action);
                ChatItem chatItem = new ChatItem(messageParser.getMessageSource(message),message);
                chatList.add(chatItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chatList;

    }

    public static long countNewMessage(Object[] action) {

        try {
            return manager.databaseDao.queryBuilder().where().eq("status", Message.STATUS_NOT_READ).and().in("action", action).countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long countNewBySender(String sender, String action) {

        if (sender == null) {
            return 0;
        }
        try {
            return manager.databaseDao.queryBuilder().where().eq("sender", sender).and().eq("status", Message.STATUS_NOT_READ).and().eq("action", action).countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public static long countNewIncludedTypesBySender(String sender, Object[] action) {

        try {
            return manager.databaseDao.queryBuilder().where().eq("sender", sender).and().eq("status", Message.STATUS_NOT_READ).and().in("action", action).countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;


    }

    public static long countNewByType(String action) {

        try {
            return manager.databaseDao.queryBuilder().where().eq("action", action).and().eq("status", Message.STATUS_NOT_READ).countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void updateHandleStatus(String status, String mid) {

        String sql = "update " + Message.TABLE_NAME + " set handleStatus = ? where   mid = ?";
        manager.innerExecuteSQL(sql, new String[]{status, mid});

    }

    public static void deleteBySenderOrReceiver(String id) {
        String sql = "delete from " + Message.TABLE_NAME + " where sender = ? or receiver =?";
        manager.innerExecuteSQL(sql, new String[]{id,id});
    }

    public static void deleteByActions(String account, String[] action) {
        String actions = TextUtils.join(",", action);
        String sql = "delete from " + Message.TABLE_NAME + " where (sender = ?  or receiver = ?) and action in(" + actions + ")";
        manager.innerExecuteSQL(sql, new String[]{account, account});
    }

    public static void deleteByAction(String account, String action) {
        String sql = "delete from " + Message.TABLE_NAME + " where (sender = ?  or receiver = ?) and action =?";
        manager.innerExecuteSQL(sql, new String[]{account, account, action});
    }

    public static void deleteByActionAndContent(String action, String content) {
        String sql = "delete from " + Message.TABLE_NAME + " where content  = ? and action =?";
        manager.innerExecuteSQL(sql, new String[]{content, action});
    }

    public static void deleteByActionAndExtra(String action, String extra) {
        String sql = "delete from " + Message.TABLE_NAME + " where extra  = ? and action =?";
        manager.innerExecuteSQL(sql, new String[]{extra, action});
    }

    public static void deleteByAction(String action) {
        String sql = "delete from " + Message.TABLE_NAME + " where  action =?";
        manager.innerExecuteSQL(sql, new String[]{action});
    }

    public static Message queryById(String mid) {

        return manager.innerQueryById(mid);
    }

    public static void batchModifyAgree(String sourceAccount, String msgType) {

        try {
            List<Message> list = manager.databaseDao.queryForEq("action", msgType);
            for (Message m : list) {
                String account = new Gson().fromJson(m.content, BaseBuilder.class).account;
                if (sourceAccount.equals(account)) {
                    updateHandleStatus(SystemMessage.RESULT_AGREE, m.mid);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static int queryIncludedNewCount(Object[] includedTypes) {

        try {
            return (int) manager.databaseDao.queryBuilder().where().eq("status", Message.STATUS_NOT_READ).and().in("action", includedTypes).countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;


    }

    public static List<Message> queryMessage(String sender, Object[] action, int pagenow) {
        try {
            return manager.databaseDao.queryBuilder().offset((pagenow - 1) * Constant.MESSAGE_PAGE_SIZE).limit(Constant.MESSAGE_PAGE_SIZE)
                    .orderBy("timestamp", false)
                    .where().raw(formatSQLString("(receiver=? or sender=? )", sender, sender))
                    .and().in("action", action).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<Message>();
    }

    public static List<Message> queryMessageByAction(String action) {
        try {
            return manager.databaseDao.queryBuilder().where().eq("action", action).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<Message>();
    }

    public static List<Message> queryNewMoments(long maxSize) {
        try {
            return manager.databaseDao.queryBuilder().offset(0L).limit(maxSize)
                    .orderBy("timestamp", false)
                    .where().raw(formatSQLString("(action=?   or  action=?)", Constant.MessageAction.ACTION_801, Constant.MessageAction.ACTION_802))
                    .and().eq("status", Message.STATUS_NOT_READ).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<Message>();
        }
    }

    public static List<Message> queryMoments() {

        try {
            return manager.databaseDao.queryBuilder()
                    .orderBy("timestamp", false)
                    .where().raw(formatSQLString("(action=?   or  action=?)", Constant.MessageAction.ACTION_801, Constant.MessageAction.ACTION_802))
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return new ArrayList<Message>();


    }

    public static Message queryNewMomentMessage() {

        try {
            return manager.databaseDao.queryBuilder().orderBy("timestamp", false).where().eq("action", Constant.MessageAction.ACTION_800).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;


    }

    public static void batchReadMessage(String[] actions) {

        String sql = "update " + Message.TABLE_NAME + " set status = ? where action in(" + TextUtils.join(",", actions) + ") ";
        manager.innerExecuteSQL(sql, new String[]{Message.STATUS_READ});

    }

    public static void batchReadMessage(String id, String[] actions) {
        String sql = "update " + Message.TABLE_NAME + " set status = ? where (sender = ? or receiver = ?) and action in(" + TextUtils.join(",", actions) + ") ";
        manager.innerExecuteSQL(sql, new String[]{Message.STATUS_READ, id, id});
    }

    public static String formatSQLString(String rawSql, Object... args) {
        StringBuffer sb = new StringBuffer(rawSql);
        for (Object arg : args) {
            int index = sb.indexOf("?");
            if (arg.getClass() == String.class) {
                sb.replace(index, index + 1, "'" + arg + "'");
            } else {
                sb.replace(index, index + 1, arg.toString());
            }
        }
        return sb.toString();
    }

    public static void updateSender(String mid, String sender) {

        String sql = "update " + Message.TABLE_NAME + " set sender = ? where mid = ? ";
        manager.innerExecuteSQL(sql, new String[]{sender, mid});
    }

    public static void updateReceiver(String mid, String receiver) {
        String sql = "update " + Message.TABLE_NAME + " set receiver = ? where   mid = ?  ";
        manager.innerExecuteSQL(sql, new String[]{receiver, mid});
    }

    public static void deleteById(String gid) {
        manager.innerDeleteById(gid);
    }
}
