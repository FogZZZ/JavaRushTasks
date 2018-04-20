package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.Tablet;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class Waiter implements Observer {
    private Map<Tablet, Boolean> allTablets;

    public Waiter(Map<Tablet, Boolean> allTablets) {
        this.allTablets = allTablets;
    }

    @Override
    public void update(Observable o, Object arg) {
        //Приносим заказ на столик
        ConsoleHelper.writeMessage(arg + " was cooked by " + o);
        ConsoleHelper.writeMessage("");

        //Запускаем поток 'Клиенты едят, потом свобождают столик'
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Order order = (Order)arg;
                    int eatingTime = order.getTotalCookingTime()*20;
                    //Клиенты едят
                    Thread.sleep(eatingTime);

                    //Клиенты закончили, 'освобождаем' столик-планшет
                    allTablets.put(order.getTablet(), true);

                } catch (InterruptedException e) {}
            }
        }).start();
    }
}
