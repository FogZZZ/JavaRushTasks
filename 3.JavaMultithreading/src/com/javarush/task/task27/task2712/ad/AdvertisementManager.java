package com.javarush.task.task27.task2712.ad;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;

import java.util.*;

public class AdvertisementManager {
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();
    private int timeSeconds;

    public AdvertisementManager(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public List<Advertisement> processVideos() throws NoVideoAvailableException {
        if (storage.list() == null || storage.list().isEmpty()) {
            throw new NoVideoAvailableException();
        }

        //проверяем на наличие доступных видео (hits > 0)
        boolean hasAvailableVideo = false;
        for (Advertisement ad : storage.list()) {
            if (ad.getHits() > 0) {
                hasAvailableVideo = true;
                break;
            }
        }
        if (!hasAvailableVideo) {
            throw new NoVideoAvailableException();
        }

        //с помощью рекурсивного метода получаем набор всех комбинаций рекламы, укладывающихся по времени в заказ
        List<List<Advertisement>> allPossibleCombinations = new ArrayList<>();
        List<Advertisement> startCombination = new ArrayList<>();
        recursive(startCombination, -1, 0, allPossibleCombinations);

        //Сортируем по деньгам (макс), вторично по времени(макс), третично по количеству роликов(мин)
        Comparator<List<Advertisement>> comparator = new Comparator<List<Advertisement>>() {
            @Override
            public int compare(List<Advertisement> o1, List<Advertisement> o2) {
                long result1 = getTotalAmount(o2) - getTotalAmount(o1);
                if (result1 != 0) {
                    return (int)result1;
                }
                int result2 = getTotalDuration(o2) - getTotalDuration(o1);
                if (result2 != 0) {
                    return result2;
                }
                return o1.size() - o2.size();
            }
        };

        Collections.sort(allPossibleCombinations, comparator);
        List<Advertisement> optimalVideoSet = allPossibleCombinations.get(0);

        //создаем и записывем событие "видео выбрано"
        StatisticManager.getInstance().register(new VideoSelectedEventDataRow(optimalVideoSet, getTotalAmount(optimalVideoSet), getTotalDuration(optimalVideoSet)));

        //показываем лучший по параметрам набор
        showVideos(optimalVideoSet);

        return optimalVideoSet;
    }

    private void recursive(List<Advertisement> combination, int currentIndex, int currentTime, List<List<Advertisement>> allPossibleCombinations) {
        int lastIndex = storage.list().size()-1;

        //Если мы в конце списка, то значит поиск комбинации завершен
        if (currentIndex == lastIndex) {
            if (!combination.isEmpty()) {
                allPossibleCombinations.add(new ArrayList<Advertisement>(combination));
            }
        } else {
            // проверяем, можно ли к списку добавить следующий элемент
            Advertisement nextAd = storage.list().get(currentIndex+1);
            int nextTime = nextAd.getDuration() + currentTime;
            //подходит по времени или hits > 0
            if (nextTime <= timeSeconds && nextAd.getHits() > 0) {
                //если можно, то доавляем и запускаем рекурсию дальше
                combination.add(nextAd);
                recursive(combination, currentIndex+1, nextTime, allPossibleCombinations);

                //после отработки этой ветви рекурсии, запускаем новую без добавленного элемента
                combination.remove(combination.size()-1);
                recursive(combination, currentIndex+1, currentTime, allPossibleCombinations);
            } else {
                //если нельзя, то запускаем рекурсию дальше без него
                recursive(combination, currentIndex+1, currentTime, allPossibleCombinations);
            }
        }
    }

    private void showVideos(List<Advertisement> videosToShow) {
        Comparator<Advertisement> comparator = new Comparator<Advertisement>() {
            @Override
            public int compare(Advertisement o1, Advertisement o2) {
                long result1 = (o2.getAmountPerOneDisplaying() - o1.getAmountPerOneDisplaying());
                if (result1 != 0) {
                    return (int)result1;
                } else {
                    long result2 = o1.getAmountPerOneDisplaying()*1000 / o1.getDuration() -
                                    o2.getAmountPerOneDisplaying()*1000 / o2.getDuration();
                    return (int)result2;
                }
            }
        };

        Collections.sort(videosToShow, comparator);

        for (Advertisement video : videosToShow) {
            ConsoleHelper.writeMessage(video.getName() + " is displaying... " +
                    video.getAmountPerOneDisplaying() + ", " +
                    video.getAmountPerOneDisplaying()*1000 / video.getDuration());
            video.revalidate();
        }
    }

    private long getTotalAmount(List<Advertisement> videos) {
        long totalAmount = 0;
        for (Advertisement ad : videos) {
            totalAmount += ad.getAmountPerOneDisplaying();
        }
        return totalAmount;
    }

    private int getTotalDuration(List<Advertisement> videos) {
        int totalTime = 0;
        for (Advertisement ad : videos) {
            totalTime += ad.getDuration();
        }
        return totalTime;
    }
}
