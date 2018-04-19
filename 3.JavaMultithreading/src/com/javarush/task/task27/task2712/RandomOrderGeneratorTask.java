package com.javarush.task.task27.task2712;

import java.util.ArrayList;
import java.util.List;

public class RandomOrderGeneratorTask implements Runnable {
    private List<Tablet> allTablets = new ArrayList<>();
    private int interval;

    public RandomOrderGeneratorTask(List<Tablet> allTablets, int interval) {
        this.allTablets = allTablets;
        this.interval = interval;
    }

    @Override
    public void run() {
        //генерируем случайный заказ у случайного планшета каждые 100 мс
        while (true) {
            int randIndex = (int)(Math.random()*allTablets.size());
            Tablet randTablet = allTablets.get(randIndex);
            randTablet.createTestOrder();
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
