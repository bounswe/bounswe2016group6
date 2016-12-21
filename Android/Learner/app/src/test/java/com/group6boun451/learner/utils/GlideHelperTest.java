package com.group6boun451.learner.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6boun451.learner.model.GenericResponse;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class GlideHelperTest {

    GenericResponse errorResponseTest = new GenericResponse(null,"error");
    GenericResponse successResponseTest = new GenericResponse("success",null);
    @Test
    public void doesNullResultReturnFalse() throws Exception {
        assertFalse(GlideHelper.showResult(null,null));
    }

    @Test
    public void doesErrorResultReturnsFalse() throws Exception {
        String result = "";
        try {
            result = new ObjectMapper().writeValueAsString(errorResponseTest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertFalse(GlideHelper.showResult(null,result));
    }

    @Test
    public void doesSuccessResultReturnsTrue() throws Exception {
        String result = "";
        try {
            result = new ObjectMapper().writeValueAsString(successResponseTest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertTrue(GlideHelper.showResult(null,result));
    }
}