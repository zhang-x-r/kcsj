package dao;
import domain.BuyWater;
import domain.Dormitory;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

// 蒋羽 201802104010
public class WaterDao {
    //创建一个私有的静态对象
    private static WaterDao waterDao =
            new WaterDao();

    private WaterDao() {
    }
    private  static int rows=9;

    public static WaterDao getInstance() {
        return waterDao;
    }

    public Collection findAllByDormitory() throws SQLException {
         Collection dormitory_waters = new TreeSet();
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建一个语句盒子对象
        Statement stmt = connection.createStatement();
        //获得结果集
        ResultSet resultSet = stmt.executeQuery("select id,dno,remain_water from dormitory");
        while (resultSet.next()) {
            Dormitory dormitory_water = new Dormitory(resultSet.getInt("id"),
                    resultSet.getFloat("remain_water"),
                    resultSet.getInt("dno")

            );
            dormitory_waters.add(dormitory_water);
        }
        //关闭语句盒子对象及连接对象
        JdbcHelper.close(stmt, connection);
        return dormitory_waters;
    }

    /**
     * 根据id返回对应Dormitory
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Dormitory find(Integer id) throws SQLException {
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //创建预编译语句对象，对sql语句进行包装编译
        PreparedStatement preparedStatement = connection.prepareStatement("select id,dno,remain_water from dormitory where id=?");
        //为参数赋值
        preparedStatement.setInt(1, id);
        //执行预编译语句的executeQuery方法
        ResultSet resultSet = preparedStatement.executeQuery();
        Dormitory desiredDormitory = null;
        if (resultSet.next()) {
            desiredDormitory = new Dormitory(resultSet.getInt("id"),
                    resultSet.getFloat("remain_water"),
                    resultSet.getInt("dno")
            );
        }
        //关闭preparedStatement,connection对象
        JdbcHelper.close(preparedStatement, connection);
        return desiredDormitory;
    }

    public Dormitory findDormitoryByDno(Integer dno) throws SQLException {
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //创建预编译语句对象，对sql语句进行包装编译
        PreparedStatement preparedStatement = connection.prepareStatement("select id,dno,remain_water from dormitory where dno=? ");
        //为参数赋值
        preparedStatement.setInt(1, dno);
        //执行预编译语句的executeQuery方法
        ResultSet resultSet = preparedStatement.executeQuery();
        Dormitory desiredDormitory = null;
        if (resultSet.next()) {
            desiredDormitory = new Dormitory(resultSet.getInt("id"),
                    resultSet.getFloat("remain_water"),
                    resultSet.getInt("dno")
            );
        }
        //关闭preparedStatement,connection对象
        JdbcHelper.close(preparedStatement, connection);
        return desiredDormitory;
    }

    public boolean update(BuyWater buyWater) throws SQLException {
        //获得连接对象
        Connection connection = null;
        PreparedStatement pstmt =null;
        Statement statement = null;
        int affectedRowNum =0;
        CallableStatement callableStatement=null;
//        String status = CardDao.getInstance().findByLogin(buyWater.getCno(),buyWater.getPassword()).getStatus();
//        if(status.equals("正常")) {
            try {
                connection = JdbcHelper.getConn();
                //设置自动提交为false,事务开始
                connection.setAutoCommit(false);
                String cardId = "SELECT id,money from card where cno = ? and password = ?";
                pstmt = connection.prepareStatement(cardId);
                pstmt.setString(1, buyWater.getCno());
                pstmt.setString(2, buyWater.getPassword());
                ResultSet resultSet = pstmt.executeQuery();
                int cardId1 = 0;
                float cardMoney = 0;
                while (resultSet.next()) {
                    cardId1 = resultSet.getInt("id");
                    cardMoney = resultSet.getFloat("money");
                }

                String dormitory_id = "select id,remain_water from dormitory where dno= ? ";
                pstmt = connection.prepareStatement(dormitory_id);
                pstmt.setInt(1, buyWater.getDno());
                ResultSet resultSet1 = pstmt.executeQuery();
                int id = 0;
                float remain_water = 0;
                while (resultSet1.next()) {
                    id = resultSet1.getInt("id");
                    remain_water = resultSet1.getFloat("remain_water");
                }

                //创建sql语句
                String updateWater_sql = "UPDATE dormitory set remain_water= ? where dno =? ";
                //在该连接上创建预编译语句对象，对sql语句进行包装编译
                pstmt = connection.prepareStatement(updateWater_sql);
                //为第一个参数赋值
                pstmt.setFloat(1, remain_water + buyWater.getMoney());
                pstmt.setInt(2, buyWater.getDno());
                //执行sql语句，并记录修改记录条数
                affectedRowNum = pstmt.executeUpdate();
                System.out.println("改变了" + affectedRowNum + "条记录");

                callableStatement =connection.prepareCall("{CALL sp_payment(?,?,?,?)}");
                callableStatement.setInt(1,rows);
                callableStatement.setString(2, "水费充值");
                callableStatement.setInt(3, id);
                callableStatement.setFloat(4,buyWater.getMoney());
                callableStatement.execute();

                String updateCard_sql = "UPDATE card set  money =? where cno =?and password =?";
                pstmt = connection.prepareStatement(updateCard_sql);
                pstmt.setFloat(1, cardMoney - buyWater.getMoney());
                pstmt.setString(2, buyWater.getCno());
                pstmt.setString(3 , buyWater.getPassword());
                affectedRowNum = pstmt.executeUpdate();
                System.out.println("改变了" + affectedRowNum + "条记录");



                callableStatement=connection.prepareCall("{CALL sp_details(?,?,?,?)}");
                callableStatement.setInt(1,rows);
                callableStatement.setString(2, "水费充值");
                callableStatement.setInt(3, cardId1);
                callableStatement.setFloat(4, 0-buyWater.getMoney());
                callableStatement.execute();


                connection.commit();

            } catch (SQLException e) {
                System.out.println(e.getMessage() + "\n errorCode=" + e.getErrorCode());
                try {
                    //回滚当前连接所做的操作
                    if (connection != null) {
                        connection.rollback();
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } finally {
                    try {
                        if (connection != null) {
                            connection.setAutoCommit(true);
                        }
                    } catch (SQLException e2) {
                        e2.printStackTrace();
                    }

                }
            }
        //关闭psmt,connection对象
        JdbcHelper.close(pstmt, connection);
            return affectedRowNum>0;
    }


}

