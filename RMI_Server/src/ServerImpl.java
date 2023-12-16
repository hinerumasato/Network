import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerImpl extends UnicastRemoteObject implements ServerInterface {

    private StudentService studentService;

    protected ServerImpl() throws RemoteException {
        super();
        this.studentService = new StudentService();
    }

    @Override
    public List<Student> findByName(String name) throws RemoteException {
        return studentService.findByName(name);
    }

    @Override
    public List<Student> findByAge(int age) throws RemoteException {
        return studentService.findByAge(age);
    }

    @Override
    public List<Student> findByScore(double score) throws RemoteException {
        return studentService.findByScore(score);
    }
    
}
