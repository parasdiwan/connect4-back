package com.gluck.service.impl;

public class Utils {

    public Utils() {
        throw new RuntimeException("This class must not be initialized");
    }

    public static void assertNotBlank(String arg1, String message) {
        if (arg1 == null || arg1.equals("")) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertNotNull(Object p2, String string) {
        if (p2 == null) {
            throw new IllegalArgumentException(string);
        }
    }

    public static void assertTrue(boolean arg, String msg) {
        if (!arg) {
            throw new IllegalArgumentException(msg);
        }
    }
}
