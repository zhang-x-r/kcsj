package security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.Card;
import domain.User;
import service.CardService;
import service.UserService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/userLogin")
public class UserLoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String card_json = JSONUtil.getJSON(req);
        User userFrom = JSON.parseObject(card_json, User.class);
        JSONObject message = new JSONObject();
        User user = null;
            try {
                user = UserService.getInstance().login(userFrom.getUsername(), userFrom.getPassword());
                if (user!= null) {
                    //从请求中获取session如果没有则自动创建
                    HttpSession session = req.getSession();
                    session.setMaxInactiveInterval(60 * 10);//set login time with 10 mins
                    //向session中添加
                    session.setAttribute("currentUser",user);
                    //推送前台
                    String user_json = JSON.toJSONString(user);
                    resp.getWriter().println(user_json);
                }
                else{
                    message.put("message", "用户名或密码错误");
                    resp.getWriter().println(message);
                }
            } catch (SQLException e) {
                message.put("message", "数据库操作异常");
                e.printStackTrace();
                resp.getWriter().println(message);
            } catch (Exception e) {
                message.put("message", "网络异常");
                e.printStackTrace();
                resp.getWriter().println(message);
            }

    }
}
