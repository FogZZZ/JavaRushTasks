package com.javarush.task.task27.task2712;

import java.util.ArrayList;
import java.util.List;

public class RandomOrderGeneratorTask implements Runnable {
    private List<Tablet> allTablets = new ArrayList<>();
    private int orderCreatingInterval;

    public RandomOrderGeneratorTask(List<Tablet> allTablets, int orderCreatingInterval) {
        this.allTablets = allTablets;
        this.orderCreatingInterval = orderCreatingInterval;
    }

    @Override
    public void run() {
        int randIndex = (int)(Math.random()*allTablets.size());
        Tablet randTablet = allTablets.get(randIndex);

        //генерируем случайный заказ у randTablet каждые 100 мс
        while (true) {
            randTablet.createTestOrder();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
    }
}
