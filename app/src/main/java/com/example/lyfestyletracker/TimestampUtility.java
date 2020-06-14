package com.example.lyfestyletracker;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Locale;

public class TimestampUtility {
    // parse method to parse timestamp from SQL query into a LocalDateTime object
    public static LocalDateTime parseDatabaseTimestamp(String s) {
        return LocalDateTime.parse(s, DateTimeFormat.forPattern("dd-MMM-yy hh.mm.ss.SSSSSS aa").withLocale(Locale.ENGLISH));
    }
}
