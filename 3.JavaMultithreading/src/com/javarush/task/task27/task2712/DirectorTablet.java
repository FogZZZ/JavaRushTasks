package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.statistic.StatisticManager;

import java.text.SimpleDateFormat;
import java.util.*;

public class DirectorTablet {
    public void printAdvertisementProfit() {
        Map<Date, Long> dailyAmounts = StatisticManager.getInstance().getDailyAmounts();
        long total = 0;

        List<Map.Entry<Date, Long>> entryList = new ArrayList<>(dailyAmounts.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<Date, Long>>() {
            @Override
            public int compare(Map.Entry<Date, Long> o1, Map.Entry<Date, Long> o2) {
                return (int)(o1.getKey().getTime() - o2.getKey().getTime());
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        for (Map.Entry entry : entryList) {
            Date date = (Date)entry.getKey();
            long amount = (long)entry.getValue();
            total += amount;
            ConsoleHelper.writeMessage(dateFormat.format(date) + " - " + amount);
        }
        ConsoleHelper.writeMessage("Total - " + total);
    }

    public void printCookWorkloading() {}
    public void printActiveVideoSet() {}
    public void printArchivedVideoSet() {}

}
