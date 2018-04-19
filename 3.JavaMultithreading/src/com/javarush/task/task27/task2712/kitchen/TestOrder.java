package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.Tablet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestOrder extends Order {
    public TestOrder(Tablet tablet) throws IOException {
        super(tablet);
    }

    @Override
    protected void initDishes() throws IOException {
        List<Dish> randDishes;
        Dish[] allDishes = Dish.values();
        int randNum = (int)(1 + Math.random()*allDishes.length);

        //Вытаскиваем несколько рандомных блюд
        List<Dish> allDishesCopy = Arrays.asList(allDishes);
        Collections.shuffle(allDishesCopy);
        randDishes = allDishesCopy.subList(0, randNum);

        dishes = randDishes;
    }
}
