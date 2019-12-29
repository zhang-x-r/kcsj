package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.Record;
import service.RecordService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/record.ctl")
public class RecordController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cno = request.getParameter("cno");
        //设置响应字符编码为UTF-8
        //response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有系对象，否则响应id指定的系对象
           // if (cno != null) {
                responseRecordsByCard(cno, response);
           // }
//            else{
//                responseRecords(response);
//            }
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

    private void responseRecordsByCard(String cno, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        Collection<Record> records = RecordService.getInstance().findByCard(cno);
        String records_json = JSON.toJSONString(records);
        //响应到前端
        response.getWriter().println(records_json);
    }
//    private void responseRecords(HttpServletResponse response) throws ServletException,IOException,SQLException{
//        Collection<Record> records = RecordService.getInstance().findAll();
//        String records_json = JSON.toJSONString(records);
//        response.getWriter().println(records_json);
//    }
}
