package com.seu.server;

import java.io.*;
import java.net.Socket;

/**
 * Classname:ServerThread
 *
 * Created by He on 2015/7/17.
 */
public class ServerThread extends Thread{
    private Socket socket;

    public ServerThread(Socket s){
        this.socket=s;
    }

    public void out(String out) {
        try{
            socket.getOutputStream().write((out+"\n").getBytes("UTF-8"));

        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Server: 断开了一个客户端链接");
            ServerThreadManager.getServerThreadManager().remove(this);
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        out("Server: 已连接到服务器");
        try {
            try{
                // 读取客户端传过来信息的DataInputStream
                DataInputStream in=new DataInputStream(socket.getInputStream());
                // 向客户端发送信息的DataOutputStream
                DataOutputStream out = new DataOutputStream(
                        socket.getOutputStream());
                while (true) {
                    // 读取来自客户端的信息
                    String accpet = in.readUTF();
                    System.out.println(accpet);
                    out.writeUTF("服务器：" + accpet);
                }
            }
            finally {
                //建立失败的话不会执行socket.close
                socket.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }



}
