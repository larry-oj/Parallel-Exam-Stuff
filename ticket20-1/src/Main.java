import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static boolean flag = true;
    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            try {
                while (true) {
                    lock.lock();
                    try {
                        flag = !flag; // Змінюємо стан флагу
                        if (flag) {
                            condition.signal(); // Сповіщаємо потік B про зміну стану
                        }
                    } finally {
                        lock.unlock();
                    }
                    Thread.sleep(1000); // Затримка 100 мс
                    System.out.println("State change");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread threadB = new Thread(() -> {
            try {
                while (true) {
                    lock.lock();
                    try {
                        condition.await(); // Очікуємо на стан true

                        // Виводимо значення масиву на консоль
                        int[] array = {1, 2, 3, 4, 5};
                        for (int i : array) {
                            System.out.println("Thread B: " + i);
                            Thread.sleep(10); // Затримка 10 мс
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Запускаємо обидва потоки
        threadA.start();
        threadB.start();
    }
}
