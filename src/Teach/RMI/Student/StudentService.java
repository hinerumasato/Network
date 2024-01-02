package Teach.RMI.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentService {

    private Database database = new Database();

    public List<Student> findByName(String name) throws SQLException, ClassNotFoundException {
        return database.findByName(name);
    }

    public List<Student> findByAge(int age) throws SQLException, ClassNotFoundException {
        return database.findByAge(age);
    }

    public List<Student> findByScore(double score) throws SQLException, ClassNotFoundException {
        return database.findByScore(score);
    }
}
