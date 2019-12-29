package dao;

import domain.Card;
import service.StudentService;
import util.JdbcHelper;

import java.sql.*;

public class CardDao {
    private static CardDao cardDao = new CardDao();
    private CardDao() {
    }
    private  static int rows=9;
    public static CardDao getInstance() {
        return cardDao;
    }
    //李美青 201802104008
    public Card find(Integer id) throws SQLException {
        //声明一个Degree类型的变量
        Card card = null;
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String findCard_sql = "SELECT * FROM card WHERE id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(findCard_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1, id);
        //执行预编译语句
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Card对象
        //若结果集中没有记录，则本方法返回null
        if (resultSet.next()) {
            card = new Card(resultSet.getInt("id"),
                    resultSet.getString("cno"),
                    resultSet.getString("status"),
                    resultSet.getFloat("money"),
                    resultSet.getString("password"),
                    StudentDao.getInstance().findByStudent(resultSet.getString("student_sno")));
        }
        //关闭资源
        JdbcHelper.close(resultSet, preparedStatement, connection);
        return card;
    }
    //李美青 201802104008
    public Card findByStudent(String cno) throws SQLException {
        //声明一个Card类型的变量
        Card card = null;
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String findCardByStudent_sql = "SELECT * FROM card WHERE cno =?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(findCardByStudent_sql);
        //为预编译参数赋值
        preparedStatement.setString(1, cno);
        //执行预编译语句
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Card对象
        //若结果集中没有记录，则本方法返回null
        while (resultSet.next()) {
            card = new Card(resultSet.getInt("id"),
                    resultSet.getString("cno"),
                    resultSet.getString("status"),
                    resultSet.getFloat("money"),
                    resultSet.getString("password"),
                    StudentDao.getInstance().findByStudent(resultSet.getString("student_sno")));
        }
        //关闭资源
        JdbcHelper.close(resultSet, preparedStatement, connection);
        return card;
    }
    //李美青 201802104008
    public boolean update(Card card) throws ClassNotFoundException, SQLException {
        int affectedRows=0;
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        CallableStatement callableStatement=null;
        try {
            //获得数据库连接对象
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);
            //写sql语句
            String updateCard_sql = "UPDATE card SET password=? WHERE id =?";
            //在该连接上创建预编译语句对象
            preparedStatement = connection.prepareStatement(updateCard_sql);
            //为预编译参数赋值
            preparedStatement.setString(1, card.getPassword());
            preparedStatement.setInt(2,card.getId());
            affectedRows=preparedStatement.executeUpdate();
            //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
            callableStatement=connection.prepareCall("{CALL AD(?,?,?)}");
            callableStatement.setInt(1,rows);
            callableStatement.setString(2,"用户修改密码");
            callableStatement.setInt(3,card.getId());
            callableStatement.execute();
            connection.commit();
        }catch (SQLException e) {
            System.out.println(e.getMessage()+"\n errorCode="+e.getErrorCode());
            e.printStackTrace();
            try{
                if (connection!=null){
                    connection.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            } finally {
                try{
                    if(connection!=null){
                        connection.setAutoCommit(true);
                    }
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
                JdbcHelper.close(preparedStatement,connection);
            }}
        return affectedRows >0;
    }
    // 张欣茹 201802104009
    public Card findByLogin(String cno, String password)  throws SQLException {
        Card card = null;
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String cardMoney_sql = "SELECT * FROM card WHERE cno = ? and password = ?";
        //再该连接上创建预编译语句
        PreparedStatement preparedStatement = connection.prepareStatement(cardMoney_sql);
        preparedStatement.setString(1, cno);
        preparedStatement.setString(2,password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            card  = new Card(resultSet.getInt("id"),
                    resultSet.getString("cno"),
                    resultSet.getString("status"),
                    resultSet.getFloat("money"),
                    resultSet.getString("password"),
                    StudentService.getInstance().findByStudent(resultSet.getString("student_sno")));
        }
        return card;
    }
}
