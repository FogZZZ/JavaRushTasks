package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.kitchen.Order;
import com.javarush.task.task27.task2712.statistic.StatisticManager;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class OrderManager implements Observer {
    private LinkedBlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();

    public OrderManager() {
        Thread daemon = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Set<Cook> allCooks = StatisticManager.getInstance().getCooks();
                    while (true) {
                        for (Cook cook : allCooks) {
                            if (!cook.isBusy()) {
                                if (!orderQueue.isEmpty()) {
                                    Order order = orderQueue.take();
                                    cook.startCookingOrder(order);
                                }
                            }
                        }
                        Thread.sleep(10);
                    }
                } catch (InterruptedException e) {}
            }
        });
        daemon.setDaemon(true);
        daemon.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            orderQueue.put((Order)arg);
        } catch (InterruptedException e) {}
    }
}
