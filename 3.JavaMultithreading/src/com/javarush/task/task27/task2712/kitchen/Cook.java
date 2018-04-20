package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;

import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;

public class Cook extends Observable implements Runnable {
    private String name;
    private boolean busy;
    private LinkedBlockingQueue<Order> queue;

    public Cook(String name) {
        this.name = name;
    }

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    public boolean isBusy() {
        return busy;
    }

    public void startCookingOrder(Order order) {
        busy = true;
        ConsoleHelper.writeMessage("Start cooking - " + order + ", cooking time " + order.getTotalCookingTime() + "min");

        //готовится...
        try {
            Thread.sleep(order.getTotalCookingTime()*10);
        } catch (InterruptedException e) {}

        //готово!
        setChanged();
        notifyObservers(order);
        StatisticManager.getInstance().register(new CookedOrderEventDataRow(order.getTablet().toString(),
                this.name, order.getTotalCookingTime()*60, order.getDishes()));
        busy = false;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = queue.take();
                startCookingOrder(order);
            }
        } catch (InterruptedException e) {}
    }

    @Override
    public String toString() {
        return name;
    }
}
