package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.ad.Advertisement;
import com.javarush.task.task27.task2712.ad.AdvertisementManager;
import com.javarush.task.task27.task2712.ad.NoVideoAvailableException;
import com.javarush.task.task27.task2712.kitchen.Dish;
import com.javarush.task.task27.task2712.kitchen.Order;
import com.javarush.task.task27.task2712.kitchen.TestOrder;
import com.javarush.task.task27.task2712.kitchen.UIOrder;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tablet {
    private final int number;
    private static Logger logger = Logger.getLogger(Tablet.class.getName());
    private LinkedBlockingQueue<Order> queue;

    private TabletView view;
    private Map<Dish, Integer> currentOrder = new HashMap<>();

    public Tablet(int number) {
        this.number = number;
        view = new TabletView(this, number);
    }

    public int getNumber() {
        return number;
    }

    public TabletView getView() {
        return view;
    }

    public Map<Dish, Integer> getCurrentOrder() {
        return currentOrder;
    }

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    public Order createOrder() {
        try {
            Order order = new Order(this);

            utilMethod(order);

            return order;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Console is unavailable.");
            return null;
        }
    }

    public void createTestOrder() {
        try {
            Order order = new TestOrder(this);

            utilMethod(order);

            ConsoleHelper.writeMessage("");

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Console is unavailable.");
        }
    }

    public void createUIOrder() {
        try {
            Order order = new UIOrder(this);

            utilMethod(order);

        } catch (IOException e) {}
    }

    private void utilMethod(Order order) {
        ConsoleHelper.writeMessage(order.toString());

        if (!order.isEmpty()) {
            AdvertisementManager manager = new AdvertisementManager(order.getTotalCookingTime()*60);
            try {
                List<Advertisement> optimalVideoSet = manager.processVideos();
                queue.put(order);
                view.playVideos(optimalVideoSet);
            } catch (NoVideoAvailableException e) {
                logger.log(Level.INFO,"No video is available for the order " + order);
            } catch (InterruptedException e) {}
        }
    }

    @Override
    public String toString() {
        return "Tablet{" +
                "number=" + number +
                '}';
    }
}
