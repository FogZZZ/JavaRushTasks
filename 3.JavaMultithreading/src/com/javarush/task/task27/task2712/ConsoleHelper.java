package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Dish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() throws IOException {
        return reader.readLine();
    }

    public static List<Dish> getAllDishesForOrder() throws IOException {
        List<Dish> listOfDishes = new ArrayList<>();
        System.out.println(Dish.allDishesToString());
        System.out.println("Введите название блюда. Для завершения заказа наберите 'exit'");

        while (true) {
            String desiredDish = reader.readLine();
            if (desiredDish.equals("exit")) {
                break;
            } else if (!Arrays.asList(Dish.allDishesToString().split(", ")).contains(desiredDish)) {
                System.out.println("Такого блюда нет в меню.");
            } else {
                listOfDishes.add(Dish.valueOf(desiredDish));
            }
        }
        return listOfDishes;
    }
}
