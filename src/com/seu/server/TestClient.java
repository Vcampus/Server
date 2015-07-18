package com.seu.server;

import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.UUID;

/**
 * Created by He on 2015/7/17.
 */
public class TestClient {
    Socket socket;
    ObjectInputStream  ois;
    ObjectOutputStream oos;
    static int count=0;
    public TestClient(){
        try {
            //客户端socket指定服务器的地址和端口号
            socket = new Socket("127.0.0.1", 8000);
            System.out.println("Client： 已连接到Socket=" + socket);

            try{
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
            }catch (IOException e){
                e.printStackTrace();
            }


            //下面的线程用于监听服务器返回的结果
            Thread clientListener = new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg=null;
                    System.out.println("Client: 开始监听服务器返回结果");
                    try {
                        while(true){
                            if((msg = (Message)ois.readObject()) !=null){
                                System.out.println("Client receive:"+msg.data);
                            }
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                        System.out.println("Client receive error");
                    }catch (ClassNotFoundException e1){
                        e1.printStackTrace();
                    }catch (NullPointerException e2){
                        e2.printStackTrace();
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
     * @param s  要使用的socket链接
     * @param strSend     通过socket连接向服务器发送的消息
     */
    public void sendMessage(Socket s,Message strSend){
        try {
            //客户端socket指定服务器的地址和端口号
            socket = s;
            //同服务器原理一样
            oos.writeObject(strSend);
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
                            JSONObject js=new JSONObject();
                            try{
                                js.put("username","He");
                                js.put("password","1995126");
                                Message s = new Message("a",10,10,js.toString());
                                sendMessage(socket, s);
//                                Message msg;
//                                try {
//                                    while(true){
//                                        if((msg = (Message)ois.readObject()) !=null){
//                                            System.out.println("Client receive:"+msg.data+count);
//                                            count++;
//                                            break;
//                                        }
//                                    }
//                                }catch (IOException e){
//                                    e.printStackTrace();
//                                    System.out.println("Client receive error");
//                                }catch (ClassNotFoundException e1){
//                                    e1.printStackTrace();
//                                }catch (NullPointerException e2){
//                                    //e2.printStackTrace();
//                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
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
                        ois.close();
                        oos.close();
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }


    }
}
