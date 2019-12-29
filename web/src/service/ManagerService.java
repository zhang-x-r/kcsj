package service;

import dao.ManagerDao;
import domain.Manager;

import java.sql.SQLException;

public class ManagerService {
    private static ManagerService managerService = new ManagerService();
    private static ManagerDao managerDao = new ManagerDao();
    public static ManagerService getInstance(){return managerService;}

    public Manager login(String username, String password) throws SQLException{
        return managerDao.login(username,password);
    }
}
