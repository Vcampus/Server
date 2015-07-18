package com.seu.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

/**
 * Created by He on 2015/7/17.
 */
public class TestClient {
    Socket socket;
    BufferedReader br = null;
    PrintWriter pw = null;
    static int count=0;
    public TestClient(){
        try {
            //客户端socket指定服务器的地址和端口号
            socket = new Socket("127.0.0.1", 8000);
            System.out.println("Client： 已连接到Socket=" + socket);
            br = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())));
            Thread clientListener = new Thread(new Runnable() {
                @Override
                public void run() {
                    String line=null;
                    System.out.println("Client: 开始监听服务器返回结果");
                    try {
                        while((line = br.readLine()) !=null){
                            System.out.println("Client receive:"+line);
                        }
                    }
                    catch (IOException e){
                        e.printStackTrace();
                        System.out.println("Client receive error");
                    }
                }
            });
            clientListener.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        TestClientFrame testClientFrame = new TestClientFrame();

    }

    /**
     *
     * @param s
     * @param strSend
     */
    public void sendMessage(Socket s,String strSend){
        try {
            //客户端socket指定服务器的地址和端口号

            socket = s;
            //同服务器原理一样
            pw.println(strSend+count);
            count++;
            //调用flush方法后会清空输入缓存并且向服务器发送消息
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class TestClientFrame extends JFrame{
        public TestClientFrame(){
            setTitle("TestClient");
            JButton ButtonSend=new JButton("Send");
            add(ButtonSend);
            setBounds(0, 0, 200, 100);
            show();
            ButtonSend.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Thread send=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendMessage(socket,"ooo");
                        }
                    });
                    send.start();
                }
            });
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    System.out.println("Client: Exit");
                    System.exit(0);
                    try {
                        System.out.println("close");
                        br.close();
                        pw.close();
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }


    }
}
