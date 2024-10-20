package com.bt.fairbilling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FairBilling {

    private static final String START_ACTION = "Start";
    private static final String END_ACTION = "End";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String BLANK_SPACE = " ";
    private static final String VALID_USERNAME = "^[a-zA-Z0-9]+$";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.print("Please provide a file path");
            return;
        }

        String logFilePath = args[args.length - 1];
        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            generateBillingReport(br);
        } catch (Exception e) {
            System.err.print("Could not process file for calculation");
        }
    }

    public static void generateBillingReport(BufferedReader br) {
        HashMap<String, UserSession> userSessions = new HashMap<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        LocalTime earliestTime = null;
        LocalTime latestTime = null;

        try {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(BLANK_SPACE);
                if (parts.length != 3) {
                    continue; // Skip invalid lines that do not have exactly 3 parts: time, username and action
                }

                LocalTime time;
                try {
                    time = LocalTime.parse(parts[0], timeFormatter);
                } catch (Exception e) {
                    continue; // Skip lines with invalid time format
                }

                String user = parts[1];
                if (!user.matches(VALID_USERNAME)) {
                    continue;
                }

                String action = parts[2];
                if(!(action.equals(START_ACTION) || action.equals(END_ACTION))){
                    continue;
                }

                if (earliestTime == null || time.isBefore(earliestTime)) {
                    earliestTime = time;
                }
                if (latestTime == null || time.isAfter(latestTime)) {
                    latestTime = time;
                }

                UserSession session = userSessions.getOrDefault(user, new UserSession(0, 0, new ArrayList<>()));
                calculateSessionTimings(action, session, time, earliestTime);

                userSessions.put(user, session);
            }

            handleUnmatchedStarts(userSessions, latestTime);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        userSessions.entrySet().forEach((entry -> System.out.printf("%s %d %d\n",
                entry.getKey(), entry.getValue().getSessionCount(), entry.getValue().getTotalDuration())));
    }

    private static void calculateSessionTimings(String action, UserSession session, LocalTime time, LocalTime earliestTime) {
        if (action.equals(START_ACTION)) {
            session.getStartTimes().add(time);
            session.incrementSessionCount();
        } else if (action.equals(END_ACTION)) {
            long duration;
            if (!session.getStartTimes().isEmpty()) {
                // End time should be assumed to be the latest time of any record in the file
                LocalTime startTime = session.getStartTimes().remove(session.getStartTimes().size() - 1);
                duration = Duration.between(startTime, time).getSeconds();
            } else {
                // No matching start, assume start time is the earliest time in log
                duration = Duration.between(earliestTime, time).getSeconds();
                session.incrementSessionCount();
            }
            session.addToTotalDuration(duration);
        }
    }

    private static void handleUnmatchedStarts(HashMap<String, UserSession> userSessions, LocalTime latestTime) {
        for (Map.Entry<String, UserSession> entry : userSessions.entrySet()) {
            UserSession session = entry.getValue();
            for (LocalTime startTime : session.getStartTimes()) {
                long duration = Duration.between(startTime, latestTime).getSeconds();
                session.addToTotalDuration(duration);
            }
            session.getStartTimes().clear();
        }
    }
}