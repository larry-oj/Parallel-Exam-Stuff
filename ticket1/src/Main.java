// Напишіть код, в якому створюються та запускаються на виконання потоки S і W. Потік S виконує переключення з затримкою 1000 мілісекунд зі стану true у стан false і навпаки. Потік W очікує стану true потоку S, виводить на консоль зворотний відлік від 30 з затримкою 100 мілісекунд та призупиняє свою дію, як тільки потік S переключено у стан false. Умовою завершення роботи потоків є досягнення відліку нульової відмітки.

// Напишіть код, в якому створюються та запускаються на виконання потоки S і W.
// Потік S виконує переключення з затримкою 1000 мілісекунд зі стану true у стан
// false і навпаки. Потік W очікує стану true потоку S, виводить на консоль
// зворотний відлік від 30 з затримкою 100 мілісекунд та призупиняє свою дію, як
// тільки потік S переключено у стан false. Умовою завершення роботи потоків є
// досягнення відліку нульової відмітки.

public class Main {
    public static void main(String[] args) {
        SThread sThread = new SThread();
        WThread wThread = new WThread(sThread);

        sThread.start();
        wThread.start();
    }
}

class SThread extends Thread {
    private boolean state = true;

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Thread.sleep(1000);
                state = !state;
                System.out.println("S state: " + state);
            } catch (InterruptedException e) {
                // Handle interruption
                return;
            }
        }
    }

    public boolean getThreadState() {
        return state;
    }
}

class WThread extends Thread {
    private final SThread sThread;

    public WThread(SThread sThread) {
        this.sThread = sThread;
    }

    @Override
    public void run() {
        try {
            // Output countdown from 30
            for (int i = 30; i >= 0; i--) {
                // Wait until the state of SThread is true
                while (!sThread.getThreadState()) {
                    Thread.sleep(100);
                }
                System.out.println("W countdown: " + i);
                Thread.sleep(100);
            }
            interrupt();
            sThread.interrupt();
        } catch (InterruptedException e) {}
    }
}
