package Teach.JDBC;

import java.util.List;

public class StudentService {

    private Database database = new Database();

    public List<Student> findByName(String name) throws Exception {
        return database.findByName(name);
    }

    public List<Student> findByAge(int age) throws Exception {
        return database.findByAge(age);
    }

    public List<Student> findByScore(double score) throws Exception {
        return database.findByScore(score);
    }
}
