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
    private final Object lock = new Object();

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Thread.sleep(1000);
                synchronized (lock) {
                    state = !state;
                    System.out.println("S state: " + state);
                    lock.notify(); // Notify waiting thread
                    lock.wait(); // Wait for notification from the other thread
                }
            } catch (InterruptedException e) {
                // Handle interruption
                return;
            }
        }
    }

    public boolean getThreadState() {
        return state;
    }

    public void waitForNotification() throws InterruptedException {
        synchronized (lock) {
            lock.wait();
        }
    }

    public void notifyThread() {
        synchronized (lock) {
            lock.notify();
        }
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
                    sThread.waitForNotification();
                }
                System.out.println("W countdown: " + i);
                Thread.sleep(100);
                sThread.notifyThread(); // Notify SThread to continue
            }
            interrupt();
            sThread.interrupt();
        } catch (InterruptedException e) {}
    }
}
