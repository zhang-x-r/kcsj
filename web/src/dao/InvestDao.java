//谢凤蒙201802104013
package dao;
import domain.Card;
import service.InvestStudentService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;
public class InvestDao {
  private static InvestDao investDao = new InvestDao();

  private InvestDao() {
  }

  private static Card card = null;
  private static int rows = 9;

  public static InvestDao getInstance() {
    return investDao;
  }

  public Collection<Card> findBySno(String sno) throws SQLException {
    Collection<Card> cards = new TreeSet<Card>();
    Connection connection = JdbcHelper.getConn();
    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM card WHERE student_sno=?");
    preparedStatement.setString(1, sno);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      Card card = new Card(
              resultSet.getInt("id"),
              resultSet.getString("cno"),
              resultSet.getString("status"),
              resultSet.getFloat("money"),
              resultSet.getString("password"),
              InvestStudentService.getInstance().find(resultSet.getString("student_sno"))
      );
      cards.add(card);
    }
    JdbcHelper.close(resultSet, preparedStatement, connection);

    return cards;
  }

  public Card find(Card cardMoney) throws SQLException {
    Connection connection = JdbcHelper.getConn();
    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM card WHERE student_sno=? AND status=?");
    preparedStatement.setString(1, cardMoney.getStudent().getSno());
    preparedStatement.setString(2, "正常");
    ResultSet resultSet = preparedStatement.executeQuery();
    if (resultSet.next()) {
      card = new Card(
              resultSet.getInt("id"),
              resultSet.getString("cno"),
              resultSet.getString("status"),
              resultSet.getFloat("money"),
              resultSet.getString("password"),
              InvestStudentService.getInstance().find(resultSet.getString("student_sno"))
      );
    }
    return card;
  }

  public boolean invest(Card cardMoney) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    CallableStatement callableStatement = null;
    int affectedRowNum = 0;
    try {
      connection = JdbcHelper.getConn();
      //设置自动提交为false,事务开始
      connection.setAutoCommit(false);
      preparedStatement = connection.prepareStatement("update card set money=? where student_sno=? and status=?");
      preparedStatement.setFloat(1, card.getMoney() + cardMoney.getMoney());
      preparedStatement.setString(2, cardMoney.getStudent().getSno());
      preparedStatement.setString(3, "正常");
      affectedRowNum= preparedStatement.executeUpdate();

      callableStatement = connection.prepareCall("{CALL sp_details(?,?,?,?)}");
      callableStatement.setInt(1, rows);
      callableStatement.setString(2, "管理员给卡充值" );
      callableStatement.setInt(3, card.getId());
      callableStatement.setFloat(4,cardMoney.getMoney());
      callableStatement.execute();

      connection.commit();
    } catch (SQLException e) {
      e.printStackTrace();
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
    System.out.println("完成"+affectedRowNum+"次充值操作");
    if (callableStatement!=null){
      callableStatement.close();
    }
    JdbcHelper.close(preparedStatement,connection);
    return affectedRowNum>0;
  }

}
