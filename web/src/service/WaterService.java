package service;

import dao.WaterDao;
import domain.BuyWater;
import domain.Dormitory;

import java.sql.SQLException;
import java.util.Collection;
//201802104010 蒋羽
public class WaterService {
    private static WaterDao waterDao
            = WaterDao.getInstance();
    private static WaterService waterService
            =new WaterService();
    private WaterService(){}

    public static WaterService getInstance(){
        return waterService;
    }
    public Collection<Dormitory> findAllDormitory()throws SQLException {
        return waterDao.findAllByDormitory();
    }
    public Dormitory findDormitoryById(Integer id)throws SQLException{
        return waterDao.find(id);
    }
    public Dormitory findDormitoryByDno(Integer dno) throws SQLException{
        return waterDao.findDormitoryByDno(dno);
    }
       public boolean update(BuyWater buyWater)throws SQLException{
        return waterDao.update(buyWater);
    }
}
