package service;

import dao.MRecordDao;
import domain.Card;
import domain.Record;

import java.sql.SQLException;
import java.util.Collection;

public class MRecordService {
  private static MRecordDao mRecordDao
    = MRecordDao.getInstance();
  private static MRecordService mRecordService
    =new MRecordService();
  private MRecordService(){}
  public static MRecordService getInstance(){
    return mRecordService;
  }
  public Collection<Record> findAll()throws SQLException {
    return mRecordDao.findAll();
  }
public Collection<Record> findBySno()throws SQLException{
    return mRecordDao.findBySno();
}
public Collection<Card> findCardBySno(String sno)throws SQLException{
    return mRecordDao.findCardBySno(sno);
}
}
