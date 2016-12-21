package com.group6boun451.learner.activity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class TaskTest {
    String [] testStrings = {"test1","test2","test3"};
    @Test
    public void doesGetResultReturnsCorrectly() throws Exception {
        String result = "";
        try {
            result = new ObjectMapper().writeValueAsString(testStrings);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertEquals(Task.getResult(result,String[].class).length, 3);
    }
}