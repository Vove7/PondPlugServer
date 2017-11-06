package cn.vove7.dbmanager;



import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by Vove on 2017/3/21.
 * 数据库通道
 */
public class DbManager {
    private Connection connection = null;
    private static final String databaseName = "pondplug";
    private static final String DB_Url = "jdbc:mysql://localhost:3306/" + databaseName + "?characterEncoding=utf8&useSSL=false";

    private static final String db_User = "your username";
    private static final String password = "your password";

    private PreparedStatement pStatement = null;

    public int executeUpdate(String sql, String[] strs) {
        if (!connect()) {
            System.out.println("连接失败");
            return 0;
        }
        try {
            pStatement = connection.prepareStatement(sql);
            for (int i = 1; i <= strs.length; i++) {
                pStatement.setString(i, strs[i - 1]);
            }
            return pStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            close();
        }
    }

    public void executeUpdate(String sql, String ss, byte[] solutionBytes) {
        if (!connect()) {
            System.out.println("连接失败");
            return;
        }
        try {
            pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, ss);
            pStatement.setBytes(2, solutionBytes);
            pStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public ResultSet executeQuery(String sql, String[] strs) {
        if (!connect()) {
            System.out.println("连接失败");
            return null;
        }
        try {
            pStatement = connection.prepareStatement(sql);
            if (strs != null)
                for (int i = 1; i <= strs.length; i++) {
                    pStatement.setString(i, strs[i - 1]);
                }
            return pStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean connect() {
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            connection = DriverManager.getConnection(DB_Url, db_User, password);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void close() {
        if (connection != null)
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public Object[] getOneRecord(ResultSet resultSet, String[] colNames) {
        Object[] objects = new Object[colNames.length];
        try {
            if (resultSet.next()) {
                int i = 0;
                for (String colName : colNames) {
                    objects[i++] = resultSet.getObject(colName);
                }
                return objects;
            } else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
