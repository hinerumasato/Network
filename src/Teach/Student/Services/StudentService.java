package Teach.Student.Services;

import java.util.List;

import Teach.Student.Student;
import Teach.Student.Repositories.StudentRepository;

public class StudentService implements IService<Student> {

    private StudentRepository studentRepository = new StudentRepository();

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> findByName(String name) {
        return studentRepository.findByName(name);
    }

    public List<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public List<Student> findByScore(double score) {
        return studentRepository.findByScore(score);
    }
    
}
