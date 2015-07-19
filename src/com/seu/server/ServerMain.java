package com.seu.server;

import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
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
		ServerListener serverListener=new ServerListener();
		serverListener.start();
		//TestClient testClient = new TestClient();
		//DbHelper db=new DbHelper();

		//String s=db.execute("select * from grade",1).toString();
		ServerClient s=new ServerClient();
		//Message msg = MessageFactory.getDefaultAskMessage("11111111",
		//		"INSERT INTO `course`.`user` (`username`, `digested_password`, `uuid`) VALUES ('Test2', '1232442', '2123312')", Message.TYPE.UPDATE);
//		String username = "Me";
//		String password = "12345678";
//		String sql = "SELECT * FROM USER WHERE username = \'"+ username + "\' AND digested_password = \'" +password+"\'";
//		Message msg = MessageFactory.getDefaultAskMessage("", sql, Message.TYPE.AUTH);
//		String data =msg.data;
//		String es = s.execute(msg).data;
		int es = s.getUUID("Me","12345678").code;
		System.out.println(es);
		Message msg = s.signUp("213","232131");
		System.out.println(msg.data);

	}

}
