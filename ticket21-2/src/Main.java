public class Main {
    public static void main(String[] args) {
        Monitor monitor = new Monitor();

        Thread threadM = new Thread(new ThreadM(monitor));
        Thread threadN = new Thread(new ThreadN(monitor));

        threadM.start();
        threadN.start();
    }

    static class Monitor {
        private boolean state = true;

        public synchronized void setState(boolean newState) {
            state = newState;
            notifyAll();
        }

        public synchronized boolean getState() {
            return state;
        }
    }

    static class ThreadM implements Runnable {
        private final Monitor monitor;

        public ThreadM(Monitor monitor) {
            this.monitor = monitor;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(1000);  // Delay 1000 milliseconds

                    boolean newState = !monitor.getState();  // Switch state to the opposite
                    monitor.setState(newState);  // Set the new state
                    System.out.println("State changed to " + newState);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class ThreadN implements Runnable {
        private final Monitor monitor;

        public ThreadN(Monitor monitor) {
            this.monitor = monitor;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    synchronized (monitor) {
                        while (!monitor.getState()) {
                            monitor.wait();  // Wait until the state of Thread M is true
                        }

                        // Output seconds count
                        for (int i = 1; i <= 10; i++) {
                            System.out.println("Seconds: " + i);
                            Thread.sleep(1000);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
