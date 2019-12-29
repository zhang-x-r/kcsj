package service;

import dao.MCardDao;
import domain.Card;

import java.sql.SQLException;
import java.util.Collection;

public class MCardService {
    private  static MCardDao cardDao = MCardDao.getInstance();
    private MCardService(){};
    private static MCardService cardService =new MCardService();
    public static MCardService getInstance(){
        return cardService;
    }
    public Collection<Card> find(String cno)throws SQLException{
        return  cardDao.find(cno);
    }
    public boolean delete(String cno)throws SQLException{
       return cardDao.delete(cno);
    }
    public Card findById(int id) throws SQLException{
        return cardDao.findById(id);
    }
    public Collection<Card> findAll()throws SQLException{
        return cardDao.findAll();
    }
    public  boolean update(Card card) throws SQLException{
        return cardDao.update(card);
    }

    }

