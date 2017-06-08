import java.util.List;

public class CommandThread extends Thread {
    private List<ClientThread> clients;

    CommandThread(List<ClientThread> clients) {
        this.clients = clients;
    }

    @Override
    public void run() {
        while (!Main.isFinished()) {
            CommandHandler.sendCommands(clients);
        }
    }

}
