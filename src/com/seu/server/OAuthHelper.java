package com.seu.server;

import org.json.JSONObject;

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
    private DbHelper dbHelper;

    public OAuthHelper(String username,String password){
        musername = username;
        mpassword = password;
    }

}
