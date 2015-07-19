package com.seu.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import com.mysql.jdbc.Driver;

/**
 * Created by 永东 on 2015/7/18.
 */
public class DbHelper {
    Connection conn=null;
    Statement stmt=null;
    ResultSet rs=null;

    /*分别标识4类SQL语言*/
    final static int SELECT=1;
    final static int UPDATA=2;
    final static int DELETE=3;
    final static int SQLDDL=4;

    public DbHelper(){
        init();
    }

    public Boolean init() {
        try {
//            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
//            Class.forName("oracal.jdbc.driver.OracalDriver");
//            Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e) {
            System.out.print("数据库驱动类驱动失败");
            e.printStackTrace();
            return false;
        }

        String url = "jdbc:mysql://localhost:3306/course";
        String username = "root" ;
        String password = "" ;
        try {
            conn = DriverManager.getConnection(url, username, password);
            stmt= conn.createStatement();

        }
        catch(SQLException se){
            System.out.println("数据库连接失败！");
            se.printStackTrace() ;
            return false;
        }
        return true;
    }

    /**
     *
     * @param sql  要执行的sql语句
     * @param TYPE_SQL  要执行的sql语句的类型
     *
     */
    public String execute(String sql,int TYPE_SQL){
        JSONObject result=new JSONObject();
        switch (TYPE_SQL){
            case SELECT:
                try {
                    rs = executeQuery(sql);
                    ResultSetMetaData rsmd = rs.getMetaData();

                    //获取各列名称
                    int colnum=rsmd.getColumnCount();
                    String[] colname=new String[colnum];
                    for(int i=1;i<=colnum;i++)
                        colname[i-1]=rsmd.getColumnName(i);

                    JSONArray dataarray=new JSONArray();
                    while(rs.next()){
                        JSONObject dataobject=new JSONObject();
                        for(int i=1;i<=colnum;i++){
                            dataobject.put(colname[i-1],rs.getString(i));

                        }
                        dataarray.put(dataobject);
                    }
                    result.put("status","success");
                    result.put("data",dataarray);
                    System.out.println("DbHelper.excute:"+result);
                }
                catch (Exception e){
                    e.printStackTrace();
                    try{
                        result.put("status", e.getMessage());
                        result.put("data","");
                        System.out.println(result.toString());
                    }catch (JSONException e1){
                        e1.printStackTrace();
                    }
                }finally {
                    return result.toString();
                }
            default:
                try {
                    int success=executeUpdate(sql);
                    result.put("status", "success");
                    result.put("data","Update col:"+success);
                    System.out.println("DbHelper.excute"+result);

                }
                catch (Exception e){
                    e.printStackTrace();
                    try {
                        result.put("status",e.getMessage());
                        result.put("data","");
                    }catch (JSONException e1){
                        e1.printStackTrace();
                    }
                }finally {
                    return result.toString();
                }
        }
    }

    /**
     *
     * @param sql 要查询的sql语句
     * @return  ResultSet,单个结果集
     * @throws SQLException
     */
    public ResultSet executeQuery (String sql)throws SQLException {
        return stmt.executeQuery(sql);
    }


    /**
     *
     * @param sql 要查询的sql语句
     * @return  int,受到影响的行数，即更新计数
     * @throws SQLException
     */
    public int executeUpdate(String sql)throws SQLException {
        return stmt.executeUpdate(sql);
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        rs.close();
        stmt.close();
        conn.close();
    }
}
