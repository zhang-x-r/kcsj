package service;

import dao.MakeUpStudentDao;
import domain.Student;

import java.sql.SQLException;

public class MakeUpStudentService {
  private static MakeUpStudentDao makeUpStudentDao = MakeUpStudentDao.getInstance();
  private static MakeUpStudentService makeUpStudentService =new MakeUpStudentService();
  public static MakeUpStudentService getInstance(){
    return makeUpStudentService;
  }
  public Student find(String sno)throws SQLException {
    return makeUpStudentDao.find(sno);
  }
}
