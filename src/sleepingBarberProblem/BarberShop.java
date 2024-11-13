package sleepingBarberProblem;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BarberShop {

    final static int SLEEP_TIME = 2000;
    public final static int BARBER_SHOP_QUEUE_SIZE = 5;
    static int customerCount = 0;

    //main class
    public static void main(String[] args) {
        var customerSemaphore = new Semaphore(0);
        var barberSemaphore = new Semaphore(0);
        var barberEndSemaphore = new Semaphore(0);
        var customerEndSemaphore = new Semaphore(0);
        var customerLock = new ReentrantLock();

        var barber = new Thread(new Barber(barberSemaphore,customerSemaphore,customerEndSemaphore,barberEndSemaphore));

        var customers = IntStream.range(0,10).mapToObj(i -> new Thread(new Customer(barberSemaphore,customerSemaphore,customerEndSemaphore,barberEndSemaphore,customerLock), "Customer_" + i)).collect(Collectors.toList());

        barber.start();
        sleep();
        customers.forEach(Thread::start);
    }

    public static void sleep() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void acquire(Semaphore semaphore) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
