package service;

import dao.StudentDao;
import domain.Student;

import java.sql.SQLException;
import java.util.Collection;

public class StudentService {
    private static StudentDao studentDao = StudentDao.getInstance();
    private static StudentService studentService = new StudentService();
    public static StudentService getInstance() {
        return studentService;
    }

    public Student findByStudent(String sno) throws SQLException{
        return studentDao.findByStudent(sno);
    }
    public Collection<Student> findAll() throws SQLException{
        return studentDao.findAll();
    }
    public boolean add(Student student) throws SQLException{
        return studentDao.add(student);
    }
}
