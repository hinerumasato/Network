package Teach.Student.Repositories;

import java.util.List;

public interface BaseRepository <T> {
    public List<T> findAll();
    public List<T> findByName(String name);
}
