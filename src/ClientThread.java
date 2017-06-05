import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    private final Socket socket;
    private Game game;

    ClientThread(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
    }

    @Override
    public void run() {
        try {
            System.out.println("HERE");
            while (true) {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(game);
                synchronized (this) {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String input = dataInputStream.readUTF();
                    Main.addOp(input);
                    notifyAll();
                }
            }
        } catch (IOException e) {
            System.out.println("Error");
            Main.closeSocket(this);
        }
    }

    /*private void addOp(String input) {
        String op = input.substring(0, 1);
        String[] tmp = input.substring(1).split(",");
        int[] val = new int[tmp.length];
        for (int i = 0; i < tmp.length; i++)
            val[i] = Integer.valueOf(tmp[i]);
        Main.addOp(op, val);
    }*/
}
