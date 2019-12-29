package service;

import dao.RecordDao;
import domain.Record;

import java.sql.SQLException;
import java.util.Collection;

public class RecordService {
    private static RecordDao recordDao = RecordDao.getInstance();
    private static RecordService recordService = new RecordService();

    private RecordService() {
    }

    public static RecordService getInstance() {
        return recordService;
    }

    public Collection<Record> findByCard(String cno) throws SQLException {
        return recordDao.findByCard(cno);
    }
//    public Collection<Record> findAll() throws SQLException{
//        return recordDao.findAll();
//    }
}
