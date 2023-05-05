package com.example.treasure.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
public class JDBCUtils {
    private static final String TAG = "mysql-treasure-JDBCUtils";

    private static String driver = "com.mysql.jdbc.Driver";

    private static String dbName = "treasure";

    private static String user = "root";

    private static String password = "1234";

    public static Connection getConn(){

        Connection connection = null;
        try{
            Class.forName(driver);
            String ip = "192.168.1.107"; //Your ip address

            connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + dbName+"?useSSL=false",
                    user, password);

        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

}

