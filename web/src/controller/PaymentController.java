package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.Payment;
import service.PaymentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/payment.ctl")
public class PaymentController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dno_str = req.getParameter("dno");
        JSONObject message = new JSONObject();
        try{
            int dno = Integer.parseInt(dno_str);
            responsePayment(dno,resp);
        }
        catch (Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }

    }
    private void responsePayment(int dno,HttpServletResponse response)throws ServletException,IOException{
        JSONObject message = new JSONObject();
        try{
            Collection<Payment> payments = PaymentService.getInstance().findByDno(dno);
            String payments_json = JSON.toJSONString(payments);
            response.getWriter().println(payments_json);
        }
        catch (SQLException e){
            message.put("message","数据库操作异常");
            e.printStackTrace();
            response.getWriter().println(message);
        }
        catch (Exception e){
            message.put("message","网络异常");
            e.printStackTrace();
            response.getWriter().println(message);
        }
    }
}
