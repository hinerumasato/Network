package Teach.SocketJDBC;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class ServerProcess implements Runnable {

    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;
    private boolean exit;
    private StudentService service;

    public ServerProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new DataInputStream(socket.getInputStream());
        this.netOut = new DataOutputStream(socket.getOutputStream());
        this.service = new StudentService();
    }

    private void findByAge() throws IOException, ClassNotFoundException, SQLException {
        int age = netIn.readInt();
        List<Student> students = service.findByAge(age);
        // Gửi response tới cho client
        // Write students size
        netOut.writeInt(students.size());
        netOut.flush();
        for (Student student : students) {
            // Write student details
            netOut.writeInt(student.getId());
            netOut.flush();
            netOut.writeUTF(student.getName());
            netOut.flush();
            netOut.writeInt(student.getAge());
            netOut.flush();
            netOut.writeDouble(student.getScore());
            netOut.flush();
        }
    }

    private void insert() throws IOException, ClassNotFoundException, SQLException {
        String commandInsert = netIn.readUTF();
        String[] commandInsertSplit = commandInsert.split(" ");

        double score = Double.parseDouble(commandInsertSplit[commandInsertSplit.length - 1]);
        int age = Integer.parseInt(commandInsertSplit[commandInsertSplit.length - 2]);
        String name = "";

        for(int i = 0; i < commandInsertSplit.length - 2; i++)
            name += commandInsertSplit[i] + " ";
        name = name.trim();

        Student student = new Student(name, age, score);
        service.insert(student);

        String response = "Them sinh vien moi thanh cong";
        netOut.writeUTF(response);
        netOut.flush();
    }

    private void update() throws IOException, ClassNotFoundException, SQLException {
        String commandUpdate = netIn.readUTF();
        String[] commandInsertSplit = commandUpdate.split(" ");
        double score = Double.parseDouble(commandInsertSplit[commandInsertSplit.length - 1]);
        int age = Integer.parseInt(commandInsertSplit[commandInsertSplit.length - 2]);
        int id = Integer.parseInt(commandInsertSplit[0]);
        String name = "";
        for(int i = 1; i < commandInsertSplit.length - 2; i++)
            name += commandInsertSplit[i] + " ";
        name = name.trim();

        Student student = new Student(name, age, score);
        service.update(id, student);

        String response = "Update thanh cong";
        netOut.writeUTF(response);
        netOut.flush();
    }

    private void delete() throws NumberFormatException, IOException, ClassNotFoundException, SQLException {
        String commandValue = netIn.readUTF();
        int deleteId = Integer.parseInt(commandValue);
        service.delete(deleteId);

        String response = "Xoa sinh vien thanh cong";
        netOut.writeUTF(response);
        netOut.flush();
    }

    private void exit() {
        this.exit = true;
        System.out.println("Client " + socket.getInetAddress() + " has disconnected");
    }

    @Override
    public void run() {
        try {
            while (!exit) {
                String command = netIn.readUTF();
                switch (command) {
                    case "findByAge":
                        findByAge();
                        break;
                    case "insert":
                        insert();
                        break;
                    case "update":
                        update();
                        break;
                    case "delete":
                        delete();
                        break;
                    case "exit":
                        exit();
                        break;
                    default:
                        break;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
