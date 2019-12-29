package service;

import dao.ElectricityDao;
import domain.BuyEle;
import domain.Dormitory;

import java.sql.SQLException;
import java.util.Collection;
// 201802104009张欣茹
public class ElectricityService {
    private static ElectricityService electricityService = new ElectricityService();
    private static ElectricityDao electricityDao = new ElectricityDao();
    public static ElectricityService getInstance(){return electricityService;}

    public Collection<Dormitory> findAll() throws SQLException {
        return electricityDao.findAll();
    }

    public Dormitory findById(int id) throws SQLException{
        return electricityDao.findById(id);
    }
    public Dormitory find(int dno) throws SQLException{
        return electricityDao.find(dno);
    }
    public boolean updateElectricity(BuyEle buyEle) throws SQLException{
        return electricityDao.updateElectricity(buyEle);
    }
}
