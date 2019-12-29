package service;

import dao.MakeUpCardDao;
import domain.Card;
import domain.Student;

import java.sql.SQLException;
import java.util.Collection;

public class MakeUpCardService {
  private static MakeUpCardDao makeUpCardDao
          = MakeUpCardDao.getInstance();
  private static MakeUpCardService makeUpCardService
          =new MakeUpCardService();
  private MakeUpCardService(){}

  public static MakeUpCardService getInstance(){
    return makeUpCardService;
  }
  public Collection<Card> findBySno(String sno)throws SQLException {
    return makeUpCardDao.findBySno(sno);
  }
  public Card lastCard(){
    return makeUpCardDao.lastCard();
  }
  public void setRows(int number){
    makeUpCardDao.setRows(number);
  }
  public boolean makeUpCard(Student student)throws SQLException {
    return makeUpCardDao.makeUpCard(student);
  }
  public Card findById(int id)throws SQLException{
    return makeUpCardDao.findById(id);
  }
  public Card find(Student student)throws SQLException{
    return makeUpCardDao.find(student);
  }
}
