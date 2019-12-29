package service;

import dao.CardDao;
import domain.Card;

import java.sql.SQLException;

public class CardService {
    private static CardDao cardDao= CardDao.getInstance();
    private static CardService cardService=new CardService();
    private CardService(){}
    public static CardService getInstance(){
        return cardService;
    }
   public Card findByStudent(String cno) throws SQLException {
       return cardDao.findByStudent(cno);
   }

    public Card find(Integer id)throws SQLException {
        return cardDao.find(id);
    }
    public boolean update(Card card)throws ClassNotFoundException, SQLException {
        return cardDao.update(card);
    }
    public Card findByLogin(String cno,String password) throws SQLException {
        return cardDao.findByLogin(cno,password);
    }

}
