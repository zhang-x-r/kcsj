package dao;

import domain.Student;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public class StudentDao {
    private static StudentDao studentDao = new StudentDao();
    private StudentDao() {
    }
    public static StudentDao getInstance() {
        return studentDao;
    }
    //漆风玲 201802104005
    public Collection<Student> findAll()  {
        Collection<Student> students = new HashSet<Student>();
       try {
        //创建连接对象
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select  * from student");
        //如果结果有下一条，就执行循环语句
        while (resultSet.next()) {
            //以当前的字段为参数创建Student对象
            Student student = new Student(resultSet.getString("sno"),
                    resultSet.getString("sname"),
                    resultSet.getString("sex"));
            //向集合中增加对象
            students.add(student);
        }
        //关闭资源
        JdbcHelper.close(resultSet, statement, connection);}
        catch (Exception e){
           e.printStackTrace();

        }
        return students;
    }
    // 李美青 201802104008
    public Student findByStudent(String sno) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String findStudent_sql = "SELECT * FROM student WHERE sno=?";
        PreparedStatement preparedStatement = connection.prepareStatement(findStudent_sql);
        preparedStatement.setString(1, sno);
        ResultSet resultSet = preparedStatement.executeQuery();
        Student student = null;
        if (resultSet.next()) {
            student = new Student(resultSet.getString("sno"),
                    resultSet.getString("sname"),
                    resultSet.getString("sex"));
        }
        JdbcHelper.close(resultSet, preparedStatement, connection);
        return student;
    }
    //漆风玲 201802104005
    public boolean add(Student student) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement =null;
        int affectedRowNum =0;
        try {
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);
            String addStudent_sql = "insert into student (sno,sname,sex) values" + "(?,?,?)";
            preparedStatement = connection.prepareStatement(addStudent_sql);
            preparedStatement.setString(1, student.getSno());
            preparedStatement.setString(2, student.getSname());
            preparedStatement.setString(3, student.getSex());
            affectedRowNum = preparedStatement.executeUpdate();
            String addCard_sql = "insert into card (cno,status,money,password,student_sno) values" + "(?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(addCard_sql);
            preparedStatement.setString(1, student.getSno());
            preparedStatement.setString(2, "正常");
            preparedStatement.setFloat(3, 0);
            preparedStatement.setString(4, student.getSno());
            preparedStatement.setString(5, student.getSno());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            try {
                if (connection != null) ;
                {
                    connection.rollback();
                }
            } catch (Exception e1) {
                e1.getStackTrace();
            }
        }
        finally {
            try{
                if(connection !=null){
                    connection.setAutoCommit(true);
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            JdbcHelper.close(preparedStatement,connection);
        }
            return affectedRowNum>0;
    }


}

