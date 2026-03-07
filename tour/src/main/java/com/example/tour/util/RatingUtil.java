package com.example.tour.util;

public class RatingUtil {

    public static double round(Double value) {
        if (value == null) return 0.0;
        return Math.round(value * 10) / 10.0;
    }
}
