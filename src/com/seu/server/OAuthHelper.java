package com.seu.server;

import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;

/**
 *
 * Classname: OAuthHelper  用于辅助注册和认证的类
 *
 * Created by He on 2015/7/18.
 *
 */
public class OAuthHelper{

    private String musername;
    private String mpassword;
    private String muuid;
    private DbHelper dbHelper = new DbHelper();

    public OAuthHelper(String username,String password){
        musername = username;
        mpassword = password;
    }

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
            String sql = "SELECT * FROM USER WHERE uuid = "+uuid;
            JSONObject dbrespond = new JSONObject(dbHelper.execute(sql,DbHelper.SELECT));
            System.out.println(dbrespond.getJSONArray("data").opt(0));
            if(dbrespond.getJSONArray("data").opt(0) != null)
                return true;
            else return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }



}
