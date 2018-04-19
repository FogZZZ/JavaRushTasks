package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.kitchen.Waiter;
import com.javarush.task.task27.task2712.statistic.StatisticManager;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private static final int ORDER_CREATING_INTERVAL = 100;

    public static void main(String[] args) {
        //создание всех нужных объектов
        Cook cook1 = new Cook("Amigo");
        Cook cook2 = new Cook("Андрей");
        Waiter waiter = new Waiter();
        DirectorTablet directorTablet = new DirectorTablet();
        List<Tablet> allTablets = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            allTablets.add(new Tablet(i));
        }
        OrderManager orderManager = new OrderManager();

        //регистрация в хранилищах, регистрация наблюдателей
        StatisticManager.getInstance().register(cook1);
        StatisticManager.getInstance().register(cook2);
        cook1.addObserver(waiter);
        cook2.addObserver(waiter);
        for (int i = 0; i < 5; i++) {
            allTablets.get(i).addObserver(orderManager);
        }

        //Создаем таск на генерацию случайных заказов (прерываем через 1 сек)
        Thread randomOrderGeneratorTask = new Thread(new RandomOrderGeneratorTask(allTablets, ORDER_CREATING_INTERVAL));
        randomOrderGeneratorTask.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        randomOrderGeneratorTask.interrupt();

        /*directorTablet.printAdvertisementProfit();
        directorTablet.printCookWorkloading();
        directorTablet.printActiveVideoSet();
        directorTablet.printArchivedVideoSet();*/
    }
}