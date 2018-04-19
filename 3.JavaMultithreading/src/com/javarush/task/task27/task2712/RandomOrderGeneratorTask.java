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
        int randIndex = (int)(Math.random()*allTablets.size());
        Tablet randTablet = allTablets.get(randIndex);

        //генерируем случайный заказ у randTablet каждые 100 мс
        while (true) {
            randTablet.createTestOrder();
            ConsoleHelper.writeMessage("");
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
