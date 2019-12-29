package dao;

import domain.Record;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public class RecordDao {
    private static RecordDao recordDao = new RecordDao();

    private RecordDao() {
    }
    public static RecordDao getInstance() {
        return recordDao;
    }
    // 李美青 201802104008
    public Collection<Record> findByCard(String cno) throws SQLException {
        Collection<Record> records = new TreeSet<Record>();
        int cardID = 0;
        //声明一个Record类型的变量
        Record record = null;
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String findCardId = "SELECT * FROM card WHERE cno = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(findCardId);
        preparedStatement.setString(1,cno);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            cardID = resultSet.getInt("id");
        }
        String findRecordByCard_sql = "SELECT * FROM record WHERE card_id=?";
        //在该连接上创建预编译语句对象
        preparedStatement = connection.prepareStatement(findRecordByCard_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1, cardID);
        //执行预编译语句
        resultSet = preparedStatement.executeQuery();
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Card对象
        //若结果集中没有记录，则本方法返回null
        while (resultSet.next()) {
            record = new Record(resultSet.getInt("id"),
                    resultSet.getString("time"),
                    resultSet.getString("description"),
                    CardDao.getInstance().find(resultSet.getInt("card_id")));
            records.add(record);
        }
        //关闭资源
        JdbcHelper.close(resultSet, preparedStatement, connection);
        return records;
    }
//    // 张欣茹 201802104009
//    public Collection<Record> findAll() throws SQLException{
//        //创建一个集合
//        Collection<Record> records = new TreeSet<>();
//        //声明一个Record类型的变量
//        Record record = null;
//        //获得连接对象
//        Connection connection = JdbcHelper.getConn();
//        //创建普通编译语句
//        Statement statement = connection.createStatement();
//        //执行预编译语句,获得结果集
//        ResultSet resultSet = statement.executeQuery("SELECT * FROM record");
//        //遍历结果集
//        while (resultSet.next()){
//            record = new Record(resultSet.getInt("id"),
//                    resultSet.getString("time"),
//                    resultSet.getString("description"),
//                    CardDao.getInstance().find(resultSet.getInt("card_id")));
//            records.add(record);
//        }
//        //关闭资源
//        JdbcHelper.close(statement,connection);
//        return records;
//    }
}
