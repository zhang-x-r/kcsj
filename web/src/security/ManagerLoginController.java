package security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.Manager;
import service.CardService;
import service.ManagerService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/managerLogin")
public class ManagerLoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String manager_json = JSONUtil.getJSON(req);
        Manager managerFrom = JSON.parseObject(manager_json, Manager.class);
        JSONObject message = new JSONObject();
        Manager manager = null;
        try {
            manager =ManagerService.getInstance().login(managerFrom.getUsername(), managerFrom.getPassword());
            if (manager!= null) {
                //从请求中获取session如果没有则自动创建
                HttpSession session = req.getSession();
                session.setMaxInactiveInterval(60 * 10);//set login time with 10 mins
                //向session中添加
                session.setAttribute("currentManager",manager);
                //推送前台
                String manager_json1 = JSON.toJSONString(manager);
                resp.getWriter().println(manager_json1);
            }
            else{
                message.put("message", "用户名或密码错误");
                resp.getWriter().println(message);
            }
        }
        catch (SQLException e){
            message.put("message","数据库操作异常");
            e.printStackTrace();
            resp.getWriter().println(message);
        }
        catch (Exception e){
            message.put("message","网络异常");
            e.printStackTrace();
            resp.getWriter().println(message);
        }
    }
}
