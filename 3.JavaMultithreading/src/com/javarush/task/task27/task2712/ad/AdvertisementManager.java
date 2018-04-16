package com.javarush.task.task27.task2712.ad;

import com.javarush.task.task27.task2712.ConsoleHelper;

import java.util.*;

public class AdvertisementManager {
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();
    private int timeSeconds;

    public AdvertisementManager(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public void processVideos() throws NoVideoAvailableException {
        if (storage.list().isEmpty()) {
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
        Set<List<Advertisement>> allPossibleCombinations = new HashSet<>();
        List<Advertisement> startCombination = new ArrayList<>();
        recursive(startCombination, -1, 0, allPossibleCombinations);

        //вычленяем комбинации с максимальным доходом (их может быть несколько с одинаковым числом полученных денег)
        int maxAmount = 0;
        List<List<Advertisement>> combinationsWithMaxAmount = new ArrayList<>();
        for (List<Advertisement> combination : allPossibleCombinations) {
            int currentAmount = 0;
            for (Advertisement ad : combination) {
                currentAmount += ad.getAmountPerOneDisplaying();
            }
            if (currentAmount > maxAmount) {
                maxAmount = currentAmount;
                combinationsWithMaxAmount.clear();
                combinationsWithMaxAmount.add(combination);
            } else if (currentAmount == maxAmount) {
                combinationsWithMaxAmount.add(combination);
            }
        }

        //если такой набор только 1, то показываем его
        if (combinationsWithMaxAmount.size() == 1) {
            //показываем...
            showVideos(combinationsWithMaxAmount.get(0));
            return;
        }

        //если не 1, то вторичная сортировка по суммарному времени (максимальное)
        int maxTime = 0;
        List<List<Advertisement>> combinationsWithMaxTime = new ArrayList<>();
        for (List<Advertisement> combination : combinationsWithMaxAmount) {
            int currentTime = 0;
            for (Advertisement ad : combination) {
                currentTime += ad.getDuration();
            }
            if (currentTime > maxTime) {
                maxTime = currentTime;
                combinationsWithMaxTime.clear();
                combinationsWithMaxTime.add(combination);
            } else if (currentTime == maxTime) {
                combinationsWithMaxTime.add(combination);
            }
        }

        //если такой набор только 1, то показываем его
        if (combinationsWithMaxTime.size() == 1) {
            //показываем...
            showVideos(combinationsWithMaxTime.get(0));
            return;
        }

        //Если не 1, то выбираем минимальное по количеству роликов
        int minNumOfVideos = Integer.MAX_VALUE;
        List<Advertisement> combinationWithMinVideos = new ArrayList<>();
        for (List<Advertisement> combination : combinationsWithMaxTime) {
            if (combination.size() < minNumOfVideos) {
                minNumOfVideos = combination.size();
                combinationWithMinVideos = combination;
            }
        }

        //показываем...
        showVideos(combinationWithMinVideos);
    }

    private void recursive(List<Advertisement> combination, int currentIndex, int currentTime, Set<List<Advertisement>> allPossibleCombinations) {
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
                    long result2 = o2.getAmountPerOneDisplaying()*1000 / o2.getDuration() -
                                    o1.getAmountPerOneDisplaying()*1000 / o1.getDuration();
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
}
