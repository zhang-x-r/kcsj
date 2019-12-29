package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.CardDao;
import domain.BuyEle;
import domain.Dormitory;
import service.ElectricityService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/electricity.ctl")
public class ElectricityController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String id_str = req.getParameter("id");
        String dno_str = req.getParameter("dno");
        //JSONObject message = new JSONObject();
        try {
            if (id_str == null && dno_str == null ) {
                responseElectricities(resp);
            }
             if(id_str != null){
                int id = Integer.parseInt(id_str);
                responseElectricityById(id ,resp);
            }
             if (dno_str != null ){
                 int dno = Integer.parseInt(dno_str);
                 response(dno,resp);
             }
        }
        catch (Exception e ){
            e.printStackTrace();
//            message.put("message ","网络异常");
//            resp.getWriter().println(message);
        }

    }

    private void responseElectricities(HttpServletResponse resp) throws ServletException,IOException{
        try {
            //获得所有宿舍
            Collection<Dormitory> electricities = ElectricityService.getInstance().findAll();
            //将electricities解析成json字串
            String electricities_json = JSONObject.toJSONString(electricities);
            //加入数据信息
            resp.getWriter().println(electricities_json);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void responseElectricityById(int id,HttpServletResponse resp) throws ServletException,IOException{
        try{
            //根据id查找宿舍
            Dormitory electricity = ElectricityService.getInstance().findById(id);
            //将electricity解析成json字串
            String electricity_json = JSONObject.toJSONString(electricity);
            //响应到前端
            resp.getWriter().println(electricity_json);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
     private void response(int dno,HttpServletResponse resp) throws ServletException,IOException{
        try{
            //根据宿舍号和楼号查找宿舍
            Dormitory electricity = ElectricityService.getInstance().find(dno);
            //将electricity解析成json字串
            String electricity_json = JSONObject.toJSONString(electricity);
            //响应到前端
            resp.getWriter().println(electricity_json);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
     }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从请求中获取json数据
        String buyEle_json = JSONUtil.getJSON(req);
        //转化成对象
        BuyEle buyEle = JSON.parseObject(buyEle_json, BuyEle.class);
        System.out.println(buyEle_json);
        JSONObject message = new JSONObject();
        float cardMoney  = 0;
        String status = null;
        try{
            status = CardDao.getInstance().findByLogin(buyEle.getCno(),buyEle.getPassword()).getStatus();
            if(status.equals("正常")) {
                cardMoney = CardDao.getInstance().findByLogin(buyEle.getCno(), buyEle.getPassword()).getMoney();
                if (cardMoney < buyEle.getMoney()) {
                    message.put("message", "余额不足");
                } else {
                    ElectricityService.getInstance().updateElectricity(buyEle);
                    message.put("message", "充值成功");
                }
            }
            else {
                message.put("message","此卡不可用");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            message.put("message","数据库操作异常");
        }
        catch (Exception e ){
            e.printStackTrace();
            message.put("message","网络异常");
        }
        resp.getWriter().println(message);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
