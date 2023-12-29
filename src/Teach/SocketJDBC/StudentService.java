package Teach.SocketJDBC;

import java.sql.SQLException;
import java.util.List;

public class StudentService {

    private Database database = new Database();

    public List<Student> findByAge(int age) throws ClassNotFoundException, SQLException {
        return database.findByAge(age);
    }

    public void insert(Student student) throws ClassNotFoundException, SQLException {
        database.insert(student);
    }

    public void update(int id, Student student) throws ClassNotFoundException, SQLException {
        database.update(id, student);
    }

    public void delete(int id) throws ClassNotFoundException, SQLException {
        database.delete(id);
    }
}
