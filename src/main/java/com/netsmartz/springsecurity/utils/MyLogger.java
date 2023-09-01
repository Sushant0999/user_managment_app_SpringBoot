package com.netsmartz.springsecurity.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyLogger {
    public static Logger logger =  LoggerFactory.getLogger(Logger.class);

    public static void info(String message) {
        logger.info(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void debug(String message) {
        logger.debug(message);
    }
}
