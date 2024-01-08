package Teach.Exams.Exam2.Server;

import java.sql.SQLException;
import java.util.List;

public class Service {

    private DAO dao = new DAO();

    public boolean isExistUsername(String username) throws ClassNotFoundException, SQLException {
        return dao.isExistUsername(username);
    }

    public boolean isValidUser(String username, String password) throws ClassNotFoundException, SQLException {
        return dao.isValidUser(username, password);
    }

    public void insert(Product product) throws ClassNotFoundException, SQLException {
        dao.insert(product);
    }

    public void update(Product product) throws ClassNotFoundException, SQLException {
        dao.update(product);
    }

    public String search(String search) throws ClassNotFoundException, SQLException {
        return dao.search(search);
    }

    public int delete(List<String> ids) throws ClassNotFoundException, SQLException {
        int sum = 0;
        for (String id : ids) {
            sum += dao.delete(id);
        }
        return sum;
    }
}
