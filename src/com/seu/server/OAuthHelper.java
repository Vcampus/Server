package com.seu.server;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.UUID;

/**
 *
 * Classname: OAuthHelper  用于辅助注册和认证的类
 *
 * Created by He on 2015/7/18.
 *
 */
public class OAuthHelper{
    private DbHelper dbHelper = new DbHelper();
    public OAuthHelper(){}

    /**
     *
     * @param msg  需要检测是否登陆的报文
     * @return  如果已经登陆返回true，否则返回false
     * @throws JSONException
     */
    public boolean isLogined (Message msg){
        try {
            JSONObject askmsg = new JSONObject(msg.data);
            String uuid = askmsg.getString("uuid");
            if(uuid.equals(""))
                return false;
            String sql = "SELECT * FROM USER WHERE uuid = \'" + uuid + "\'";
            JSONObject dbrespond = new JSONObject(dbHelper.execute(sql,DbHelper.SELECT));
            System.out.println("OAuthhelper.isLogined:"+dbrespond.getJSONArray("data").opt(0));
            if(dbrespond.getJSONArray("data").opt(0) != null)
                return true;
            else return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     *
     * @param username 需要匹配的用户名
     * @return  如果该用户已注册则返回true，反之则返回false
     */
    public boolean isSigned (String username){
        try {
            String sql = "SELECT * FROM USER WHERE username = \'" + username + "\'";
            JSONObject dbrespond = new JSONObject(dbHelper.execute(sql,DbHelper.SELECT));
            System.out.println("OAuthhelper.isSigned:"+dbrespond.getJSONArray("data").opt(0));
            if(dbrespond.getJSONArray("data").opt(0) != null)
                return true;
            else return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param username 用户名
     * @param password  密码
     * @return          当用户名和密码匹配时，返回uuid，否则返回null
     */
    public  String isCorrect(String username,String password){
        try {
            String sql = "SELECT * FROM USER WHERE username = \'" + username +
                    "\' AND digested_password = \'" + password + "\'";
            JSONObject dbrespond = new JSONObject(dbHelper.execute(sql,DbHelper.SELECT));
            System.out.println("OAuthhelper.isCorrect:"+dbrespond.getJSONArray("data").opt(0));
            if(dbrespond.getJSONArray("data").opt(0) != null){
                JSONObject user = new JSONObject(dbrespond.getJSONArray("data").opt(0).toString());
                String uuid = user.getString("uuid");
                return uuid;
            }
            else return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     *
     * @param username  用户名
     * @param password  密码
     * @return          如果注册成功返回success，如果失败则返回错误信息
     */
    public String addRegister(String username,String password){
        try{
            String uuid = UUID.randomUUID().toString();
            String sql = "INSERT INTO `course`.`user` (`username`, `digested_password`, `uuid`)" +
                    " VALUES ('" + username +
                    "', '" + password +
                    "', '" + uuid + "')";
            JSONObject dbrespond = new JSONObject(dbHelper.execute(sql,DbHelper.UPDATA));
            System.out.println("OAuthhelper.addRegister:"+dbrespond.getString("data"));
            return dbrespond.getString("status");
        }catch (JSONException e){
            return null;
        }
    }
}
