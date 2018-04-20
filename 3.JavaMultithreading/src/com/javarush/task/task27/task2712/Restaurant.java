package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.kitchen.Order;
import com.javarush.task.task27.task2712.kitchen.Waiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Restaurant {
    private static final int ORDER_CREATING_INTERVAL = 100;
    private final static LinkedBlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        //создание всех нужных объектов
        Cook cook1 = new Cook("Amigo");
        Cook cook2 = new Cook("Андрей");
        cook1.setQueue(orderQueue);
        cook2.setQueue(orderQueue);
        Thread cookThread1 = new Thread(cook1);
        Thread cookThread2 = new Thread(cook2);
        cookThread1.start();
        cookThread2.start();

        Map<Tablet, Boolean> allTablets = new ConcurrentHashMap<>();
        for (int i = 1; i <= 5; i++) {
            Tablet tablet = new Tablet(i);
            tablet.setQueue(orderQueue);
            allTablets.put(tablet, true);
        }
        Waiter waiter = new Waiter(allTablets);

        cook1.addObserver(waiter);
        cook2.addObserver(waiter);

        //Создаем таск на генерацию случайных заказов (прерываем через 1 сек)
        Thread randomOrderGeneratorTask = new Thread(new RandomOrderGeneratorTask(allTablets, ORDER_CREATING_INTERVAL));
        randomOrderGeneratorTask.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        randomOrderGeneratorTask.interrupt();

        /*DirectorTablet directorTablet = new DirectorTablet();

        directorTablet.printAdvertisementProfit();
        directorTablet.printCookWorkloading();
        directorTablet.printActiveVideoSet();
        directorTablet.printArchivedVideoSet();*/
    }
}