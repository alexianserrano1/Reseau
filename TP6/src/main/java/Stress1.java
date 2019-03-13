import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Stress1 {
    public static void main(String[] args) {
        String[] arguments = {"localhost", "1235"};
        int clientNumber = Integer.parseInt(args[0]);

        for(int index = 0; index < clientNumber; index++) {
            System.out.println("Lancement du client "+ index);
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new StressHandler(arguments));
        }
    }

    static class StressHandler implements Runnable {
        String[] address_port;

        public StressHandler(String[] address_port) {
            this.address_port = address_port;
        }

        public void run() {
            ClientTCP.main(address_port);
        }
    }
}
