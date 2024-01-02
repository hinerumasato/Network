package Teach.RMI.Student;

import java.sql.SQLException;

public class UserService {

    private Database database = new Database();

    public User login(String username, String password) throws ClassNotFoundException, SQLException {
        return database.login(username, password);
    }
}
