import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class Client {

    private ServerInterface server;

    private void runProcees() throws Exception {
        init();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean exit = false;
        while (!exit) {
            System.out.print("Enter your command: ");
            String command = reader.readLine();
            List<Student> foundStudents = findStudentFromServer(command);
            printStudentsData(foundStudents);
        }

        reader.close();
    }

    private List<Student> findStudentFromServer(String command) throws Exception {
        String commandName = command.split(" ")[0];
        String commandValue = command.split(" ")[1];

        List<Student> foundStudents = null;
        switch (commandName) {
            case "findByName":
                foundStudents = server.findByName(commandValue);
                return foundStudents;
            case "findByAge":
                int age = Integer.parseInt(commandValue);
                foundStudents = server.findByAge(age);
                return foundStudents;
            case "findByScore":
                double score = Double.parseDouble(commandValue);
                foundStudents = server.findByScore(score);
                return foundStudents;
            default:
                throw new Exception("The command is not valid");
        }
    }

    private void printStudentsData(List<Student> students) {
        students.forEach(student -> {
            System.out.print("Name = " + student.getName() + ", Age = " + student.getAge() + ", Score = " + student.getScore());
            System.out.println();
        });
    }

    private void init() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 2003);
        this.server = (ServerInterface) registry.lookup("server");
    }

    public static void main(String[] args) throws Exception {
        new Client().runProcees();
    }
}
