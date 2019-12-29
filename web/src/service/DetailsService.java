package service;

import dao.DetailsDao;
import domain.Details;

import java.sql.SQLException;
import java.util.Collection;

public final class DetailsService {
    private static DetailsDao detailsDao = DetailsDao.getInstance();
    private static DetailsService detailsService = new DetailsService();

    public DetailsService() { }
    public static DetailsService getInstance(){
        return detailsService;
    }
    public Collection<Details> find(String cno)throws SQLException {
        return detailsDao.find(cno);
    }
    public Collection<Details> findAll() throws SQLException {
        return detailsDao.findAll();
    }


}
