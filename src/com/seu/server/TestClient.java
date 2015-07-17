package com.seu.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

/**
 * Created by He on 2015/7/17.
 */
public class TestClient {
    Socket socket;
    BufferedReader br = null;
    PrintWriter pw = null;
    public TestClient(){

        JFrame Client=new JFrame("TestClient");
        JButton ButtonSend=new JButton("Send");
        Client.add(ButtonSend);
        Client.setBounds(0,0,200,100);
        Client.show();
        ButtonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread send=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //客户端socket指定服务器的地址和端口号
                            socket = new Socket("127.0.0.1", 8000);
                            sendMessage(socket,"ooo");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                send.start();
            }
        });
    }

    public void sendMessage(Socket s,String strSend){
        try {
            //客户端socket指定服务器的地址和端口号
            socket = s;
            System.out.println("Socket=" + socket);
            //同服务器原理一样
            br = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())));
            pw.println(strSend);
            pw.flush();
            String str = br.readLine();
            System.out.println(str);
            pw.println("END");
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("close");
                br.close();
                pw.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
