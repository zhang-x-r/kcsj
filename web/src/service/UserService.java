package service;

import dao.UserDao;
import domain.User;

import java.sql.SQLException;

public class UserService {
    private static UserDao userDao = new UserDao();
    private static UserService userService = new UserService();
    public static  UserService getInstance(){return userService;}

    public User login(String username,String password) throws SQLException{
        return userDao.login(username,password);
    }
}
