package Teach.Student;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import Teach.Student.Services.StudentService;
import Teach.Student.Services.UserService;

public class ServerProcess implements Runnable {

    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;
    private boolean exit;
    private boolean clientIsLogin;

    private StudentService studentService = new StudentService();
    private UserService userService = new UserService();

    public ServerProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new DataInputStream(socket.getInputStream());
        this.netOut = new DataOutputStream(socket.getOutputStream());
        this.exit = false;
        this.clientIsLogin = false;
    }

    private String getNameFromCommand(String command) {
        String[] commandSplit = command.split(" ");
        String result = "";
        for(int i = 1; i < commandSplit.length; i++)
            result += commandSplit[i] + " ";
        return result.trim();
    }

    private List<Student> commandHandler(String command) {
        String[] commandSplit = command.split(" ");
        String commandName = commandSplit[0];
        String commandValue = commandSplit[1];
        switch (commandName) {
            case "findByName":
                String name = getNameFromCommand(command);
                return studentService.findByName(name);
            case "findByAge":
                int age = Integer.parseInt(commandValue);
                return studentService.findByAge(age);
            case "findByScore":
                double score = Double.parseDouble(commandValue);
                return studentService.findByScore(score);

            default:
                return null;
        }
    }

    private void sendStudentsToClient(List<Student> students) throws IOException {
        netOut.writeInt(students.size());
        netOut.flush();
        for (Student student : students) {
            netOut.writeUTF(student.getName());
            netOut.flush();
            netOut.writeInt(student.getAge());
            netOut.flush();
            netOut.writeDouble(student.getScore());
            netOut.flush();
        }
    }

    @Override
    public void run() {
        try {
            while (!exit) {
                if (!clientIsLogin) {
                    String username = netIn.readUTF();
                    String password = netIn.readUTF();
                    if(userService.login(username, password)) {
                        clientIsLogin = true;
                        // Ghi login response cho client
                        netOut.writeUTF("success");
                        netOut.flush();
                    } else {
                        // Ghi login response cho client
                        netOut.writeUTF("error");
                        netOut.flush();
                    }
                } else {
                    String command = netIn.readUTF();
                    List<Student> foundStudents = commandHandler(command);

                    // Gửi list student tới client
                    sendStudentsToClient(foundStudents);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
