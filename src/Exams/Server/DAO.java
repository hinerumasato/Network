package Exams.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO {

    private static final String DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";
    private static final String URL = "jdbc:ucanaccess://resources/db/exam1.accdb";
    private Connection connection;

    public void createConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        this.connection = DriverManager.getConnection(URL);
    }

    public void close(PreparedStatement stmt, ResultSet rs) throws SQLException {
        if (rs != null)
            rs.close();
        if (stmt != null)
            stmt.close();
        if (connection != null)
            connection.close();

    }

    public boolean isContainUsername(String username) throws SQLException, ClassNotFoundException {
        createConnection();
        String sql = "SELECT * FROM users WHERE username = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        stmt = connection.prepareStatement(sql);
        stmt.setObject(1, username);
        rs = stmt.executeQuery();
        result = rs.next();
        close(stmt, rs);
        return result;
    }

    public boolean isValidUser(String username, String password) throws SQLException, ClassNotFoundException {
        createConnection();
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;

        stmt = connection.prepareStatement(sql);
        stmt.setObject(1, username);
        stmt.setObject(2, password);
        rs = stmt.executeQuery();
        result = rs.next();

        close(stmt, rs);
        return result;
    }

    public void insertProduct(Product product) throws SQLException, ClassNotFoundException {
        createConnection();
        String sql = "INSERT INTO products (ID, name, amount, price) VALUES(?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, product.getId());
        stmt.setObject(2, product.getName());
        stmt.setObject(3, product.getAmount());
        stmt.setObject(4, product.getPrice());
        stmt.executeUpdate();
        close(stmt, null);
    }

    public int removeProduct(int id) throws SQLException, ClassNotFoundException {
        createConnection();
        String sql = "DELETE FROM products WHERE ID = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, id);
        int rowsAffected = stmt.executeUpdate();
        close(stmt, null);
        return rowsAffected;
    }

    public void updateProduct(Product product) throws ClassNotFoundException, SQLException {
        createConnection();
        String sql = "UPDATE products SET name = ?, amount = ?, price = ? WHERE ID = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, product.getName());
        stmt.setObject(2, product.getAmount());
        stmt.setObject(3, product.getPrice());
        stmt.setObject(4, product.getId());
        stmt.executeUpdate();
        close(stmt, null);
    }

    public String view(String search) throws ClassNotFoundException, SQLException {
        createConnection();
        search = "%" + search + "%";
        String sql = "SELECT * FROM products WHERE name LIKE ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, search);
        ResultSet rs = stmt.executeQuery();
        List<Product> products = new ArrayList<Product>();
        String result = "";
        while(rs.next()) {
            int id = rs.getInt("ID");
            String name = rs.getString("name");
            int amount = rs.getInt("amount");
            double price = rs.getDouble("price");
            Product product = new Product(id, name, amount, price);
            products.add(product);
        }

        close(stmt, rs);

        for (Product product : products) {
            result += "ID = " + product.getId() + ", Name = " + product.getName() + ", Amount = " + product.getAmount() + ", Price = " + product.getPrice() + "\n";
        }
        result += "THE END. \n";
        return result;
    }
}
