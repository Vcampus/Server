package com.seu.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classname:ServerListener
 *
 * Created by He on 2015/7/17.
 */
public class ServerListener extends Thread{
    @Override
    public void run() {
        try{
            ServerSocket serverSocket = new ServerSocket(8000);
            while(true) {
                //accept()会阻塞主线程，故要开一个新的线程处理端口监听
                Socket socket = serverSocket.accept();
                //建立连接
                System.out.println("Server: 有客户端连接到了本机的8000端口");
                //每连接一个客户端创建一个新的线程进行处理
                ServerThread serverThread = new ServerThread(socket);
                serverThread.start();
                //将新线程发送给线程管理器
                ServerThreadManager.getServerThreadManager().add(serverThread);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
