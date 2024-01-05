package Exams.Server;

import java.sql.SQLException;
import java.util.List;

public class Service {
    private DAO dao = new DAO();

    public boolean isContainUsername(String username) throws ClassNotFoundException, SQLException {
        return dao.isContainUsername(username);
    }

    public boolean isValidUser(String username, String password) throws ClassNotFoundException, SQLException {
        return dao.isValidUser(username, password);
    }

    public void insertProduct(Product product) throws ClassNotFoundException, SQLException {
        dao.insertProduct(product);
    }

    public int removeProduct(List<Integer> ids) throws ClassNotFoundException, SQLException {
        int sum = 0;
        for (int id : ids) {
            sum += dao.removeProduct(id);
        }
        return sum;
    }

    public void updateProduct(Product product) throws ClassNotFoundException, SQLException {
        dao.updateProduct(product);
    }

    public String view(String search) throws ClassNotFoundException, SQLException {
        return dao.view(search);
    }
}
