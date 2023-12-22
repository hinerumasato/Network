package Teach.Expression;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerProcess implements Runnable {

    private Socket socket;
    private BufferedReader netIn;
    private PrintWriter netOut;
    private boolean exit;

    public ServerProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.netOut = new PrintWriter(socket.getOutputStream());
        this.exit = false;
    }

    private String commandHandler(String command) {
        String result = command;
        result += " = ";
        String[] commandSplit = command.split(" ");

        int firstNumber = Integer.parseInt(commandSplit[0]);
        String expression = commandSplit[1];
        int secondNumber = Integer.parseInt(commandSplit[2]);

        switch (expression) {
            case "+":
                result += (firstNumber + secondNumber);
                break;
            case "-":
                result += (firstNumber - secondNumber);
                break;
            case "*":
                result += (firstNumber * secondNumber);
                break;
            case "/":
                if(secondNumber == 0)
                    result = "Phep toan khong hop le";
                else {
                    result += (firstNumber / secondNumber);
                }
                break;
            default:
                break;
        }
        return result;
    } 

    @Override
    public void run() {
        try {
            while(!exit) {
                String command = netIn.readLine();
                if(command.equalsIgnoreCase("exit")) {
                    exit = true;
                    System.out.println("Client has disconnected");
                } else {
                    String result = commandHandler(command);
                    netOut.println(result);
                    netOut.flush();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        } 
    }

}
