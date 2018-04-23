package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.Tablet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestOrder extends Order {
    public TestOrder(Tablet tablet) throws IOException {
        super(tablet);
    }

    @Override
    protected void initDishes() {
        List<Dish> randDishes;
        Dish[] allDishes = Dish.values();
        int randNum = ThreadLocalRandom.current().nextInt(1, allDishes.length+1);

        //Вытаскиваем несколько рандомных блюд
        List<Dish> allDishesCopy = Arrays.asList(allDishes);
        Collections.shuffle(allDishesCopy);
        randDishes = allDishesCopy.subList(0, randNum);

        dishes = randDishes;
    }
}
