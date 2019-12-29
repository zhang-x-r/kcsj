package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.Card;
import service.InvestCardService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/investCard.ctl")
public class InvestCardController extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { ;
    String card_json = JSONUtil.getJSON(request);
    Card cardMoney = JSON.parseObject(card_json, Card.class);
    JSONObject message = new JSONObject();
    try {
      Card card= InvestCardService.getInstance().find(cardMoney);
      if (card!=null){
        InvestCardService.getInstance().invest(cardMoney);
        message.put("message", "充值成功");
      } else {
        message.put("message","该卡处于挂失状态，不能进行充值操作");
      }
    } catch (SQLException e) {
      message.put("message", "数据库操作异常");
      e.printStackTrace();
    } catch (Exception e) {
      message.put("message", "网络异常");
      e.printStackTrace();
    }
    response.getWriter().println(message);
  }
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String sno_json=request.getParameter("sno");
    JSONObject message=new JSONObject();
    try{
      Collection<Card> cards= InvestCardService.getInstance().findBySno(sno_json);
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

