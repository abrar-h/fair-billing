package com.bt.fairbilling;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FairBillingTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testFileCouldNotBeProcessed() {
        String[] args = {"thisIsARandomFilePath.txt"};
        System.setErr(new PrintStream(outContent));

        FairBilling.main(args);
        String expected = "Could not process file for calculation";
        String actualOutput = outContent.toString();
        assertEquals(expected, actualOutput);
    }

    @Test
    void testNoFilePathProvided() {
        String[] args = {};
        System.setErr(new PrintStream(outContent));

        FairBilling.main(args);
        String expected = "Please provide a file path";
        String actualOutput = outContent.toString();
        assertEquals(expected, actualOutput);
    }

    @Test
    void testWithValidFilePath(){
        String testLogPath = "src/test/resources/validInput.txt";
        String[] args = {testLogPath};

        assertDoesNotThrow(() -> FairBilling.main(args));
    }

    @Test
    void testGenerateWithNoSessions() {
        String input = "";
        BufferedReader br = new BufferedReader(new StringReader(input));
        FairBilling.generateBillingReport(br);
        String actual = outContent.toString();
        assertEquals("", actual);
    }

    @Test
    void testGenerateWithOneUserAndOneSession() {
        String input = "10:00:00 USER1 Start\n" +
                "10:03:00 USER1 End";
        BufferedReader br = new BufferedReader(new StringReader(input));
        FairBilling.generateBillingReport(br);

        String expected = "USER1 1 180\n";
        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    void testGenerateWithTwoSessionsAndOneUser() {
        String input = "10:00:00 USER1 Start\n" +
                "10:03:00 USER1 End\n" +
                "10:04:00 USER1 Start\n" +
                "10:05:30 USER1 End";
        BufferedReader br = new BufferedReader(new StringReader(input));
        FairBilling.generateBillingReport(br);

        String expected = "USER1 2 270\n";
        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    void testGenerateWithMultipleSessionsAndUsers() {
        String input = "10:00:00 USER1 Start\n" +
                "10:00:05 USER2 Start\n" +
                "10:01:34 USER1 End\n" +
                "10:02:00 USER1 Start\n" +
                "10:03:02 USER2 End\n" +
                "10:04:00 USER1 End\n" +
                "10:05:15 USER2 End\n +" +
                "10:05:20 USER2 End";
        BufferedReader br = new BufferedReader(new StringReader(input));
        FairBilling.generateBillingReport(br);

        String expected = "USER1 2 214\n" +
                "USER2 2 492\n";
        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    void testGenerateWithUnmatchedStartsAndUnmatchedEnds() {
        String input = "14:02:03 ALICE99 Start\n" +
                "14:02:05 CHARLIE End\n" +
                "14:02:34 ALICE99 End\n" +
                "14:02:58 ALICE99 Start\n" +
                "14:03:02 CHARLIE Start\n" +
                "14:03:33 ALICE99 Start\n" +
                "14:03:35 ALICE99 End\n" +
                "14:03:37 CHARLIE End\n" +
                "14:04:05 ALICE99 End\n" +
                "14:04:23 ALICE99 End\n" +
                "14:04:41 CHARLIE Start";
        BufferedReader br = new BufferedReader(new StringReader(input));
        FairBilling.generateBillingReport(br);

        String expected = "ALICE99 4 240\nCHARLIE 3 37\n";
        String actual = outContent.toString();
        assertEquals(expected, actual);
    }

    @Test
    void testGenerateWithNoSessionTime() {
        String input = " USER1 Start";
        BufferedReader br = new BufferedReader(new StringReader(input));
        FairBilling.generateBillingReport(br);

        String actual = outContent.toString();
        assertEquals("", actual);
    }

    @Test
    void testGenerateWithInvalidSessionTime() {
        String input = "10:05 USER1 Start";
        BufferedReader br = new BufferedReader(new StringReader(input));
        FairBilling.generateBillingReport(br);

        String actual = outContent.toString();
        assertEquals("", actual);
    }

    @Test
    void testGenerateWithNoUsername() {
        String input = "10:05:00 Start";
        BufferedReader br = new BufferedReader(new StringReader(input));
        FairBilling.generateBillingReport(br);

        String actual = outContent.toString();
        assertEquals("", actual);
    }

    @Test
    void testGenerateWithInvalidUsername() {
        String input = "10:05:00 USER-X-%£$ Start";
        BufferedReader br = new BufferedReader(new StringReader(input));
        FairBilling.generateBillingReport(br);

        String actual = outContent.toString();
        assertEquals("", actual);
    }

    @Test
    void testGenerateWithBlankAction() {
        String input = "10:05:00 USER1 ";
        BufferedReader br = new BufferedReader(new StringReader(input));
        FairBilling.generateBillingReport(br);

        String actual = outContent.toString();
        assertEquals("", actual);
    }

    @Test
    void testGenerateWithInvalidAction() {
        String input = "10:05:00 USER1 Qwerty";
        BufferedReader br = new BufferedReader(new StringReader(input));
        FairBilling.generateBillingReport(br);

        String actual = outContent.toString();
        assertEquals("", actual);
    }

    @AfterAll
    static void tearDown(){
        System.setOut(System.out);
    }
}