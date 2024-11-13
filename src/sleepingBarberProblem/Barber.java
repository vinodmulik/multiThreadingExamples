package sleepingBarberProblem;

import java.util.concurrent.Semaphore;

public class Barber implements Runnable {

    private final Semaphore barberSemaphore;
    private final Semaphore customerSemaphore;
    private final Semaphore customerEndSemaphore;
    private final Semaphore barberEndSemaphore;

    Barber(Semaphore barberSemaphore, Semaphore customerSemaphore,Semaphore customerEndSemaphore,Semaphore barberEndSemaphore){
        this.barberSemaphore = barberSemaphore;
        this.customerSemaphore = customerSemaphore;
        this.customerEndSemaphore = customerEndSemaphore;
        this.barberEndSemaphore = barberEndSemaphore;
    }

    @Override
    public void run() {
        while (true) {
            BarberShop.acquire(customerSemaphore);
            barberSemaphore.release();
            doWork();
            barberEndSemaphore.release();
            BarberShop.acquire(customerEndSemaphore);
            workDone();
            BarberShop.sleep();
        }
    }

    private void workDone() {
        System.out.println("[BARBER] Work Done");
    }

    private void doWork() {
        System.out.println("[BARBER] Doing work");
    }
}
