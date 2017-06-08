import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandHandler {
    private static List<String> ops;
    static {
        ops = Collections.synchronizedList(new ArrayList<>());
    }
    public static void addOp(String op) {
        synchronized (ops) {
            ops.add(op);
            System.out.println(ops.size() + ": " + op);
            ops.notifyAll();
        }
    }

    static void sendCommands(List<ClientThread> clientThreads) {
        List<String> toRemove = new ArrayList<>();

        try {
            synchronized (ops) {
                while (ops.isEmpty())
                    ops.wait();
                for (String op :
                        ops) {
                    int id = Integer.parseInt(op.substring(0, 1));
                    for (int i = 0; i < clientThreads.size(); i++) {
                        if (i == id)
                            continue;
                        clientThreads.get(i).sendOp(op);
                    }
                    toRemove.add(op);
                }
                ops.removeAll(toRemove);

            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception :" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException :" + e.getMessage());

        }

    }
}
