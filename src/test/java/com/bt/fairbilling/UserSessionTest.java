package com.bt.fairbilling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserSessionTest {

    private UserSession userSession;

    @BeforeEach
    void setup(){
        userSession = new UserSession(0, 0, new ArrayList<>());
    }

    @Test
    void testIncrementSessionCount() {
        assertEquals(0, userSession.getSessionCount());
        userSession.incrementSessionCount();
        assertEquals(1, userSession.getSessionCount());
    }

    @Test
    void testAddToTotalDuration() {
        assertEquals(0, userSession.getTotalDuration());
        userSession.addToTotalDuration(5);
        assertEquals(5, userSession.getTotalDuration());
    }

    @Test
    void testToString() {
        userSession.incrementSessionCount();
        userSession.addToTotalDuration(5);
        assertEquals("UserSession{sessionCount=1, totalDuration=5, startTimes=[]}", userSession.toString());
    }
}