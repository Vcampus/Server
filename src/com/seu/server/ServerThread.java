package com.seu.server;

import javafx.beans.binding.ObjectExpression;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

/**
 * Classname:ServerThread
 *
 * Created by He on 2015/7/17.
 */
public class ServerThread extends Thread{
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    OAuthHelper oAuthHelper;
    DbHelper dbHelper;

    /**
     *
     * @param s  用于初始化Server线程的socket连接
     */
    public ServerThread(Socket s){
        socket=s;
        oAuthHelper = new OAuthHelper();
        dbHelper = new DbHelper();
        try{
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     *
     * @param out  要向客户端返回的Message
     */
    public void out(Message out) {
        try{
            System.out.println("ServerThread.out：Server: 服务器答复");
            oos.writeObject(out);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Server: Exception in funciton out");
        }
    }


    @Override
    public void run() {
        try {
            Message msg = null;
            while (true) {
                if((msg = (Message) ois.readObject()) != null) {
                    System.out.println("Server： 服务端已收到msg：" + msg.data);
                    Message respond = null;
                    switch (msg.type){
                        case GET:
                           respond = dealGet(msg);
                            break;
                        case POST:
                        case UPDATE:
                        case OAUTH:
                            break;
                        default:
                            respond = MessageFactory.getDefaultRespondMessage(msg.uid,400,
                                    "","Wrong from of type");
                            break;
                    }
                    ServerThreadManager.getServerThreadManager().publish(this, respond);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("断开了一个客户端链接");
            ServerThreadManager.getServerThreadManager().remove(this);
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }



    public Message dealGet(Message msg){
        if(oAuthHelper.isLogined(msg)){
            try {
                String sql = new JSONObject(msg.data).getString("sql");
                JSONObject dbrespond = new JSONObject(dbHelper.execute(sql,DbHelper.SELECT));
                System.out.println("Server.Thread.dealGet："+dbrespond);
                System.out.println("Server.Thread.dealGet："+dbrespond.getString("status"));
                return MessageFactory.getDefaultRespondMessage(msg.uid, 200,
                        dbrespond.getJSONArray("data"),
                        dbrespond.getString("status"));
            } catch (JSONException e) {
                e.printStackTrace();
                return MessageFactory.getDefaultRespondMessage(msg.uid,400,"","Wrong from of data");
            }
        }
        return MessageFactory.getDefaultRespondMessage(msg.uid,401,"","invalid uuid");
    }

//    public Message dealPost(Message msg){
//
//    }
//
    public Message dealUpdate(Message msg){
        if(oAuthHelper.isLogined(msg)){
            try {
                String sql = new JSONObject(msg.data).getString("sql");
                JSONObject dbrespond = new JSONObject(dbHelper.execute(sql,DbHelper.UPDATA));
                return MessageFactory.getDefaultRespondMessage(msg.uid, 200,
                        dbrespond.getJSONArray("data"),
                        "success");
            } catch (JSONException e) {
                e.printStackTrace();
                return MessageFactory.getDefaultRespondMessage(msg.uid,400,"","Wrong from of data");
            }
        }
        return MessageFactory.getDefaultRespondMessage(msg.uid,401,"","invalid uuid");
    }
//
//    public Message dealOauth(Message msg){
//
//    }


}
