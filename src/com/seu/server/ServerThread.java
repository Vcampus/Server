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
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream(),"UTF-8"));
            String line = null;
            while (true) {
                if((line = br.readLine()) != null)
                    System.out.println(line);
                ServerThreadManager.getServerThreadManager().publish(this, line);
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
