package Teach.Student.Repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Teach.Student.Student;

public class StudentRepository implements BaseRepository<Student> {

    // Truy vấn tới cơ sở dữ liệu
    // Tạo dữ liệu giả
    private List<Student> students = new ArrayList<Student>(Arrays.asList(
            new Student("Nguyen Van A", 20, 7.0),
            new Student("Nguyen Van B", 21, 8.0),
            new Student("Nguyen Van C", 22, 9.0)));

    @Override
    public List<Student> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public List<Student> findByName(String name) {
        List<Student> result = new ArrayList<Student>();
        for (Student student : students) {
            if (student.getName().equals(name))
                result.add(student);
        }
        return result;
    }

    public List<Student> findByAge(int age) {
        List<Student> result = new ArrayList<Student>();
        for (Student student : students) {
            if (student.getAge() == age)
                result.add(student);
        }
        return result;
    }

    public List<Student> findByScore(double score) {
        List<Student> result = new ArrayList<Student>();
        for (Student student : students) {
            if (student.getScore() == score)
                result.add(student);
        }
        return result;
    }

}
