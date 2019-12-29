package dao;

import domain.Card;
import domain.Record;
import service.MakeUpCardService;
import service.MakeUpStudentService;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.TreeSet;

public class MRecordDao {
  private static MRecordDao MRecordDao =new MRecordDao();
  private static Collection<Card> cards;
  private MRecordDao() {
  }
  public static MRecordDao getInstance(){
    return MRecordDao;
  }
  public Collection<Card> findCardBySno(String sno)throws SQLException {
    cards=new TreeSet<>();
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
  public Collection<Record> findBySno()throws SQLException{
    Collection<Record> records=new TreeSet<Record>();
    Connection connection=JdbcHelper.getConn();

    ResultSet resultSet=null;
    Record record=null;
    PreparedStatement preparedStatement=connection.prepareStatement("select * from record where card_id=?");
    for (Card card:cards){
      preparedStatement.setInt(1,card.getId());
      resultSet=preparedStatement.executeQuery();
      while (resultSet.next()){
        record=new Record(resultSet.getInt("id"),
          resultSet.getString("time"),
          resultSet.getString("description"),
          MakeUpCardService.getInstance().findById(resultSet.getInt("card_id")));
        records.add(record);
      }
    }
    return records;
  }
  public Collection<Record> findAll() throws SQLException{
    Collection<Record> records=new TreeSet<Record>();
    Connection connection=JdbcHelper.getConn();
    PreparedStatement preparedStatement=connection.prepareStatement("select * from record");
    ResultSet resultSet=preparedStatement.executeQuery();
    Record record=null;
    while (resultSet.next()){
      record=new Record(
        resultSet.getInt("id"),
        resultSet.getString("time"),
        resultSet.getString("description"),
        MakeUpCardService.getInstance().findById(resultSet.getInt("card_id"))
      );
      records.add(record);
    }
    return records;
  }
}
