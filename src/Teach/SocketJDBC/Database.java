package Teach.SocketJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";
    private static final String URL = "jdbc:ucanaccess://resources/db/teach.accdb";
    private Connection connection;

    public void openConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        this.connection = DriverManager.getConnection(URL);
    }

    public List<Student> findByAge(int age) throws ClassNotFoundException, SQLException {
        openConnection();
        List<Student> students = new ArrayList<Student>();
        String sql = "SELECT * FROM STUDENTS WHERE AGE = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, age);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            String name = rs.getString("name");
            int _age = rs.getInt("age");
            double score = rs.getDouble("score");
            Student student = new Student(id, name, _age, score);
            students.add(student);
        }

        rs.close();
        stmt.close();
        connection.close();

        return students;
    }

    public void insert(Student student) throws ClassNotFoundException, SQLException {
        openConnection();
        String sql = "INSERT INTO students (name, age, score) VALUES (?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, student.getName());
        stmt.setObject(2, student.getAge());
        stmt.setObject(3, student.getScore());
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    public void update(int id, Student student) throws ClassNotFoundException, SQLException {
        openConnection();
        String sql = "UPDATE students SET name = ?, age = ?, score = ? WHERE ID = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, student.getName());
        stmt.setObject(2, student.getAge());
        stmt.setObject(3, student.getScore());
        stmt.setObject(4, id);
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    public void delete(int id) throws ClassNotFoundException, SQLException {
        openConnection();
        String sql = "DELETE FROM students WHERE ID = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, id);
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }
}
