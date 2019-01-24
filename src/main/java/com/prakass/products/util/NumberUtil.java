package com.prakass.products.util;

public class NumberUtil {

    public static double parseNumberSafely(String numberStr) {
        try {
            return Double.parseDouble(numberStr);
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public static boolean isNumberAnInt(double value) {
        return Math.floor(value) == value;
    }
}
