package Teach.Student.Repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Teach.Student.User;

public class UserRepository implements BaseRepository<User> {

    private List<User> users = new ArrayList<User>(Arrays.asList(
            new User("abc", "abc"),
            new User("admin", "12345")));

    @Override
    public List<User> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public List<User> findByName(String name) {
        throw new UnsupportedOperationException("Unimplemented method 'findByName'");
    }

    public boolean findByUsernameAndPassword(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password))
                return true;
        }
        return false;
    }

}
