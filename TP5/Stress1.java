public class Stress1 {
    public static void main(String[] args) {
        String[] arguments = {"localhost", "1234"};
        int clientNumber = Integer.parseInt(args[0]);

        for(int index = 0; index < clientNumber; index++) {
            Thread thread = new Thread(new StressHandler(arguments));
            thread.start();
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
