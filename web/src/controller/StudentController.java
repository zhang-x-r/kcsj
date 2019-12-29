package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.StudentDao;
import domain.Student;
import service.StudentService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/student.ctl")
public class StudentController extends HttpServlet {
    private void responseStudent(String sno, HttpServletResponse response) throws SQLException, IOException {
        Student student = StudentService.getInstance().findByStudent(sno);
        String student_json = JSON.toJSONString(student);
        response.getWriter().println(student_json);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String student_json = JSONUtil.getJSON(request);
        System.out.println(student_json);
        //将JSON字串解析为Student对象
        Student studentToAdd = JSON.parseObject(student_json, Student.class);;
        //创建JSON对象
        System.out.println(studentToAdd);
        JSONObject message = new JSONObject();
        //在数据库表中增加Teacher对象
        try {

            if( StudentService.getInstance().add(studentToAdd)==true) {
                message.put("message", "增加成功");
            }
            else {message.put("message","用户已存在");}
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "网络异常");
        }
        response.getWriter().println(message);
    }
}
