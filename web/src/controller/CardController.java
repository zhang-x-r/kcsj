package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.Card;
import service.CardService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/card.ctl")
public class CardController extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置请求字符编码为UTF-8
       // request.setCharacterEncoding("UTF-8");
        String card_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Card对象
        Card cardPasswordToUpdate = JSON.parseObject(card_json, Card.class);
        //设置响应字符编码为UTF-8
       // response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加加Card对象
        try {
            CardService.getInstance().update(cardPasswordToUpdate);
            message.put("message", "修改成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
        } catch (Exception e) {
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id_str = request.getParameter("id");
        String cno_str = request.getParameter("cno");
        //设置响应字符编码为UTF-8
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有系对象，否则响应id指定的系对象
            if (cno_str == null && id_str == null) {
                message.put("message","请输入卡号");
                response.getWriter().println(message);
            } else {
                // if (status_str.equals("正常")) {
                if (cno_str == null) {
                    int id = Integer.parseInt(id_str);
                    responseCard(id, response);
                } else {
                    responseCardByStudent(cno_str, response);
                }
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            //响应message到前端
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "网络异常");
            //响应message到前端
            response.getWriter().println(message);
        }
    }

    //响应一个系对象
    private void responseCardByStudent(String cno, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        Card card = CardService.getInstance().findByStudent(cno);
        String card_json = JSON.toJSONString(card);

        //响应message到前端
        response.getWriter().println(card_json);
    }

    private void responseCard(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        //根据id查找教师
        Card card = CardService.getInstance().find(id);
        String card_json = JSON.toJSONString(card);

        //响应teacher_json到前端
        response.getWriter().println(card_json);
    }
}
