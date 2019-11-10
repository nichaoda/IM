package com.example.im.util;

import java.sql.*;

import static com.example.im.info.ConstValues.DATABASE_NAME;
import static com.example.im.info.ConstValues.DATABASE_PASSWORD;
import static com.example.im.info.ConstValues.DATABASE_URL;
import static com.example.im.info.ConstValues.DATABASE_USER;
import static com.example.im.info.ConstValues.DRIVER_NAME;

public class MySqlDBHelper {
    private Connection conn;
    /**
     * 预编译的Sql语句对象
     */
    private PreparedStatement psmt;
    /**
     * 结果集
     */
    private ResultSet rs = null;

    /**
     * 连接数据库
     */
    public MySqlDBHelper(String sql) {
        try {
            // 注册驱动
            Class.forName(DRIVER_NAME);
            // 连接数据库
            conn = DriverManager.getConnection(DATABASE_URL + DATABASE_NAME
                            + "?useUnicode=true&characterEncoding=utf8",
                    DATABASE_USER, DATABASE_PASSWORD);
            // 获取sql语句管理对象
            getPreparedStatement(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public void closeConnection() {
        try {
            if (psmt != null) {
                psmt.close();
            }
            if (conn != null) {
                conn.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取SQL语句管理对象
     *
     * @param sql sql语句
     */
    private void getPreparedStatement(String sql) {
        try {
            psmt = conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给sql语句中的?赋值
     *
     * @param value 对应参数的值
     */
    public void setData(String[] value) {
        try {
            for (int pos = 1; pos <= value.length; pos++) {
                psmt.setString(pos, value[pos - 1]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 增删改操作
     *
     * @return DML操作的行数，-1表示操作失败
     */
    public int executeSQL() {
        // 操作失败
        int row = -1;
        try {
            return psmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }

    /**
     * 查询操作
     *
     * @return 包含查询到的数据的ResultSet对象
     */
    public ResultSet executeQuery() {
        try {
            if (psmt.executeQuery() != null) {
                return psmt.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
}
