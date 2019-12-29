package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.Details;
import service.CardService;
import service.DetailsService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/details.ctl")
public class DetailsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cno_str = request.getParameter("cno");
        JSONObject message = new JSONObject();
        try{
            if (cno_str == null){
                responseDetails(response);
            }
            else{
                responseDetails(cno_str,response);
            }
        }
        catch (Exception e ){
            e.printStackTrace();
            message.put("message","获得消费明细失败");
            response.getWriter().println(message);
        }
    }

    private void responseDetails(HttpServletResponse response) throws ServletException, IOException, SQLException {
        Collection<Details> details = DetailsService.getInstance().findAll();
        String details_json = JSON.toJSONString(details);
        response.getWriter().println(details_json);

    }
    private void responseDetails(String cno,HttpServletResponse response) throws ServletException,IOException{
        JSONObject message = new JSONObject();
        try{
            Collection<Details> details = DetailsService.getInstance().find(cno);
            String details_str = JSON.toJSONString(details);
            response.getWriter().println(details_str);
            System.out.println(details_str);
        }
        catch(SQLException e){
            message.put("message", "获取失败");
            e.printStackTrace();
            response.getWriter().println(message);
        }
    }

}
