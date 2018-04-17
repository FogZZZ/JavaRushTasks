package com.javarush.task.task27.task2712.statistic;

import com.javarush.task.task27.task2712.statistic.event.EventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        public StatisticStorage() {
            for (EventType eventType : EventType.values()) {
                storage.put(eventType, new ArrayList<EventDataRow>());
            }
        }

        private void put(EventDataRow data) {
            List<EventDataRow> dataList = storage.get(data.getType());
            dataList.add(data);
            storage.put(data.getType(), dataList);
        }
    }
}
