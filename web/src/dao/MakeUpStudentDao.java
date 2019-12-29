package dao;

import domain.Student;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MakeUpStudentDao {
  private static MakeUpStudentDao makeUpStudentDao =new MakeUpStudentDao();
  public static MakeUpStudentDao getInstance(){
    return makeUpStudentDao;
  }
  //sno作为参数，返回Student
  public Student find(String sno)throws SQLException {
    Connection connection= JdbcHelper.getConn();
    PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM student WHERE sno=?");
    preparedStatement.setString(1,sno);
    ResultSet resultSet=preparedStatement.executeQuery();
    Student student=null;
    if (resultSet.next()){
      student=new Student(
              resultSet.getString("sno"),
              resultSet.getString("sname"),
              resultSet.getString("sex")
      );
    }
    JdbcHelper.close(resultSet,preparedStatement,connection);
    return student;
  }

}
