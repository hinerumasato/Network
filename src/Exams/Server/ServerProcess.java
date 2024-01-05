package Exams.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ServerProcess implements Runnable {
    private Socket socket;
    private BufferedReader netIn;
    private PrintWriter netOut;
    private boolean isLogin;
    private boolean isExit;
    private Service service;

    public ServerProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.netOut = new PrintWriter(socket.getOutputStream(), true);
        this.service = new Service();
        this.isLogin = false;
        this.isExit = false;
    }

    @Override
    public void run() {
        try {
            netOut.println("WELCOME TO MANAGE PRODUCT SYSTEM");
            while (!isExit) {
                if (!isLogin) {
                    String userCommand = netIn.readLine();
                    if (userCommand.startsWith("user\t")) {
                        String username = userCommand.split("\t")[1];
                        try {
                            if (service.isContainUsername(username)) {
                                netOut.println("OK");
                                String passwordCommand = netIn.readLine();
                                String password = passwordCommand.split("\t")[1];
                                if (service.isValidUser(username, password)) {
                                    netOut.println("OK");
                                    isLogin = true;
                                } else {
                                    netOut.println("FALSE");
                                }
                            } else {
                                netOut.println("FALSE");
                            }
                        } catch (ClassNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        isExit = true;
                        System.out.println("Client " + socket.getInetAddress() + "has disconnected");
                    }
                } else {
                    String command = netIn.readLine();
                    StringTokenizer tokenizer = new StringTokenizer(command, "\t");
                    String commandName = tokenizer.nextToken();
                    switch (commandName) {
                        case "ADD":
                            int id = Integer.parseInt(tokenizer.nextToken());
                            String name = tokenizer.nextToken();
                            int amount = Integer.parseInt(tokenizer.nextToken());
                            double price = Double.parseDouble(tokenizer.nextToken());
                            Product product = new Product(id, name, amount, price);
                            try {
                                service.insertProduct(product);
                                netOut.println("OK");
                            } catch (ClassNotFoundException | SQLException e) {
                                netOut.println("ERROR");
                            }
                            break;
                        case "REMOVE":
                            List<Integer> ids = new ArrayList<Integer>();
                            int length = tokenizer.countTokens();
                            for (int i = 0; i < length; i++)
                                ids.add(Integer.parseInt(tokenizer.nextToken()));
                            try {
                                int rowsAffected = service.removeProduct(ids);
                                netOut.println(rowsAffected);

                            } catch (ClassNotFoundException | SQLException e) {
                                netOut.println("ERROR");
                            }
                            break;
                        case "EDIT":
                            int uId = Integer.parseInt(tokenizer.nextToken());
                            String uName = tokenizer.nextToken();
                            int uAmount = Integer.parseInt(tokenizer.nextToken());
                            double uPrice = Double.parseDouble(tokenizer.nextToken());
                            Product uProduct = new Product(uId, uName, uAmount, uPrice);
                            try {
                                service.updateProduct(uProduct);
                                netOut.println("OK");
                            } catch (ClassNotFoundException | SQLException e) {
                                netOut.println("CAN NOT UPDATE");
                            }
                            break;
                        case "VIEW":
                            String search = tokenizer.nextToken();
                            try {
                                String data = service.view(search);
                                System.out.println(data);
                                StringTokenizer dataTokenizer = new StringTokenizer(data, "\n");
                                int size = dataTokenizer.countTokens();
                                netOut.println(size);
                                for (int i = 0; i < size; i++) {
                                    String line = dataTokenizer.nextToken();
                                    netOut.println(line);
                                }
                            } catch (ClassNotFoundException | SQLException e) {
                                netOut.println("NOT FOUND");
                                e.printStackTrace();
                            }
                            break;
                        case "QUIT":
                            isLogin = false;
                            break;
                        default:
                            break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
