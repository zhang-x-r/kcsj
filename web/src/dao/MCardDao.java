package dao;

import domain.Card;
import domain.Record;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

// 漆风玲 201802104005
public class MCardDao {
    private static MCardDao cardDao =new MCardDao();
    private MCardDao(){};
    private  static int rows=9;
    public static MCardDao getInstance(){return cardDao;}

    public Collection<Card> findAll() throws SQLException {
            Collection<Card> cards =new TreeSet<>();
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        ResultSet resultSet =statement.executeQuery("select * from card");
        while (resultSet.next()){
             Card card =new Card(resultSet.getInt("id"),
                    resultSet.getString("cno"),
                    resultSet.getString("status"),
                    resultSet.getFloat("money"),
                    resultSet.getString("password"),
                    StudentDao.getInstance().findByStudent(resultSet.getString("student_sno")));
            cards.add(card);
        }
        System.out.println(cards);
        JdbcHelper.close(resultSet,statement,connection);
        return cards;
    }
    public Card findById(Integer id) throws SQLException {
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
    public static Collection<Card> find(String cno)throws SQLException{
        Collection<Card> cards = new HashSet<>();
        int id = 0;
        Card card =null;
        Connection connection = JdbcHelper.getConn();
        String findCard ="select * from card where cno = ?";
        PreparedStatement preparedStatement =connection.prepareStatement(findCard);
        preparedStatement.setString(1,cno);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
             card =new Card(resultSet.getInt("id"),
                     resultSet.getString("cno"),
                    resultSet.getString("status"),
                    resultSet.getFloat("money"),
                    resultSet.getString("password"),
                    StudentDao.getInstance().findByStudent(resultSet.getString("student_sno")));
             cards.add(card);
        }
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return cards;
    }

    public boolean update(Card card) throws SQLException{
        String change_sql =null;
        Connection connection =null;
        PreparedStatement preparedStatement =null;
        CallableStatement callableStatement=null;
        int affectedRowNum =0;
        //int cardId =0;
        Record reCord =null;
        try {
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);
            change_sql = "update card set password =?,status=? where id =?";
            preparedStatement = connection.prepareStatement(change_sql);
            preparedStatement.setString(1, card.getPassword());
            preparedStatement.setString(2, card.getStatus());
            preparedStatement.setInt(3, card.getId());
            affectedRowNum = preparedStatement.executeUpdate();

            callableStatement=connection.prepareCall("{CALL AD(?,?,?)}");
            callableStatement.setInt(1,rows);
            callableStatement.setString(2,"管理员修改");
            callableStatement.setInt(3,card.getId());
            callableStatement.execute();
            connection.commit();
        }
        catch (SQLException e){
            e.printStackTrace();
            try{
                if(connection !=null){
                    connection.rollback();
                }
            }
            catch (SQLException e1){
                e1.printStackTrace();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            try{
                if(connection !=null){
                    connection.rollback();
                }
            }
            catch (SQLException e1){
                e1.printStackTrace();
            }

        }
        finally {
            try{
                if(connection !=null){
                    connection.setAutoCommit(true);
                }
            }
            catch ( SQLException e){
                e.printStackTrace();
            }
            JdbcHelper.close(preparedStatement,connection);
        }
        return affectedRowNum>0;
    }
    public  boolean delete(String cno){
        //声明连接和预编译对象
        Connection connection = null;
        PreparedStatement pstmt = null;
        int affectedRowNum =0;
        try {
            //给连接对象赋值
            connection = JdbcHelper.getConn();
            //关闭自动提交(事件开始）
            connection.setAutoCommit(false);
            //sql语句
            String deleteUser_sql = "delete from card where cno= ?";
            //给预编译赋值
            pstmt = connection.prepareStatement(deleteUser_sql);
            //赋值
            pstmt.setString(1,cno);
            //执行返回影响行数
            affectedRowNum = pstmt.executeUpdate();
            System.out.println("删除了 " + affectedRowNum +" 行记录");
            String deleteStudent_sql ="delete from student where sno =?";
            pstmt=connection.prepareStatement(deleteStudent_sql);
            pstmt.setString(1,cno);
            affectedRowNum =pstmt.executeUpdate();
            System.out.println("删除了 " + affectedRowNum +" 行记录");
        } catch (SQLException e) {
            //获取错误信息
            System.out.println(e.getMessage() + "\nerrorCode = " + e.getErrorCode());
            try {
                if (connection != null){
                    connection.rollback();
                }
            } catch (SQLException e1){
                e.printStackTrace();
            }
        } finally {
            //恢复自动提交
            try {
                if (connection != null){
                    //恢复自动提交
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //关闭资源
            JdbcHelper.close(pstmt,connection);
        }
        return affectedRowNum >0;
    }

}
