package org.helloworld.gymmate.domain.gym.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GymDummyImage {
    public static final List<String> IMAGE_URLS = List.of(
        "https://...jpg",
        "https://...jpg"
    );

    public static List<String> getRandomImageUrls(int count) {
        List<String> shuffled = new ArrayList<>(IMAGE_URLS);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, Math.min(count, shuffled.size()));
    }
}