import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    private final Socket socket;
    private int id;

    ClientThread(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            synchronized (Main.lock) {
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeInt(this.id);
                while (!Main.checkStarted())
                    Main.lock.wait();
            }
            DataOutputStream outputStream = null;
            try {
                outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeBoolean(true);
                System.out.println("Game Started! " + this.id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("Error");
            Main.closeSocket(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            while (!Main.isFinished()) {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                CommandHandler.addOp(dataInputStream.readUTF());
            }
        } catch (IOException e) {

        }
    }

    void registerPlayer(int id) {
        DataOutputStream outputStream = null;
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeBoolean(false);
            outputStream.writeInt(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendOp(String op) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(op);

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
