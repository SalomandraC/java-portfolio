package org.example;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

class People {
    public final Integer first;
    public final Integer second;
    public final Integer diff;
    final CountDownLatch done = new CountDownLatch(1);
    People (Integer first, Integer second) {
        this.first = first;
        this.second = second;
        this.diff = Math.abs(second - first);
    }
}

public class Main {
    static final int  floors = 10;
    static final long timeForFloor = 5;
    static final int  seed = 42;
    static final int[] pValues = {5, 10, 20, 40, 80};
    static final int repeat = 5;

    static final People exit = new People(-1, -1);

    static void main() {
        for (int i = 0; i < 3; ++ i){
            List<People > list = generate(80);
        }
        System.out.printf("Здание: %d этажей, %d мс/этаж%n", floors, timeForFloor);
        System.out.printf("%5s | %14s | %14s | %10s%n", "P", "standard, ms", "concurrent, ms", "overhead, %");
        for (int p : pValues){
            List<People> generatedList = generate(p);
            double base = median(repeat, () -> {
                return Watcher(() -> standardStart(generatedList));
            });
            double conc = median(repeat, () -> {
                return Watcher(() -> cuncurencyLoop(generatedList));
            });
            double overhead = (conc - base) / base * 100.0;
            System.out.printf("%5d | %10.3f | %10.3f | %9.2f%%%n", p, base, conc, overhead);
        }
    }

    private static void standardStart(List<People > inpList) {
        Integer current = 1;
        for (People  p : inpList) {
            long move = (long) Math.abs(current - p.first) * timeForFloor;
            current = p.first;
            try {
                Thread.sleep(move + p.diff * timeForFloor);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            current = p.second;
        }
    }

    private static void cuncurencyLoop(List<People > inpList) {
        BlockingQueue queue = new ArrayBlockingQueue(inpList.size() + 1);
        AtomicInteger served = new AtomicInteger(0);

        Thread elevator = new Thread(() -> {
            elevLoop(queue, served);
        });
        elevator.start();

        List<Thread> PeopleThreads = new ArrayList<>(inpList.size());

        for (People p : inpList){
            Thread t = new Thread(() -> {
                try {
                    queue.put(p);
                    p.done.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            t.start();
            PeopleThreads.add(t);
        }

        for (Thread iThread : PeopleThreads){
            try {
                iThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            queue.put(exit);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        try {
            elevator.join();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (served.get() != inpList.size()) {
            throw new AssertionError("Инвариант нарушен");
        }
    }

    static void elevLoop(BlockingQueue<People> queue, AtomicInteger servedCount) {
        int current = 1;
        try{
            while (true) {
                People p = queue.take();
                if(p == exit) return;
                long move = (long) Math.abs(current - p.first) * timeForFloor;
                current = p.first;
                try {
                    Thread.sleep(move + p.diff * timeForFloor);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                current = p.second;
                p.done.countDown();
                servedCount.incrementAndGet();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static double Watcher(Runnable task) {
        long start = System.nanoTime();
        task.run();
        return (System.nanoTime() - start) / 1e6;
    }

    private static List<People> generate(int n) {
        Random rnd = new Random(seed);
        List<People > list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            list.add(new People (rnd.nextInt(floors) + 1, rnd.nextInt(floors) + 1));
        }
        return list;
    }

    static double median(int reps, Supplier<Double> task) {
        double[] arr = new double[reps];
        for (int i = 0; i < reps; i++)
            arr[i] = task.get();
        Arrays.sort(arr);
        return arr[reps / 2];
    }
}
