package com.javarush.task.task27.task2712.ad;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementStorage {
    private static AdvertisementStorage instance = new AdvertisementStorage();

    private AdvertisementStorage() {
        Object someContent = new Object();
        add(new Advertisement(someContent, "First Video", 5000, 100, 3 * 60)); // 3 min
        add(new Advertisement(someContent, "Second Video", 100, 10, 15 * 60)); //15 min
        add(new Advertisement(someContent, "Third Video", 400, 2, 10 * 60)); //10 min
        add(new Advertisement(someContent, "четвертое видео", 400, 2, 11 * 60)); //11 min
        add(new Advertisement(someContent, "Firth Video", 400, 2, 12 * 60)); //12 min

        add(new Advertisement(someContent, "Супер видео", 10000, 0, 1 * 60)); //1 min
        add(new Advertisement(someContent, "еще видео", 200, 0, 15 * 60)); //15 min
        add(new Advertisement(someContent, "another video", 100, 0, 5 * 60)); //5 min
    }

    public static AdvertisementStorage getInstance() {
        return instance;
    }

/*...........................................................................................*/

    private final List<Advertisement> videos = new ArrayList<>();

    public List<Advertisement> list() {
        return videos;
    }

    public void add(Advertisement advertisement) {
        videos.add(advertisement);
    }

}
