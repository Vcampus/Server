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

    /**
     *
     * @param st
     * st 是要加入到线程管理器中的ServerThread线程
     *
     */
    public void add(ServerThread st){
        vector.add(st);
    }

    /**
     *
     * @param st
     * 要从线程管理器中移除的线程
     *
     */
    public void remove(ServerThread st) {
        vector.remove(st);
    }

    /**
     *
     * @param st
     * 要在管理器中查找的目标线程
     *
     * @param out
     * 向目标线程发送消息
     */
    public void publish(ServerThread st,String out) {
        for (int i = 0; i < vector.size(); i++) {
            ServerThread stServerThread= vector.get(i);
            if (st.equals(stServerThread)) {
                stServerThread.out(out);
            }
        }
    }

}
