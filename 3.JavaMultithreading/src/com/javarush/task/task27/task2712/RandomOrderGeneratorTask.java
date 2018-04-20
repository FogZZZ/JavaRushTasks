package com.javarush.task.task27.task2712;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RandomOrderGeneratorTask implements Runnable {
    private Map<Tablet, Boolean> allTablets;
    private int interval;

    public RandomOrderGeneratorTask(Map<Tablet, Boolean> allTablets, int interval) {
        this.allTablets = allTablets;
        this.interval = interval;
    }

    @Override
    public void run() {
        List<Tablet> freeTablets = new ArrayList<>();
        //генерируем случайный заказ у случайного СВОБОДНОГО планшета каждые 100 мс
        while (true) {
            //Заполняем временный список свободных столов (планшетов)
            for (Map.Entry<Tablet, Boolean> pair : allTablets.entrySet()) {
                if (pair.getValue()) {
                    freeTablets.add(pair.getKey());
                }
            }

            //Если есть свободные столы
            if (!freeTablets.isEmpty()) {
                //Берем случайный свободный стол
                int randIndex = ThreadLocalRandom.current().nextInt(0, freeTablets.size());
                Tablet randTablet = freeTablets.get(randIndex);

                //Помечаем его как 'не свободный'
                allTablets.put(randTablet, false);

                //Делаем на нем тестовый заказ
                randTablet.createTestOrder();

                //очищаем временный список
                freeTablets.clear();
            }

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
