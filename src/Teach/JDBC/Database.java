package Teach.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";
    private static final String URL = "jdbc:ucanaccess://resources/db/teach.accdb";
    private Connection connection;


    public Connection createConnection() throws Exception {
        Class.forName(DRIVER);
        Connection connection = DriverManager.getConnection(URL);
        return connection;
    }

    public List<Student> findBy(String column, Object value) throws Exception {
        this.connection = createConnection();
        List<Student> students = new ArrayList<Student>();
        String sql = "SELECT * FROM STUDENTS WHERE " + column + "= ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, value);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            double score = rs.getDouble("score");

            Student student = new Student(id, name, age, score);
            students.add(student);
        }

        rs.close();
        stmt.close();
        connection.close();
        return students;
    }

    public List<Student> findByName(String name) throws Exception {
        return findBy("NAME", name);
    }

    public List<Student> findByAge(int age) throws Exception {
        return findBy("AGE", age);
    }

    public List<Student> findByScore(double score) throws Exception {
        return findBy("SCORE", score);
    }
}
