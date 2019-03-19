package TP5;

public class StressMaster {
    public static void main(String[] args) {
        for(int i = 0; i < 5; i++)
            new Thread(new Handler()).start();
    }

    static class Handler implements Runnable {

        public void run() {
            String[] arguments = {"1000"};
            Stress1.main(arguments);
        }
    }
}
