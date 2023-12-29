package Teach.JDBC;

import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        StudentService service = new StudentService();
        // List<Student> queryResult = service.findByName("Nguyen Van A");
        // System.out.println(queryResult);

        // List<Student> findByScoreResult = service.findByScore(8.0);
        // System.out.println(findByScoreResult);

        List<Student> findByAgeResult = service.findByAge(22);
        System.out.println(findByAgeResult);
    }
}
