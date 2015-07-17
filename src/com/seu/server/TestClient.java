package com.seu.server;

import java.io.*;
import java.net.Socket;

/**
 * Created by He on 2015/7/17.
 */
public class TestClient extends Thread{
    Socket socket;
    BufferedReader br = null;
    PrintWriter pw = null;
    @Override
    public void run() {
        super.run();
        try {
            //客户端socket指定服务器的地址和端口号
            socket = new Socket("127.0.0.1", 8000);
            System.out.println("Socket=" + socket);
            //同服务器原理一样
            br = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())));
            for (int i = 0; i < 10; i++) {
                pw.println("howdy " + i);
                pw.flush();
                String str = br.readLine();
                System.out.println(str);
            }
            pw.println("END");
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("close......");
                br.close();
                pw.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
