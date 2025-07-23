import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to the server");
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true){
            System.out.println("Waiting for client...");
            new ClientHandler(serverSocket.accept()).start();
        }
    }
}
class ClientHandler extends Thread{
    Socket socket;
    DataOutputStream dos;
    DataInputStream dis;
    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());
        System.out.println("Connected to server");
    }

    public String listener() throws IOException{
        System.out.println("listener");
        StringBuilder sb = new StringBuilder();
        int index = dis.read();
        while (index != 0){
            sb.append((char) index);
            index = dis.read();
        }
        System.out.println("listener2");
        return sb.toString();
    }

    public void writer(String number) throws IOException{

    }

    @Override
    public void run() {
        super.run();
        String command;
        try {
            command = listener();
            System.out.println("command received: { " + command + " }");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] split = command.split("$");//login$402243000$Teryak1999
        for (String s : split){
            System.out.println(s);
        }
        switch (split[0]){
            case "login": {
                boolean logIn = false;
                int responseOfDatabase = 100;
                try {
                    responseOfDatabase = Database.loginChecker(split[1], split[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (responseOfDatabase == 2) {
                    logIn = true;
                    System.out.println("status code is 200");
                    System.out.println("Successfully logged in!");
                    try {
                        writer("200");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (responseOfDatabase == 1) {
                    System.out.println("status code is 401");
                    System.out.println("Password is incorrect!");
                    try {
                        writer("401");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    System.out.println("status code is 404");
                    System.out.println("username not found!");
                    try {
                        writer("404");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (logIn){
                    long l = Long.parseLong(split[1]);
                    Student student = null;
                    try {
                        student = new Student(l);
                        cli.studentAfterLog(student);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }



                }
            }

        }
    }
}
