package controller;

import dao.CardDao;
import domain.BuyWater;
import domain.Dormitory;
import service.CardService;
import service.WaterService;
import util.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/water.ctl")
public class WaterController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数
        String dno_str = request.getParameter("dno");
        String id_str = request.getParameter("id");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            if (dno_str!=null) {
                    int dno = Integer.parseInt(dno_str);
                    respondseWaters1(dno,response);
                }
            if (id_str!=null){
                    int id = Integer.parseInt(id_str);
                    respondseWaters(id, response);
                } if(dno_str==null&&id_str==null){
                respondseWaters(response);
            }
        }catch (SQLException e) {
            message.put("message","数据库操作异常");
        }catch (Exception e){
            message.put("message","网络异常");
            e.printStackTrace();
        }
    }
    protected void respondseWaters1(Integer dno,HttpServletResponse response) throws SQLException, IOException {
        Dormitory dormitory_water = WaterService.getInstance().findDormitoryByDno(dno);
        String water_json = JSON.toJSONString(dormitory_water);
        //响应message到前端
        response.getWriter().println(water_json);
    }

    protected void respondseWaters(Integer id, HttpServletResponse response) throws SQLException, IOException {

        Dormitory dormitory_water = WaterService.getInstance().findDormitoryById(id);
        String water_json = JSON.toJSONString(dormitory_water);
        //响应message到前端
        response.getWriter().println(water_json);

    }

    protected void respondseWaters(HttpServletResponse response) throws SQLException, IOException {
        //获得所有宿舍的水情况
        Collection waters = WaterService.getInstance().findAllDormitory();
        String waters_json = JSON.toJSONString(waters);
        //响应message到前端
        response.getWriter().println(waters_json);
    }
    protected void doPut(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        String buyWater_json = JSONUtil.getJSON(request);
        BuyWater buyWater = JSON.parseObject(buyWater_json,BuyWater.class);
        System.out.println(buyWater);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        String  status = null;
        float money1 =0;
        try {
            status = CardDao.getInstance().findByLogin(buyWater.getCno(),buyWater.getPassword()).getStatus();
            if(status.equals("正常")) {
                money1 = CardDao.getInstance().findByLogin(buyWater.getCno(), buyWater.getPassword()).getMoney();
                if (money1 >= buyWater.getMoney()) {
                    //修改Dormitory对象
                    WaterService.getInstance().update(buyWater);
                    //加入数据信息
                    message.put("message", "充值成功");
                }
            }
            else {
                message.put("message","此卡不可用");
            }
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }
}
