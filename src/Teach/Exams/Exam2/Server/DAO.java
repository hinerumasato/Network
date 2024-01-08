package Teach.Exams.Exam2.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAO {

    private static String DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";
    private static String URL = "jdbc:ucanaccess://resources/db/exam.accdb";

    private Connection connection;

    public void createConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        connection = DriverManager.getConnection(URL);
    }

    public void close(PreparedStatement stmt, ResultSet rs) throws SQLException {
        if (rs != null)
            rs.close();
        if (stmt != null)
            stmt.close();
        connection.close();
    }

    public boolean isExistUsername(String username) throws ClassNotFoundException, SQLException {
        createConnection();
        String sql = "SELECT * FROM users WHERE username = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, username);
        ResultSet rs = stmt.executeQuery();
        boolean isHaveData = rs.next();
        close(stmt, rs);
        return isHaveData;
    }

    public boolean isValidUser(String username, String password) throws ClassNotFoundException, SQLException {
        createConnection();
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, username);
        stmt.setObject(2, password);
        ResultSet rs = stmt.executeQuery();
        boolean isHaveData = rs.next();
        close(stmt, rs);
        return isHaveData;
    }

    public void insert(Product product) throws ClassNotFoundException, SQLException {
        createConnection();
        String sql = "INSERT INTO products (id, name, amount, price) VALUES(?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, product.getId());
        stmt.setObject(2, product.getName());
        stmt.setObject(3, product.getAmount());
        stmt.setObject(4, product.getPrice());
        stmt.executeUpdate();
        
        close(stmt, null);
    }

    public void update(Product product) throws ClassNotFoundException, SQLException {
        createConnection();
        String sql = "UPDATE products SET name=?, amount=?, price=? WHERE ID = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, product.getName());
        stmt.setObject(2, product.getAmount());
        stmt.setObject(3, product.getPrice());
        stmt.setObject(4, product.getId());
        stmt.executeUpdate();
        
        close(stmt, null);
    }

    public int delete(String id) throws ClassNotFoundException, SQLException {
        createConnection();
        String sql = "DELETE FROM products WHERE ID = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, id);
        int rowsAffected = stmt.executeUpdate();
        close(stmt, null);
        return rowsAffected;
    }

    public String search(String search) throws ClassNotFoundException, SQLException {
        createConnection();
        search = "%" + search + "%";
        String sql = "SELECT * FROM products WHERE NAME LIKE ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, search);
        ResultSet rs = stmt.executeQuery();
        String result = "";
        while (rs.next()) {
            String id = rs.getString("ID");
            String name = rs.getString("name");
            int amount = rs.getInt("amount");
            double price = rs.getDouble("price");

            result += "ID = " + id + ", Name = " + name + ", Amount = " + amount + ", Price = " + price + "\n"; 
        }

        result += "THE END";
        close(stmt, rs);
        return result;
    }
}
