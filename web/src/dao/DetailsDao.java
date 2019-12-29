package dao;

import domain.Details;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public class DetailsDao {
    private static DetailsDao detailsDao = new DetailsDao();
    public static DetailsDao getInstance() {
        return detailsDao;
    }
    private DetailsDao() { }

    public Collection<Details> find(String cno)throws SQLException {
        Collection<Details> details = new TreeSet<>();
        Details detail = null;
        int cardId = 0;
        Connection connection = JdbcHelper.getConn();
        String findCardId = "SELECT * FROM card WHERE cno = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(findCardId);
        //给预编译语句赋值
        preparedStatement.setString(1,cno);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            cardId = resultSet.getInt("id");
        }
        String findDetails_sql = "SELECT * FROM details WHERE card_id = ?";
        preparedStatement = connection.prepareStatement(findDetails_sql);
        preparedStatement.setInt(1, cardId);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            detail= new Details(resultSet.getInt("id"),
                    resultSet.getString("detail"),
                    CardDao.getInstance().find(resultSet.getInt("card_id")),
                    resultSet.getFloat("money"),
                    resultSet.getString("time")
            );
            details.add(detail);
        }
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return details;
    }

    public Collection<Details> findAll() throws SQLException {
        Collection<Details> details = new HashSet<>();
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from details");
        while(resultSet.next()){
            Details detail= new Details(resultSet.getInt("id"),
                            resultSet.getString("detail"),
                            CardDao.getInstance().find(resultSet.getInt("card_id")),
                            resultSet.getFloat("money"),
                    resultSet.getString("time"));
            details.add(detail);
        }
        JdbcHelper.close(resultSet,statement,connection);
        return details;
    }
}

                 