package service;

import dao.InvestStudentDao;
import domain.Student;

import java.sql.SQLException;

public class InvestStudentService {
  private static InvestStudentDao studentDao= InvestStudentDao.getInstance();
  private static InvestStudentService studentService=new InvestStudentService();
  public static InvestStudentService getInstance(){
    return studentService;
  }
  public Student find(String sno)throws SQLException {
    return studentDao.find(sno);
  }
}
