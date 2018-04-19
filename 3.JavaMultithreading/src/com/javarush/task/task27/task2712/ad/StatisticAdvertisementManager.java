package com.javarush.task.task27.task2712.ad;

import java.util.ArrayList;
import java.util.List;

public class StatisticAdvertisementManager {
    private static StatisticAdvertisementManager instance;

    private StatisticAdvertisementManager() {}

    public static StatisticAdvertisementManager getInstance() {
        if (instance == null) {
            instance = new StatisticAdvertisementManager();
        }
        return instance;
    }

/*..............................................................................*/

    AdvertisementStorage advertisementStorage = AdvertisementStorage.getInstance();

    public List<Advertisement> getActiveVideos() {
        List<Advertisement> allVideos = advertisementStorage.list();
        List<Advertisement> activeVideos = new ArrayList<>();

        for (Advertisement video : allVideos) {
            if (video.getHits() > 0) {
                activeVideos.add(video);
            }
        }
        return activeVideos;
    }

    public List<Advertisement> getArchivedVideos() {
        List<Advertisement> allVideos = advertisementStorage.list();
        List<Advertisement> archivedVideos = new ArrayList<>();

        for (Advertisement video : allVideos) {
            if (video.getHits() == 0) {
                archivedVideos.add(video);
            }
        }
        return archivedVideos;
    }
}
