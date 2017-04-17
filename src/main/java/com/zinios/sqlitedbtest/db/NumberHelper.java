package com.zinios.sqlitedbtest.db;

public class NumberHelper {

    public static boolean isNumber(String input) {
        String regEx = "^[0-9]+([\\.]?[0-9]+)*$";
        return input.matches(regEx);
    }

    public static Float getNumber(String input) {
        String regExFloat = "^[0-9]+[\\.]{1}[0-9]+$";
        String regExInt = "^[0-9]+$";
        if (input.matches(regExFloat)) {
            return Float.parseFloat(input);
        } else if (input.matches(regExInt)) {
            return (float) Integer.parseInt(input);
        }
        return null;
    }

}
