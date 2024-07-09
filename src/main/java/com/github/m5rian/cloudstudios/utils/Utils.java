package com.github.m5rian.cloudstudios.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Utils {
    public static final ScheduledExecutorService TIMER = Executors.newScheduledThreadPool(5);

    public static final String NON_DIGITS = "[^\\d.]";
    public static final String USER_MENTION = "<@!?(\\d+)>";

    /**
     * Get a clickable message, which redirects you to a link.
     *
     * @param message The shown message.
     * @param link    The link if you click on the message.
     * @return Returns a hyperlink as a String.
     */
    public static String hyperlink(String message, String link) {
        return "[" + message + "](" + link + ")";
    }
}
