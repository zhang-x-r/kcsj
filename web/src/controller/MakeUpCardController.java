package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.Card;
import domain.Student;
import service.MakeUpCardService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/makeUpCard.ctl")
public class MakeUpCardController extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String sno_json= JSONUtil.getJSON(request);
    Student student=JSON.parseObject(sno_json,Student.class);
    JSONObject message=new JSONObject();
    try {
      MakeUpCardService.getInstance().find(student);
      MakeUpCardService.getInstance().lastCard();
      MakeUpCardService.getInstance().makeUpCard(student);
      message.put("message","补卡成功");

    }catch (SQLException e){
      e.printStackTrace();
      message.put("message","数据库异常");
    }catch (Exception e){
      e.printStackTrace();
      message.put("message","网络异常");
    }
    response.getWriter().println(message);
  }
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    response.setHeader("Access-Control-Allow-Origin","*");
    String number_json=request.getParameter("number");
    JSONObject message=new JSONObject();
    if (number_json!=null) {
      int number = Integer.parseInt(number_json);
      MakeUpCardService.getInstance().setRows(number);
      message.put("message", "您已将最大记录数改为" + number);
    }
    response.getWriter().println(message);
  }
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String sno_json=request.getParameter("sno");
    JSONObject message=new JSONObject();
    try{
      Collection<Card> cards= MakeUpCardService.getInstance().findBySno(sno_json);
      if (cards.size()>0){
        String cards_json= JSON.toJSONString(cards);
        response.getWriter().println(cards_json);
      }
      else {
        message.put("message","该用户不存在");
        response.getWriter().println(message);
      }

    }catch (SQLException e){
      e.printStackTrace();
      message.put("message","数据库异常");
      response.getWriter().println(message);
    }catch (Exception e){
      e.printStackTrace();
      message.put("message","网络异常");
      response.getWriter().println(message);
    }

  }
}
