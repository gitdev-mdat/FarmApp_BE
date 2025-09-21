package com.farmapp.helper;

import java.text.NumberFormat;
import java.util.Locale;

public class MoneyUtil {
    private static final Locale VN = new Locale("vi", "VN");

    public static String format(int amount) {
        return NumberFormat.getInstance(VN).format(amount) + " VNĐ";
    }

    public static String format(float amount) {
        return NumberFormat.getInstance(VN).format(amount) + " VNĐ";
    }

    public static String formatKg(float kg) {
        return NumberFormat.getInstance(VN).format(kg) + " kg";
    }
}