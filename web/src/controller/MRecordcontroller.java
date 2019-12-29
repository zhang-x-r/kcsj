package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.Record;
import service.MRecordService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/MRecord.ctl")
public class MRecordcontroller extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     String sno_json= request.getParameter("sno");
     JSONObject message=new JSONObject();
     try {
     if (sno_json==null){
         responseRecord(response);
     }else {
       responseRecord(sno_json,response);
     }
       }catch (SQLException e){
       e.printStackTrace();
         message.put("message","数据库操作异常");
         response.getWriter().println(message);
       }catch (Exception e){
       message.put("message","网络异常");
       response.getWriter().println(message);
     }
     }

    public void responseRecord(String sno,HttpServletResponse response) throws SQLException, IOException {
      MRecordService.getInstance().findCardBySno(sno);
      Collection<Record> records=MRecordService.getInstance().findBySno();
      String records_json= JSON.toJSONString(records);
      response.getWriter().println(records_json);
    }
    public void responseRecord(HttpServletResponse response)throws SQLException,IOException{
      Collection<Record> records=MRecordService.getInstance().findAll();
      response.getWriter().println(JSON.toJSONString(records));
    }
}
