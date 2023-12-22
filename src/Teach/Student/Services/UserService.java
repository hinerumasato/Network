package Teach.Student.Services;

import java.util.List;

import Teach.Student.User;
import Teach.Student.Repositories.UserRepository;

public class UserService implements IService<User> {

    private UserRepository userRepository = new UserRepository();

    @Override
    public List<User> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public List<User> findByName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByName'");
    }
    
    public boolean login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
