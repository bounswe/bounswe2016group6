package org.learner.web.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.learner.validation.PasswordMatches;
import org.learner.validation.ValidEmail;
import org.learner.validation.ValidPassword;

@PasswordMatches
public class TopicDto {
    @NotNull
    @Size(min = 1)
    private String header;

    @NotNull
    @Size(min = 1)
    private String content;
    
    
    //TODO reveal > current date constraint
    private Date revealDate;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;



    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserDto [header=").append(header).append("]");
        return builder.toString();
    }

}
