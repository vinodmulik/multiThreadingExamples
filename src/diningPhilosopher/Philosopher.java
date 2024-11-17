package diningPhilosopher;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class Philosopher implements Runnable{

    String name;
    List<Lock> forks;
    int leftIndex;
    int rightIndex;
    Semaphore diningTableSemaphore;

    public Philosopher(String name, List<Lock> forks, int leftIndex, int rightIndex,Semaphore diningTableSemaphore) {
        this.name = name;
        this.forks = forks;
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
        this.diningTableSemaphore = diningTableSemaphore;
    }

    @Override
    public void run() {
        try {
            diningTableSemaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        think();
        pickForks();
        eat();
        putForks();
        diningTableSemaphore.release();
    }

    private void putForks() {
        forks.get(leftIndex).unlock();
        forks.get(rightIndex).unlock();
        System.out.println(name + " put down forks");
    }

    private void eat() {
        System.out.println(name + " eating");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void pickForks() {
        forks.get(leftIndex).lock();
        forks.get(rightIndex).lock();
        System.out.println(name + " picked forks");
    }

    private void think() {
        System.out.println(name + " thinking");
    }
}
