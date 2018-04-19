package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.statistic.StatisticManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

public class DirectorTablet {
    public void printAdvertisementProfit() {
        Map<Date, Long> dailyAmounts = StatisticManager.getInstance().getDailyAmounts();
        double total = 0;

        List<Map.Entry<Date, Long>> entryList = new ArrayList<>(dailyAmounts.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<Date, Long>>() {
            @Override
            public int compare(Map.Entry<Date, Long> o1, Map.Entry<Date, Long> o2) {
                return (int)(o1.getKey().getTime() - o2.getKey().getTime());
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00", otherSymbols);
        for (Map.Entry<Date, Long> entry : entryList) {
            Date date = entry.getKey();
            double amount = (double)entry.getValue() / 100;
            total += amount;
            ConsoleHelper.writeMessage(dateFormat.format(date) + " - " + decimalFormat.format(amount));
        }
        ConsoleHelper.writeMessage("Total - " + decimalFormat.format(total));
    }

    public void printCookWorkloading() {
        Map<Date, Map<String, Integer>> cookWorkloading = StatisticManager.getInstance().getCookWorkloading();
        
        List<Map.Entry<Date, Map<String, Integer>>> entryList1 = new ArrayList<>(cookWorkloading.entrySet());
        Collections.sort(entryList1, new Comparator<Map.Entry<Date, Map<String, Integer>>>() {
            @Override
            public int compare(Map.Entry<Date, Map<String, Integer>> o1, Map.Entry<Date, Map<String, Integer>> o2) {
                return (int)(o1.getKey().getTime() - o2.getKey().getTime());
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        for (Map.Entry<Date, Map<String, Integer>> entry1 : entryList1) {
            Date date = entry1.getKey();
            ConsoleHelper.writeMessage(dateFormat.format(date));
            
            List<Map.Entry<String, Integer>> entryList2 = new ArrayList<>(entry1.getValue().entrySet());
            Collections.sort(entryList2, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o1.getKey().compareTo(o2.getKey());
                }
            });

            for (Map.Entry<String, Integer> entry2 : entryList2) {
                String cookName = entry2.getKey();
                int cookingTimeSeconds = entry2.getValue();
                int cookingTimeMinutes = cookingTimeSeconds%60 > 0 ? cookingTimeSeconds/60 + 1 : cookingTimeSeconds/60;
                ConsoleHelper.writeMessage(cookName + " - " + cookingTimeMinutes + " min");
            }
            ConsoleHelper.writeMessage("");
        }
    }

    public void printActiveVideoSet() {}
    public void printArchivedVideoSet() {}

}
