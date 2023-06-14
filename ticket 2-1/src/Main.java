import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        ThreadA threadA = new ThreadA();

        Thread thread = new Thread(() -> threadA.performActions());

        thread.start();
    }
}


class ThreadA {
    private boolean isEmpty = true;
    private boolean isFull = false;

    private Lock lock = new ReentrantLock();
    private Condition emptyCondition = lock.newCondition();
    private Condition fullCondition = lock.newCondition();

    public void performActions() {
        lock.lock();
        try {
            while (isEmpty) {
                emptyCondition.await();
            }

            // Виконання дії actionEmpty
            System.out.println("actionEmpty");

            isEmpty = !isEmpty;
            isFull = !isFull;
            fullCondition.signal();

            while (isFull) {
                fullCondition.await();
            }

            // Виконання дії actionFull
            System.out.println("actionFull");

            isEmpty = !isEmpty;
            isFull = !isFull;
            emptyCondition.signal();
        } catch (InterruptedException e) {
            // Обробка переривання
        } finally {
            lock.unlock();
        }
    }
}
