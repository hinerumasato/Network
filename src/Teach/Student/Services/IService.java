package Teach.Student.Services;

import java.util.List;

public interface IService<T> {
    public List<T> findAll();
    public List<T> findByName(String name);
}
