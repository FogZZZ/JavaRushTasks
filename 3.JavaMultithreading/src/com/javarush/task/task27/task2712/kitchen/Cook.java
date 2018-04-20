package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;

import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;

public class Cook implements Runnable {
    private String name;
    private boolean busy;
    private LinkedBlockingQueue<Order> queue;
    private LinkedBlockingQueue<Order> completedOrderQueue;

    public Cook(String name) {
        this.name = name;
    }

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    public void setCompletedOrderQueue(LinkedBlockingQueue<Order> completedOrderQueue) {
        this.completedOrderQueue = completedOrderQueue;
    }

    public boolean isBusy() {
        return busy;
    }

    public void startCookingOrder(Order order) {
        busy = true;
        ConsoleHelper.writeMessage("Start cooking by " + order.getCook() + " - " + order + ", cooking time " + order.getTotalCookingTime() + "min");
        ConsoleHelper.writeMessage("");

        try {
            //готовится...
            Thread.sleep(order.getTotalCookingTime()*10);

            //готово!
            completedOrderQueue.put(order);

        } catch (InterruptedException e) {}

        StatisticManager.getInstance().register(new CookedOrderEventDataRow(order.getTablet().toString(),
                this.name, order.getTotalCookingTime()*60, order.getDishes()));
        busy = false;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = queue.take();
                order.setCook(this);
                startCookingOrder(order);
            }
        } catch (InterruptedException e) {}
    }

    @Override
    public String toString() {
        return name;
    }
}
