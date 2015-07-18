package com.seu.server;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by He on 2015/7/18.
 *
 * 通信消息
 */
public class Message implements Serializable {
    String uid;
    int type;
    int code;
    String data;
    public Message(String uid,int type,int code,String data){
        this.uid = uid;
        this.type = type;
        this.code = code;
        this.data = data;
    }

}
