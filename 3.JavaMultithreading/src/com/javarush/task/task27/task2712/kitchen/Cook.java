package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.Tablet;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;

import java.util.Observable;
import java.util.Observer;

public class Cook extends Observable {
    private String name;

    public Cook(String name) {
        this.name = name;
    }

    public void startCookingOrder(Order order) {
        ConsoleHelper.writeMessage("Start cooking - " + order + ", cooking time " + order.getTotalCookingTime() + "min");

        //готовится...
        //...
        //готово!

        setChanged();
        notifyObservers(order);
        StatisticManager.getInstance().register(new CookedOrderEventDataRow(order.getTablet().toString(), this.name, order.getTotalCookingTime()*60, order.getDishes()));
    }

    @Override
    public String toString() {
        return name;
    }
}
