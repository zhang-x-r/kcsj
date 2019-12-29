package dao;

import domain.Payment;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public class PaymentDao {
    private static PaymentDao paymentDao = new PaymentDao();
    public static PaymentDao getInstance(){return paymentDao;}

    public Collection<Payment> findByDno(int dno) throws SQLException {
        Collection<Payment> payments = new HashSet<>();
        Payment payment;
        int id = 0;
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String find = "SELECT id FROM dormitory WHERE dno = ?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(find);
        //给预编译语句赋值
        preparedStatement.setInt(1,dno);
        //执行预编译语句
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            id = resultSet.getInt("id");
        }
        //写sql语句
        String find_sql = "SELECT * FROM payment WHERE dormitory_id = ?";
        //在该连接上创建预编译语句对象
        preparedStatement = connection.prepareStatement(find_sql);
        //给预编译语句赋值
        preparedStatement.setInt(1,id);
        //执行预编译语句
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            payment = new Payment(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getFloat("money"),
                    resultSet.getString("time"),
                    DormitoryDao.getInstance().find(resultSet.getInt("dormitory_id")));
            payments.add(payment);
        }
        return payments;
    }
}
