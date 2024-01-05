package Teach.RMI.Expression;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AuthenticationImpl extends UnicastRemoteObject implements IAuthentication {

    private Map<String, User> sessionMap = new HashMap<String, User>();

    private List<User> users = new ArrayList<User>(Arrays.asList(
        new User("1", "admin", "admin123"),
        new User("2", "abc", "abc")
    ));

    protected AuthenticationImpl() throws RemoteException {
        super();
    }

    @Override
    public String login(String username, String password) throws RemoteException {
        String sessionId = "";
        for (User user : users) {
            if(username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                sessionId = UUID.randomUUID().toString();
                sessionMap.put(sessionId, user);
                break;
            }
        }
        // Return cái sessionId
        return sessionId;
    }

    @Override
    public User getUserData(String sessionId) throws RemoteException {
        if(sessionMap.containsKey(sessionId))
            return sessionMap.get(sessionId);
        return null;
    }
    
}
