package com.seu.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classname:ServerMain
 *
 * Created by He on 2015/7/17.
 * */

public class ServerMain {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		System.out.println("Server: Server running..");
		System.out.println("我");
		ServerListener serverListener=new ServerListener();
		serverListener.start();
		TestClient testClient = new TestClient();
		testClient.start();
	}

}
