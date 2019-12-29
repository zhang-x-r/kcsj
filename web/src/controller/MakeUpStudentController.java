package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.Student;
import service.MakeUpStudentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/slogin.ctl")
public class MakeUpStudentController extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String student_json= request.getParameter("sno");
    JSONObject message=new JSONObject();
    try{
      Student student1= MakeUpStudentService.getInstance().find(student_json);
      if (student1==null){
        message.put("message","该用户不存在");
        response.getWriter().println(message);
      }else {
        response.getWriter().println(JSON.toJSONString(student1));
      }
    }catch (SQLException e){
      message.put("message","数据库异常");
      response.getWriter().println(message);
    }catch (Exception e){
      message.put("message","网络异常");
      response.getWriter().println(message);
    }

  }
}


