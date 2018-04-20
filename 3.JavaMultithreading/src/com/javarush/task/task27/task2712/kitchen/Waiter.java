package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.Tablet;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;

public class Waiter implements Runnable {
    private Map<Tablet, Boolean> allTablets;
    private LinkedBlockingQueue<Order> completedOrderQueue;

    public Waiter(Map<Tablet, Boolean> allTablets) {
        this.allTablets = allTablets;
    }

    public void setCompletedOrderQueue(LinkedBlockingQueue<Order> completedOrderQueue) {
        this.completedOrderQueue = completedOrderQueue;
    }

    @Override
    public void run() {
        while (true) {
            //Вынимаем готовый заказ из очереди
            Order order = null;
            try {
                order = completedOrderQueue.take();
            } catch (InterruptedException e) {}

            //Приносим заказ на столик
            ConsoleHelper.writeMessage(order + " was cooked by " + order.getCook());
            ConsoleHelper.writeMessage("");

            //Запускаем поток 'Клиенты едят, потом свобождают столик'
            final Order order1 = order;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int eatingTime = order1.getTotalCookingTime()*20;
                        //Клиенты едят
                        Thread.sleep(eatingTime);

                        //Клиенты закончили, 'освобождаем' столик-планшет
                        allTablets.put(order1.getTablet(), true);

                    } catch (InterruptedException e) {}
                }
            }).start();
        }




    }
}
