package org.learner.security;

public interface ISecurityUserService {

    String validatePasswordResetToken(long id, String token);

}
