logging utilities


package com.webcrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingUtility {
    private static final Logger logger = LogManager.getLogger(LoggingUtility.class);

    public static void info(Class<?> clazz, String message) {
        LogManager.getLogger(clazz).info(message);
    }

    public static void debug(Class<?> clazz, String message) {
        LogManager.getLogger(clazz).debug(message);
    }

    public static void error(Class<?> clazz, String message, Throwable throwable) {
        LogManager.getLogger(clazz).error(message, throwable);
    }

    public static void error(Class<?> clazz, String message) {
        LogManager.getLogger(clazz).error(message);
    }
}

