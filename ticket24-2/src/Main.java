import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int NUMBER_OF_CASHIERS = 9;
    private static final int SIMULATION_TIME_MINUTES = 60;

    private static int maxQueueTime = 0;
    private static boolean isQueueEmpty = true;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_CASHIERS);

        for (int i = 0; i < NUMBER_OF_CASHIERS; i++) {
            executor.execute(new Cashier());
        }

        executor.shutdown();

        try {
            executor.awaitTermination(SIMULATION_TIME_MINUTES, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Maximum observed queue time: " + maxQueueTime + " minutes");
    }

    static class Cashier implements Runnable {
        private Random random = new Random();

        @Override
        public void run() {
            int queueTime = 0;

            for (int currentTime = 0; currentTime < SIMULATION_TIME_MINUTES; currentTime++) {
                if (random.nextDouble() < 1.0 / 60.0) {
                    if (isQueueEmpty) {
                        queueTime = 1; // Клієнт надходить, черга починається
                        isQueueEmpty = false;
                    } else {
                        queueTime++; // Черга продовжується
                    }
                    maxQueueTime = Math.max(maxQueueTime, queueTime);
                }

                if (!isQueueEmpty && random.nextDouble() < 1.0 / 10.0) {
                    queueTime--; // Клієнт обслуговується
                    if (queueTime == 0) {
                        isQueueEmpty = true; // Черга порожняє
                    }
                }
            }
        }
    }
}
