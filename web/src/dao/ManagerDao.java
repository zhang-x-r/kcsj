package dao;

import domain.Manager;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//张欣茹 201802104009
public class ManagerDao {
    public Manager login(String username, String password) throws SQLException{
        Manager manager = null;
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String manager_sql = "SELECT * FROM manager WHERE username =? and password = ?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(manager_sql);
        //给预编译语句赋值
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,password);
        //执行预编译语句
        ResultSet resultSet = preparedStatement.executeQuery();
        //判断结果集的否有东西
        if (resultSet.next()){
            manager = new Manager(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"));
        }
        return manager;
    }
}
