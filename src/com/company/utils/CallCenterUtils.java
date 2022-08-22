package com.company.utils;

import java.util.concurrent.ThreadLocalRandom;

public class CallCenterUtils {

    public static long getRandomTime() {
        return ThreadLocalRandom.current().nextLong(1500, 3000);
    }
}
