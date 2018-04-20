package com.javarush.task.task27.task2712.statistic;

import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventType;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;

import java.util.*;

public class StatisticManager {
    private static StatisticManager instance = new StatisticManager();

    private StatisticManager() {}

    public static StatisticManager getInstance() {
        return instance;
    }

/*........................................................................................*/

    private StatisticStorage statisticStorage = new StatisticStorage();

    public void register(EventDataRow data) {
        statisticStorage.put(data);
    }

    private class StatisticStorage {
        private Map<EventType, List<EventDataRow>> storage = new HashMap<>();

        private StatisticStorage() {
            for (EventType eventType : EventType.values()) {
                storage.put(eventType, new ArrayList<EventDataRow>());
            }

            /*EventDataRow event = new VideoSelectedEventDataRow(null, 10, 300);
            ((VideoSelectedEventDataRow) event).setDate(new Date(117, 7, 23));
            storage.get(EventType.SELECTED_VIDEOS).add(event);

            event = new CookedOrderEventDataRow(null, "Andrey", 61, null);
            ((CookedOrderEventDataRow) event).setDate(new Date(117, 7, 23));
            storage.get(EventType.COOKED_ORDER).add(event);*/

            /*List<EventDataRow> eventDataRowList = new ArrayList<>();
            eventDataRowList.add(new VideoSelectedEventDataRow(null, 10, 300));
            eventDataRowList.add(new VideoSelectedEventDataRow(null, 20, 350));
            eventDataRowList.add(new VideoSelectedEventDataRow(null, 30, 400));
            eventDataRowList.add(new VideoSelectedEventDataRow(null, 40, 500));
            eventDataRowList.add(new VideoSelectedEventDataRow(null, 10, 650));

            storage.put(EventType.COOKED_ORDER, new ArrayList<EventDataRow>());
            storage.put(EventType.NO_AVAILABLE_VIDEO, new ArrayList<EventDataRow>());
            storage.put(EventType.SELECTED_VIDEOS, eventDataRowList);*/
        }

        private void put(EventDataRow data) {
            List<EventDataRow> dataList = storage.get(data.getType());
            dataList.add(data);
            storage.put(data.getType(), dataList);
        }

        private List<EventDataRow> get(EventType eventType) {
            return storage.get(eventType);
        }
    }

    public Map<Date, Long> getDailyAmounts() {
        Map<Date, Long> dailyAmounts = new HashMap<>();
        List<EventDataRow> videoSelectedEvents = statisticStorage.get(EventType.SELECTED_VIDEOS);

        for (EventDataRow eventDataRow : videoSelectedEvents) {
            Date date = eventDataRow.getDate();
            date = new Date(date.getYear(), date.getMonth(), date.getDate());
            Long amount = ((VideoSelectedEventDataRow)eventDataRow).getAmount();

            if (!dailyAmounts.containsKey(date)) {
                dailyAmounts.put(date, amount);
            } else {
                Long newAmount = dailyAmounts.get(date) + amount;
                dailyAmounts.put(date, newAmount);
            }
        }
        return dailyAmounts;
    }

    public Map<Date, Map<String, Integer>> getCookWorkloading() {
        Map<Date, Map<String, Integer>> cookWorkloading = new HashMap<>();
        List<EventDataRow> cookedOrderEvents = statisticStorage.get(EventType.COOKED_ORDER);

        for (EventDataRow eventDataRow : cookedOrderEvents) {
            Date date = eventDataRow.getDate();
            date = new Date(date.getYear(), date.getMonth(), date.getDate());
            Integer cookingTimeSeconds = eventDataRow.getTime();
            String cookName = ((CookedOrderEventDataRow)eventDataRow).getCookName();

            if (!cookWorkloading.containsKey(date)) {
                HashMap<String, Integer> currentCookWorkloading = new HashMap<String, Integer>();
                currentCookWorkloading.put(cookName, cookingTimeSeconds);
                cookWorkloading.put(date, currentCookWorkloading);
            } else {
                Map<String, Integer> currentCookWorkloading = cookWorkloading.get(date);
                if (!currentCookWorkloading.containsKey(cookName)) {
                    currentCookWorkloading.put(cookName, cookingTimeSeconds);
                } else {
                    Integer newCookingTimeSeconds = currentCookWorkloading.get(cookName) + cookingTimeSeconds;
                    currentCookWorkloading.put(cookName, newCookingTimeSeconds);
                }
                cookWorkloading.put(date, currentCookWorkloading);
            }
        }
        return cookWorkloading;
    }

}
