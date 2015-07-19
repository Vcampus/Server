package com.seu.server;

import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by He on 2015/7/19.
 */
public class MessageFactory {
    public MessageFactory(){}
    /**
     *
     * @param uuid 用于认证的uuid，当使用不需要登陆即可响应的请求时可以直接填空的
     * @param sql   需要服务器执行的sql语句
     * @param type  报文类型
     * @return      返回默认的用于向服务器发送请求的报文
     */
    public static Message getDefaultAskMessage(String uuid,String sql,Message.TYPE type){
        try {
            JSONObject data = new JSONObject();
            data.put("data","");
            data.put("sql",sql);
            data.put("status","");
            data.put("uuid",uuid);
            Message default_ask_message = new Message(UUID.randomUUID().toString(),type,0,data.toString());
            return default_ask_message;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Message getDefaultAuthMessage(String username,String password){
        try {
            JSONObject data = new JSONObject();
            data.put("username",username);
            data.put("password",password);
            Message default_ask_message = new Message(UUID.randomUUID().toString(), Message.TYPE.AUTH,0,data.toString());
            return default_ask_message;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param uid  返回报文的标志号，通常和对应的发送报文的uid一样
     * @param code  服务器响应状态号
     * @param str_data  返回的数据,此处必须是String类型的，如果是带“”号的数据返回后“前会有斜杠
     * @param status    返回的状态，成功时为success，失败时为错误原因
     * @return
     */
    public static Message getDefaultRespondMessage(String uid,int code,String str_data,String status){
        try {
            JSONObject data = new JSONObject();
            data.put("data",str_data);
            data.put("sql","");
            data.put("status",status);
            data.put("uuid","");
            Message default_ask_message = new Message(uid, Message.TYPE.POST,code,data.toString());
            return default_ask_message;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param uid  返回报文的标志号，通常和对应的发送报文的uid一样
     * @param code  服务器响应状态号
     * @param jsonArray_data 返回的数据,此处必须是JSONArray类型的，如果是带“”号的数据返回后也无斜杠
     * @param status    返回的状态，成功时为success，失败时为错误原因
     * @return
     */
    public static Message getDefaultRespondMessage(String uid,int code,JSONArray jsonArray_data,String status){
        try {
            JSONObject data = new JSONObject();
            data.put("data",jsonArray_data);
            data.put("sql","");
            data.put("status",status);
            data.put("uuid","");
            Message default_ask_message = new Message(uid, Message.TYPE.POST,code,data.toString());
            return default_ask_message;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }




}
