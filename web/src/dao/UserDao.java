package dao;

import domain.User;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private static UserDao userDao = new UserDao();
    public static UserDao getInstance(){return userDao;}
    // 张欣茹 201802104009
    public User login(String username, String password)  throws SQLException {
        User user = null;
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String cardMoney_sql = "SELECT * FROM card  WHERE cno = ? and password = ? ";
        //再该连接上创建预编译语句
        PreparedStatement preparedStatement = connection.prepareStatement(cardMoney_sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            user  = new User(resultSet.getInt("id"),
                    resultSet.getString("cno"),
                    resultSet.getString("password"));
        }

        return user;
    }
}
