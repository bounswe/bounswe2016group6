package com.group6boun451.learner.model;

/**
 * Created by Ahmet Zorer on 11/11/2016.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Owner {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;
    private List<Object> comments = new ArrayList<Object>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName
     * The firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return
     * The lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     * The lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     *
     * @param enabled
     * The enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     *
     * @return
     * The comments
     */
    public List<Object> getComments() {
        return comments;
    }

    /**
     *
     * @param comments
     * The comments
     */
    public void setComments(List<Object> comments) {
        this.comments = comments;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
