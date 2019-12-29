package dao;

import domain.BuyEle;
import domain.Dormitory;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
//张欣茹 201802014009
public class ElectricityDao {
    private static ElectricityDao electricityDao = new ElectricityDao();
    public static ElectricityDao getInstance(){return electricityDao;}
    private  static int rows=9;
    public Collection<Dormitory> findAll() throws SQLException {
        //创建集合
        Collection<Dormitory> electricities = new HashSet<>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //在该连接对象上创建普通编译对象
        Statement statement = connection.createStatement();
        //执行返回结果集
        ResultSet resultSet = statement.executeQuery("SELECT id,dno,remain_ele FROM dormitory ");
        //遍历结果集
        while(resultSet.next()){
            Dormitory electricity = new Dormitory(resultSet.getInt("id"),
                    resultSet.getInt("dno"),
                    resultSet.getFloat("remain_ele"));
            electricities.add(electricity);
        }
        //关闭资源
        JdbcHelper.close(statement,connection);
        //返回集合
        return electricities;
    }
    public Dormitory find(int dno) throws SQLException{
        //定义一个Dormitory类型的变量
        Dormitory electricity = null;
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //写SQL语句
        String findElec_sql = "SELECT id,dno,remain_ele FROM dormitory WHERE dno = ? ";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(findElec_sql);
        //给预编译语句赋值
        preparedStatement.setInt(1,dno);
        //执行预编译语句
        ResultSet resultSet = preparedStatement.executeQuery();
        //获得结果集
        if (resultSet.next()){
            electricity = new Dormitory(resultSet.getInt("id"),
                    resultSet.getInt("dno"),
                    resultSet.getFloat("remain_ele"));
        }
        //返回
        return  electricity;
    }
    public Dormitory findById(int id) throws SQLException {
        //定义一个Dormitory类型的变量
        Dormitory electricity = null;
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //写SQL语句
        String findElec_sql = "SELECT id,dno,remain_ele FROM dormitory WHERE id = ?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(findElec_sql);
        //给预编译语句赋值
        preparedStatement.setInt(1, id);
        //执行预编译语句
        ResultSet resultSet = preparedStatement.executeQuery();
        //获得结果集
        if (resultSet.next()) {
            electricity = new Dormitory(resultSet.getInt("id"),
                    resultSet.getInt("dno"),
                    resultSet.getFloat("remain_ele"));
        }
        //返回
        return electricity;
    }
    public boolean updateElectricity(BuyEle buyEle) throws SQLException {
        //声明连接对象和预编译语句对象
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        CallableStatement callableStatement = null;
        int affectedRowNum = 0;
        int cardId = 0;
        float remainMoney = findById(buyEle.getId()).getRemainEle();
        float cardMoney = CardDao.getInstance().findByLogin(buyEle.getCno(),buyEle.getPassword()).getMoney();
//        String status = CardDao.getInstance().findByLogin(buyEle.getCno(),buyEle.getPassword()).getStatus();
//        if(status.equals("正常")) {
            try {
                //为连接对象赋值
                connection = JdbcHelper.getConn();
                //关闭自动提交(事件开始）
                connection.setAutoCommit(false);
                //写SQL语句
                String addElectricity_sql = "UPDATE dormitory SET remain_ele = ?  where id =? ";
                //给预编译语句对象赋值
                preparedStatement = connection.prepareStatement(addElectricity_sql);
                //为预编译语句对象赋值
                preparedStatement.setFloat(1, buyEle.getMoney() + remainMoney);
                preparedStatement.setInt(2, buyEle.getId());
                //执行返回影响行数
                affectedRowNum = preparedStatement.executeUpdate();
                System.out.println("修改了 " + affectedRowNum + " 行记录");

                callableStatement=connection.prepareCall("{CALL sp_payment(?,?,?,?)}");
                callableStatement.setInt(1,rows);
                callableStatement.setString(2, "电费充值");
                callableStatement.setInt(3, buyEle.getId());
                callableStatement.setFloat(4, buyEle.getMoney());
                callableStatement.execute();

                String card_sql = "UPDATE card set money = ? where cno = ? and password = ?";
                preparedStatement = connection.prepareStatement(card_sql);
                preparedStatement.setFloat(1, cardMoney - buyEle.getMoney());
                preparedStatement.setString(2, buyEle.getCno());
                preparedStatement.setString(3, buyEle.getPassword());
                affectedRowNum = preparedStatement.executeUpdate();
                System.out.println("修改了 " + affectedRowNum + " 行记录");
                String cardId_sql = "SELECT * from card where cno = ? ";
                preparedStatement = connection.prepareStatement(cardId_sql);
                preparedStatement.setString(1, buyEle.getCno());
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    cardId = resultSet.getInt("id");
                }

                callableStatement=connection.prepareCall("{CALL sp_details(?,?,?,?)}");
                callableStatement.setInt(1,rows);
                callableStatement.setString(2, "电费");
                callableStatement.setInt(3, cardId);
                callableStatement.setFloat(4, 0-buyEle.getMoney());
                callableStatement.execute();


                //提交当前连接所做的操作（事件以提交结束）
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    //回滚当前连接所作的操作
                    if (connection != null) {
                        //事件以回滚结束
                        connection.rollback();
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    //回滚当前连接所作的操作
                    if (connection != null) {
                        //事件以回滚结束
                        connection.rollback();
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } finally {
                try {
                    //恢复自动提交
                    if (connection != null) {
                        connection.setAutoCommit(true);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
       // }
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return affectedRowNum>0;

    }

}
