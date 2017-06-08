import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {
    private static final int portNumber = 8888;
    private static final int maxClientsNumber = 3;

    private static List<ClientThread> clientThreads;
    private static boolean isStarted = false;
    private static boolean finished = false;
    static final Object lock = new Object();

    public static void main(String[] args) {
        clientThreads = new ArrayList<>();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
//            Game game = new Game(new File("map1.txt"));
            while (clientThreads.size() < maxClientsNumber) {
                Socket socket = serverSocket.accept();
                ClientThread player = new ClientThread(socket, clientThreads.size());
                player.start();
                System.out.println("player = " + clientThreads.size());
                for (ClientThread th : clientThreads)
                    th.registerPlayer(clientThreads.size());
                clientThreads.add(player);
            }
            synchronized (lock) {
                isStarted = true;
                lock.notifyAll();
            }
            CommandThread commandThread = new CommandThread(clientThreads);
            commandThread.start();

        } catch (Exception e) {
            System.out.println("Exception: " + e.toString());
        }
    }

    static boolean checkStarted() {
        return isStarted;
    }

    static void closeSocket(ClientThread thread) {
        clientThreads.remove(thread);
    }

    static boolean isFinished() {
        return finished;
    }

}
