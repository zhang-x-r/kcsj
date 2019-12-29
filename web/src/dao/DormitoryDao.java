package dao;

import domain.Dormitory;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DormitoryDao {
    private static  DormitoryDao dormitoryDao = new DormitoryDao();
    public static DormitoryDao getInstance(){return dormitoryDao;}

    public Dormitory find(int id) throws SQLException{
        //定义一个int类型的变量
        Dormitory dormitory = null;
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String sql = "SELECT * FROM dormitory WHERE id = ?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //给预编译语句赋值
        preparedStatement.setInt(1,id);
        //执行预编译语句，并获得一个结果集对象
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            dormitory = new Dormitory(resultSet.getInt("id"),
                    resultSet.getInt("dno"),
                    resultSet.getFloat("remain_water"),
                    resultSet.getFloat("remain_ele"));
        }
        return dormitory;
    }
}
