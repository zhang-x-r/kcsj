package service;

import dao.PaymentDao;
import domain.Payment;

import java.sql.SQLException;
import java.util.Collection;

public class PaymentService {
    private static PaymentService paymentService = new PaymentService();
    private static PaymentDao paymentDao = new PaymentDao();
    public static PaymentService getInstance(){ return paymentService;}

    public Collection<Payment> findByDno(int dno) throws SQLException{
        return paymentDao.findByDno(dno);
    }
}
