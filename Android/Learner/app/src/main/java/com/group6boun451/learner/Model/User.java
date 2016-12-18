package com.group6boun451.learner.model;

import java.util.Collection;
import java.util.List;

/**
 * Model class for users.
 */
public class User {
    private Long id;

    private String firstName;

    private String lastName;
    
    private String email;
    private String password;
    
    private boolean enabled;
    private String secret;


    private List<com.group6boun451.learner.model.Topic> topics;
    private Collection<com.group6boun451.learner.model.Role> roles;

    private List<Comment> comments;
    private List<User> followedBy;
    private String picture;

    public User() {
        super();
//        this.secret = Base32.random();
        this.enabled = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Collection<com.group6boun451.learner.model.Role> getRoles() {
        return roles;
    }

    public void setRoles(final Collection<com.group6boun451.learner.model.Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<com.group6boun451.learner.model.Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<com.group6boun451.learner.model.Topic> topics) {
        this.topics = topics;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((email == null) ? 0 : email.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User user = (User) obj;
        return email.equals(user.email);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("User [id=").append(id).append(", firstName=").append(firstName).append(", lastName=").append(lastName).append(", email=").append(email).append(", password=").append(password).append(", enabled=").append(enabled).append(", isUsing2FA=")
                .append(", secret=").append(secret).append(", roles=").append(roles).append("]");
        return builder.toString();
    }

    public List<User> getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(List<User> followedBy) {
        this.followedBy = followedBy;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}