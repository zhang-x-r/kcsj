package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.StudentDao;
import domain.Card;
import service.CardService;
import service.MCardService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/mcard.ctl")
public class MCardController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String cno_str =request.getParameter("cno");
        String id_str = request.getParameter("id");
        JSONObject message = new JSONObject();
        try{
            if(cno_str ==null && id_str == null){
                responseCards(response);
            }
            if (cno_str != null){
                responseCard(cno_str,response);
            }
            if (id_str != null){
                int id = Integer.parseInt(id_str);
                responseCardById(id,response);
            }
        }
        catch (Exception e){
                message.put("messge","网络异常");
                response.getWriter().println(message);
        }
    }
   private void responseCard(String cno,HttpServletResponse response)throws ServletException ,IOException{
        try{
       Collection<Card> card = MCardService.getInstance().find(cno);
       String student_json = JSON.toJSONString(card);
       response.getWriter().println(student_json);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
   }
   private void responseCards(HttpServletResponse response)throws ServletException,IOException{
      try{
          Collection<Card> cards = MCardService.getInstance().findAll();

        String cards_json =JSON.toJSONString(cards);
        response.getWriter().println(cards_json);}
      catch (Exception e){
          e.printStackTrace();
      }
   }
    private void responseCardById(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        //根据id查找教师
        Card card = CardService.getInstance().find(id);
        String card_json = JSON.toJSONString(card);

        //响应到前端
        response.getWriter().println(card_json);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String graduateProject_json = JSONUtil.getJSON(req);
        System.out.println(graduateProject_json);
        //将JSON字串解析为Card对象
        Card cardToPut = JSON.parseObject(graduateProject_json, Card.class);
        //创建JSON对象
        JSONObject message = new JSONObject();
        //到数据库表修改Degree对象对应的记录
        try {
            MCardService.getInstance().update(cardToPut);
            message.put("message", "修改成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "更新失败");
        }
        //响应
        resp.getWriter().println(message);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //读取参数cno
        String cno_str = req.getParameter("cno");
        //创建JSON对象
        System.out.println(cno_str);
        JSONObject message = new JSONObject();
        //到数据库表中删除对应的学院
        try {
            MCardService.getInstance().delete(cno_str);
            message.put("message", "删除成功");
        }catch (SQLException e){
            e.printStackTrace();
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "删除失败");
        }
        //响应
        resp.getWriter().println(message);
    }


    }

