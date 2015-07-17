package com.seu.server;

import java.util.Vector;

/**
 * Classname:ServerThreadManger
 *
 * Created by He on 2015/7/17.
 */
public class ServerThreadManager {

    //静态变量及方法
    private static final ServerThreadManager sm = new ServerThreadManager();
    public static ServerThreadManager getServerThreadManager(){
        return sm;
    }

    //实例变量
    Vector<ServerThread> vector = new Vector<ServerThread>();

    //方法
    private ServerThreadManager(){}

    public void add(ServerThread st){
        vector.add(st);
    }

    public void remove(ServerThread st) {
        vector.remove(st);
    }

    public void publish(ServerThread cs,String out) {
        for (int i = 0; i < vector.size(); i++) {
            ServerThread stServerThread= vector.get(i);
            if (!cs.equals(stServerThread)) {
                stServerThread.out(out);
            }
        }
    }

}
