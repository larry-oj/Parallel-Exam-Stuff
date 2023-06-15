import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    private static final int BUFFER_SIZE = 10;
    private static final int OPERATIONS_COUNT = 1000;

    private static ArrayBlockingQueue<Object> buffer = new ArrayBlockingQueue<>(BUFFER_SIZE);

    public static void main(String[] args) {
        Thread producerThread = new Thread(new Producer());
        Thread consumerThread = new Thread(new Consumer());

        producerThread.start();
        consumerThread.start();

        try {
            // Очікування завершення виконання потоків
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All threads finished execution.");
    }

    static class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < OPERATIONS_COUNT; i++) {
                Object obj = new Object();
                try {
                    buffer.put(obj); // Додавання об'єкта до буфера
                    System.out.println("Producer: Added object " + obj);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < OPERATIONS_COUNT; i++) {
                try {
                    Object obj = buffer.take(); // Вилучення об'єкта з буфера
                    System.out.println("Consumer: Removed object " + obj);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
