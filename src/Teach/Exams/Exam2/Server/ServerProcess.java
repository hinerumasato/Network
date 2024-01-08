package Teach.Exams.Exam2.Server;

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
        this.netOut = new PrintWriter(socket.getOutputStream());
        this.isLogin = false;
        this.isExit = false;
        this.service = new Service();
    }

    @Override
    public void run() {
        try {
            netOut.println("WELCOME TO MANAGE PRODUCT SYSTEM");
            netOut.flush();

            while (!isExit) {
                if (!isLogin) {
                    String userCommand = netIn.readLine();
                    if (userCommand.startsWith("USER")) {
                        String username = userCommand.split("\t")[1];
                        try {
                            boolean isExistUsername = service.isExistUsername(username);
                            if (isExistUsername) {
                                netOut.println("OK");
                                netOut.flush();

                                String passwordCommand = netIn.readLine();
                                String password = passwordCommand.split("\t")[1];

                                try {
                                    boolean isValidUser = service.isValidUser(username, password);
                                    if (isValidUser) {
                                        netOut.println("OK");
                                        netOut.flush();
                                        isLogin = true;
                                    } else {
                                        netOut.println("FALSE");
                                        netOut.flush();
                                    }
                                } catch (ClassNotFoundException | SQLException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                netOut.println("FALSE");
                                netOut.flush();
                            }
                        } catch (ClassNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }

                    } else {
                        isExit = true;
                        System.out.println("Client " + socket.getInetAddress() + " has disconnected");
                    }

                } else {
                    // Xử lý truy vấn từ phía client
                    String command = netIn.readLine();
                    StringTokenizer tokenizer = new StringTokenizer(command, "\t");
                    String commandName = tokenizer.nextToken();
                    switch (commandName) {
                        case "ADD":
                            String id = tokenizer.nextToken();
                            String name = tokenizer.nextToken();
                            int amount = Integer.parseInt(tokenizer.nextToken());
                            double price = Double.parseDouble(tokenizer.nextToken());

                            Product product = new Product(id, name, amount, price);
                            try {
                                service.insert(product);
                                netOut.println("OK");
                                netOut.flush();
                            } catch (ClassNotFoundException | SQLException e) {
                                netOut.println("ERROR");
                                netOut.flush();
                                e.printStackTrace();
                            }
                            break;
                        case "REMOVE":
                            List<String> ids = new ArrayList<String>();
                            while(tokenizer.countTokens() > 0) {
                                String _id = tokenizer.nextToken();
                                ids.add(_id);
                            }

                            try {
                                int rowsAffected = service.delete(ids);
                                netOut.println(rowsAffected);
                                netOut.flush();
                            } catch (ClassNotFoundException | SQLException e) {
                                e.printStackTrace();
                            }
                        case "EDIT":
                            String uId = tokenizer.nextToken();
                            String uName = tokenizer.nextToken();
                            int uAmount = Integer.parseInt(tokenizer.nextToken());
                            double uPrice = Double.parseDouble(tokenizer.nextToken());

                            Product uProduct = new Product(uId, uName, uAmount, uPrice);
                            try {
                                service.update(uProduct);
                                netOut.println("OK");
                                netOut.flush();
                            } catch(ClassNotFoundException | SQLException e) {
                                netOut.println("CAN NOT UPDATE");
                                netOut.flush();
                                e.printStackTrace();
                            }
                            break;
                        case "VIEW":
                            String param = tokenizer.nextToken();
                            try {
                                String data = service.search(param);
                                StringTokenizer dataTokenizer = new StringTokenizer(data, "\n");
                                int length = dataTokenizer.countTokens();
                                netOut.println(length);
                                netOut.flush();

                                for(int i = 0; i < length; i++) {
                                    String line = dataTokenizer.nextToken();
                                    netOut.println(line);
                                    netOut.flush();
                                }
                            } catch(ClassNotFoundException | SQLException e) {
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
