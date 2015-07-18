package com.seu.server;

import javafx.beans.binding.ObjectExpression;

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

    /**
     *
     * @param s  用于初始化Server线程的socket连接
     */
    public ServerThread(Socket s){
        socket=s;
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
            System.out.println("Server: 服务器答复");
            oos.writeObject(out);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Server: Exception in funciton out");
        }
    }


    @Override
    public void run() {
        //out(new Message("",0,0,"服务器已链接"));
        try {
//            br = new BufferedReader(new InputStreamReader(
//                    socket.getInputStream(),"UTF-8"));

            //pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
            //        socket.getOutputStream(),"UTF-8")));
//            String line = null;
//            while ((line = br.readLine()) != null) {
//                System.out.println("Server： 服务端已收到消息："+line);
//                ServerThreadManager.getServerThreadManager().publish(this, line);
//            }
            Message msg = null;
            while (true) {
                try{
                    if((msg = (Message) ois.readObject()) != null) {
                        System.out.println("Server： 服务端已收到msg：" + msg.data);
                        ServerThreadManager.getServerThreadManager().publish(this, msg);
                    }
                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
            }
            //ServerThreadManager.getServerThreadManager().remove(this);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("断开了一个客户端链接");
            ServerThreadManager.getServerThreadManager().remove(this);
            e.printStackTrace();
        }
    }
}
