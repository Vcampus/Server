package com.seu.server;

import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

/**
 * Created by He on 2015/7/19.
 */
public class ServerClient {
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    public ServerClient(){
        try {
            socket = new Socket("127.0.0.1", 8000);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param msg  要向服务器发送的报文
     * @return      服务器响应结果
     */
    public Message execute(Message msg){
        try {
            oos.writeObject(msg);
            Message result;
            while(true){
                if((result = (Message)ois.readObject())!=null){
                    return result;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e1){
            e1.printStackTrace();
        }
        return new Message("", Message.TYPE.GET,0,"{\"data\",\"unknown error\"\"}");
    }

    public String getUUID(String username,String password){
//        String sql = "SELECT * FROM USER WHERE username = \'" + username +
//                "\' AND digested_password = \'" + password + "\'";
//        Message msg = MessageFactory.getDefaultAskMessage("", sql, Message.TYPE.AUTH);
//        Message result = execute(msg);
        Message msg = MessageFactory.getDefaultAuthMessage(username, password);
        Message result = execute(msg);
        return result.data;
    }

}
