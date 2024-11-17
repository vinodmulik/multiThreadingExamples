package diningPhilosopher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class DiningTable {

    private static final int CAPACITY = 5;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        List<Lock> forks = new ArrayList<>(CAPACITY);
        IntStream.range(0,CAPACITY).forEach(i -> forks.add(i,new ReentrantLock()));
        //If all philosophers are allowed at the dining table , there can be possibility of deadlock.
        //To avoid deadlock , the number of philosophers allowed at a time is restricted using semaphore.
        Semaphore diningTableSemaphore = new Semaphore(CAPACITY-1);
        List<Philosopher> philosophers = new LinkedList<>();
        IntStream.range(0,CAPACITY).forEach(i -> philosophers.add(new Philosopher("Philosopher_" + i,forks,i,(i%CAPACITY),diningTableSemaphore)));
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try{
            philosophers.forEach(philosopher -> executorService.execute(philosopher));
        }finally {
            executorService.shutdown();
        }
    }
}
