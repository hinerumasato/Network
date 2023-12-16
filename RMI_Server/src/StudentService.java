import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentService {

    private List<Student> students = new ArrayList<Student>(Arrays.asList(
        new Student("Hineru Masato", 20, 7.47),
        new Student("Nguyen Phuong Nha", 20, 9.0),
        new Student("Hoang Hai Van", 20, 8.5)
    ));

    public List<Student> findByName(String name) {
        List<Student> result = new ArrayList<Student>();
        students.stream().forEach(student -> {
            if(student.getName().equals(name))
                result.add(student);
        });
        return result;
    }

    public List<Student> findByAge(int age) {
        List<Student> result = new ArrayList<Student>();
        students.stream().forEach(student -> {
            if(student.getAge() == age)
                result.add(student);
        });
        return result;
    }

    public List<Student> findByScore(double score) {
        List<Student> result = new ArrayList<Student>();
        students.stream().forEach(student -> {
            if(student.getScore() == score)
                result.add(student);
        });
        return result;
    } 
}
