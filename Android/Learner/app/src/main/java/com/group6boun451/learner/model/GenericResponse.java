package com.group6boun451.learner.model;

/**
 * Created by Ahmet Zorer on 11/12/2016.
 */
public class GenericResponse {
    private String message;
    private String error;

    public GenericResponse(){}
    public GenericResponse(final String message) {
        super();
        this.message = message;
    }

    public GenericResponse(final String message, final String error) {
        super();
        this.message = message;
        this.error = error;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

}