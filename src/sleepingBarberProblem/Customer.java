package sleepingBarberProblem;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class Customer implements Runnable{

    private final Semaphore barberSemaphore;
    private final Semaphore customerSemaphore;
    private final Semaphore customerEndSemaphore;
    private final Semaphore barberEndSemaphore;
    private Lock customerLock;

    Customer(Semaphore barberSemaphore,Semaphore customerSemaphore,Semaphore customerEndSemaphore,Semaphore barberEndSemaphore,Lock customerLock) {
        this.barberSemaphore = barberSemaphore;
        this.customerSemaphore = customerSemaphore;
        this.customerEndSemaphore = customerEndSemaphore;
        this.barberEndSemaphore = barberEndSemaphore;
        this.customerLock = customerLock;
    }

    @Override
    public void run() {
        customerLock.lock();
        if(BarberShop.customerCount == BarberShop.BARBER_SHOP_QUEUE_SIZE) {
            System.out.println("Queue is full." + Thread.currentThread().getName() + " left");
            customerLock.unlock();
            return;
        }
        BarberShop.customerCount++;
        System.out.println("Count : " + BarberShop.customerCount);
        var threadName = Thread.currentThread().getName();
        System.out.println("Customer entering queue : " + threadName);
        customerLock.unlock();

        customerSemaphore.release();
        BarberShop.acquire(barberSemaphore);

        System.out.println("Customer getting served : " + threadName);

        BarberShop.acquire(barberEndSemaphore);
        customerEndSemaphore.release();

        customerLock.lock();
        BarberShop.customerCount--;
        System.out.println("Customer leaving shop after getting served : " + threadName);
        customerLock.unlock();

        BarberShop.sleep();
    }
}
