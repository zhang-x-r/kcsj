package dao;

import domain.Card;
import domain.Student;
import service.MakeUpStudentService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public class MakeUpCardDao {
  private static MakeUpCardDao makeUpCardDao=new MakeUpCardDao();
  private MakeUpCardDao() {
  }
  private static Collection<Card> cards;
  private static Card card=null;
  private static  Card card2=null;
  private  static int rows=9;
  public static MakeUpCardDao getInstance(){
    return makeUpCardDao;
  }
  public void setRows(int number){
    rows=number;
  }
  public Collection<Card> findBySno(String sno)throws SQLException {
    cards = new TreeSet<Card>();
    Connection connection= JdbcHelper.getConn();
    PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM card WHERE student_sno=?");
    preparedStatement.setString(1,sno);
    ResultSet resultSet=preparedStatement.executeQuery();
    while(resultSet.next()){
      Card card=new Card(
              resultSet.getInt("id"),
              resultSet.getString("cno"),
              resultSet.getString("status"),
              resultSet.getFloat("money"),
              resultSet.getString("password"),
              MakeUpStudentService.getInstance().find(resultSet.getString("student_sno"))
      );
      cards.add(card);
    }
    JdbcHelper.close(resultSet,preparedStatement,connection);

    return cards;
  }
  public Card lastCard(){
    if (card!=null){
      card2=card;
    }else {
      int id=0;
      for (Card card1:cards){
        if (card1.getId()>id){
          card2=card1;
        }
      }
    }
    return card2;
  }
  public Card findById(int id)throws SQLException{
    Connection connection= JdbcHelper.getConn();
    PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM card WHERE id=?");
    preparedStatement.setInt(1,id);
    ResultSet resultSet=preparedStatement.executeQuery();
    Card card=null;
    if(resultSet.next()){
      card=new Card(
              resultSet.getInt("id"),
              resultSet.getString("cno"),
              resultSet.getString("status"),
              resultSet.getFloat("money"),
              resultSet.getString("password"),
              MakeUpStudentService.getInstance().find(resultSet.getString("student_sno"))
      );
    }
    JdbcHelper.close(resultSet,preparedStatement,connection);
    return card;
  }
  public Card find(Student student)throws SQLException{
    Connection connection=JdbcHelper.getConn();
    PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM card WHERE student_sno=? AND status=?");
    preparedStatement.setString(1,student.getSno());
    preparedStatement.setString(2,"正常");
    ResultSet resultSet=preparedStatement.executeQuery();
    if (resultSet.next()){
      card=new Card(
              resultSet.getInt("id"),
              resultSet.getString("cno"),
              resultSet.getString("status"),
              resultSet.getFloat("money"),
              resultSet.getString("password"),
              MakeUpStudentService.getInstance().find(resultSet.getString("student_sno"))
      );
    }
    return card;
  }
  public boolean makeUpCard(Student student)throws SQLException{
    Connection connection=null;
    PreparedStatement preparedStatement=null;
    CallableStatement callableStatement=null;
    int affectedRowNum = 0;
    try{
      connection=JdbcHelper.getConn();
      //设置自动提交为false,事务开始
      connection.setAutoCommit(false);
      if (card!=null) {
        preparedStatement = connection.prepareStatement("UPDATE card SET status=? WHERE student_sno=? AND status=?");
        preparedStatement.setString(1, "挂失");
        preparedStatement.setString(2, student.getSno());
        preparedStatement.setString(3, "正常");
        preparedStatement.executeUpdate();

        callableStatement=connection.prepareCall("{CALL AD(?,?,?)}");
        callableStatement.setInt(1,rows);
        callableStatement.setString(2,"挂失时修改原卡状态");
        callableStatement.setInt(3,card2.getId());
        callableStatement.execute();
      }

      preparedStatement=connection.prepareStatement("INSERT INTO card(status,money,password,student_sno,cno) values (?,?,?,?,?)");
      preparedStatement.setString(1,"正常");
      preparedStatement.setFloat(2,card2.getMoney());
      preparedStatement.setString(3,card2.getPassword());
      preparedStatement.setString(4,student.getSno());
      preparedStatement.setString(5,card2.getCno());
      affectedRowNum=preparedStatement.executeUpdate();

      //提交
      connection.commit();
    }catch (SQLException e){
      e.printStackTrace();
      try{
        //回滚当前连接所做的操作
        if (connection != null) {
          connection.rollback();
        }
      }catch (SQLException e1){
        e1.printStackTrace();
      }finally {
        try{
          if (connection != null) {
            connection.setAutoCommit(true);
          }
        }catch (SQLException e2){
          e2.printStackTrace();
        }
      }
    }

    System.out.println("完成"+affectedRowNum+"次补卡操作");
    if (callableStatement!=null){
      callableStatement.close();
    }
    JdbcHelper.close(preparedStatement,connection);
    return affectedRowNum>0;
  }

}
