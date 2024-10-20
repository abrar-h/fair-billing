package com.bt.fairbilling;

import java.time.LocalTime;
import java.util.List;

public class UserSession {
    private int sessionCount;
    private long totalDuration;
    private final List<LocalTime> startTimes;

    public UserSession(int sessionCount, long totalDuration, List<LocalTime> startTimes) {
        this.sessionCount = sessionCount;
        this.totalDuration = totalDuration;
        this.startTimes = startTimes;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public List<LocalTime> getStartTimes() {
        return startTimes;
    }

    public void incrementSessionCount() {
        this.sessionCount++;
    }

    public void addToTotalDuration(long duration) {
        this.totalDuration += duration;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "sessionCount=" + sessionCount +
                ", totalDuration=" + totalDuration +
                ", startTimes=" + startTimes +
                '}';
    }
}