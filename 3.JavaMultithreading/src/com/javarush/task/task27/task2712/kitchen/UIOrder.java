package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.Tablet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UIOrder extends Order {
    public UIOrder(Tablet tablet) throws IOException {
        super(tablet);
    }

    @Override
    protected void initDishes() {
        Map<Dish, Integer> orderDishes = tablet.getCurrentOrder();

        List<Dish> result = new ArrayList<>();

        for (Map.Entry<Dish, Integer> pair : orderDishes.entrySet()) {
            for (int i = 0; i < pair.getValue(); i++) {
                result.add(pair.getKey());
            }
        }

        dishes = result;
    }
}
