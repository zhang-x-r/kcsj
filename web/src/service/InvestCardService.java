package service;

import dao.InvestDao;
import domain.Card;

import java.sql.SQLException;
import java.util.Collection;

public final class InvestCardService {
    private static InvestDao investDao = InvestDao.getInstance();
    private static InvestCardService investCardService = new InvestCardService();

    public InvestCardService() { }
    public static InvestCardService getInstance(){
        return investCardService;
    }
    public Card find(Card card)throws SQLException {
        return investDao.find(card);
    }
    public Collection<Card> findBySno(String sno)throws SQLException{
        return investDao.findBySno(sno);
    }
    public boolean invest(Card card)throws SQLException{
        return investDao.invest(card);
    }


}
