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
                            respond = dealPost(msg);
                            break;
                        case UPDATE:
                            respond = dealUpdate(msg);
                            break;
                        case AUTH:
                            respond = dealAuth(msg);
                            break;
                        default:
                            respond = MessageFactory.getDefaultRespondMessage(msg.uid,400,
                                    "","Invaild type.");
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
                if(dbrespond.getString("status").toString().equals("success")){
                    return MessageFactory.getDefaultRespondMessage(msg.uid, 200,
                            dbrespond.getJSONArray("data"),
                            dbrespond.getString("status"));
                } else{
                    return MessageFactory.getDefaultRespondMessage(msg.uid, 200,
                            dbrespond.getString("data"),
                            dbrespond.getString("status"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return MessageFactory.getDefaultRespondMessage(msg.uid,400,"","Invalid form of data.");
            }
        }
        return MessageFactory.getDefaultRespondMessage(msg.uid,401,"","invalid uuid");
    }

    /**
     *
     * @param msg   包含需要注册的用户名和密码的msg，
     * @return      如果注册成功或者出现问题，则返回成功信息或者错误信息
     */
    public Message dealPost(Message msg){
        try{
            String username = new JSONObject(msg.data).getString("username");
            String password = new JSONObject(msg.data).getString("password");
            if(!oAuthHelper.isSigned(username)) {
                String result = oAuthHelper.addRegister(username,password);
                if(result != null)
                    return MessageFactory.getDefaultRespondMessage(msg.uid,200,"",result);
                return MessageFactory.getDefaultRespondMessage(msg.uid,400,"","failed");
            }
            else
                return MessageFactory.getDefaultRespondMessage(msg.uid,403,"","Username has been registered.");
        }catch (JSONException e){
            e.printStackTrace();
            return MessageFactory.getDefaultRespondMessage(msg.uid,400,"","Invalid form of data.");
        }
    }

    public Message dealUpdate(Message msg){
        if(oAuthHelper.isLogined(msg)){
            try {
                String sql = new JSONObject(msg.data).getString("sql");
                JSONObject dbrespond = new JSONObject(dbHelper.execute(sql,DbHelper.UPDATA));
                System.out.println("Server.Thread.dealUpdate："+dbrespond);
                System.out.println("Server.Thread.dealUpdate："+dbrespond.getString("data"));
                return MessageFactory.getDefaultRespondMessage(msg.uid, 200,
                        dbrespond.getString("data"),
                        dbrespond.getString("status"));

            } catch (JSONException e) {
                e.printStackTrace();
                return MessageFactory.getDefaultRespondMessage(msg.uid,400,"","Invalid form of data.");
            }
        }
        return MessageFactory.getDefaultRespondMessage(msg.uid,401,"","Invalid uuid.");
    }


    /**
     *
     * @param msg 从客户端接收的用于认证的报文，data中比较特殊，只包含username和password（在前端已加密过）
     * @return  如果该用户已注册并且密码和用户名匹配，则返回该用户的信息
     *          如果改用户已注册但是密码和用户名不匹配，则返回错误信息密码错误
     *          如果未注册，则返回信息该用户未注册
     */
    public Message dealAuth(Message msg){
        try{
            String username = new JSONObject(msg.data).getString("username");
            String password = new JSONObject(msg.data).getString("password");
            if(oAuthHelper.isSigned(username)) {
                String result = oAuthHelper.isCorrect(username,password);
                if(result != null)
                    return MessageFactory.getDefaultRespondMessage(msg.uid,200,result,"success");
                else
                    return MessageFactory.getDefaultRespondMessage(msg.uid,400,"","Wrong password.");
            }
            else
                return MessageFactory.getDefaultRespondMessage(msg.uid,400,"","User hasn't signed up.");

        }catch (JSONException e){
            e.printStackTrace();
            return MessageFactory.getDefaultRespondMessage(msg.uid,400,"","Invalid form of data.");
        }
    }


}
