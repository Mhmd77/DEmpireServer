import java.io.DataOutputStream;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {
    private static final int portNumber = 8888;
    private static final int maxClientsNumber = 5;
    private static List<ClientThread> clientThreads;
    private static List<String> ops;

    public static void main(String[] args) {
        ops = new ArrayList<>();
        clientThreads = new ArrayList<>();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
            Game game = new Game(new File("map1.txt"));
            while (true) {
                Socket socket = serverSocket.accept();
                if (clientThreads.size() < maxClientsNumber) {
                    ClientThread player = new ClientThread(socket, game);
                    player.start();
                    clientThreads.add(player);
                } else {
                    System.out.println("Max Size Reached");
                    clientThreads.clear();
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public static void addOp(String op) {
        ops.add(op);
        System.out.println(ops.size() + ": " + op);
    }


    public static void closeSocket(ClientThread thread) {
        clientThreads.remove(thread);
    }
}
