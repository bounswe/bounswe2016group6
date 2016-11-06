package org.learner.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.learner.persistence.dao.UserRepository;
import org.learner.persistence.model.User;
import org.learner.spring.Application;
import org.learner.spring.TestDbConfig;
import org.learner.spring.TestIntegrationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.authentication.FormAuthConfig;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, TestDbConfig.class, TestIntegrationConfig.class })
@WebAppConfiguration
@IntegrationTest(value = "server.port:0")
public class ChangePasswordIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${local.server.port}")
    int port;

    private FormAuthConfig formConfig;
    private String URL;

    //

    @Before
    public void init() {
        User user = userRepository.findByEmail("test@test.com");
        if (user == null) {
            user = new User();
            user.setFirstName("Test");
            user.setLastName("Test");
            user.setPassword(passwordEncoder.encode("test"));
            user.setEmail("test@test.com");
            user.setEnabled(true);
            userRepository.save(user);
        } else {
            user.setPassword(passwordEncoder.encode("test"));
            userRepository.save(user);
        }

        RestAssured.port = port;

        String URL_PREFIX = "http://localhost:" + String.valueOf(port);
        URL = URL_PREFIX + "/user/updatePassword";
        formConfig = new FormAuthConfig(URL_PREFIX + "/login", "username", "password");
    }

    @Test
    public void givenNotAuthenticatedUser_whenLoggingIn_thenCorrect() {
        final RequestSpecification request = RestAssured.given().auth().form("test@test.com", "test", formConfig);

        request.when().get("/homepage.html").then().assertThat().statusCode(200).and().body(containsString("home"));
    }

    @Test
    public void givenNotAuthenticatedUser_whenBadPasswordLoggingIn_thenCorrect() {
        final RequestSpecification request = RestAssured.given().auth().form("XXXXXXXX@XXXXXXXXX.com", "XXXXXXXX", formConfig).redirects().follow(false);

        request.when().get("/homepage.html").then().statusCode(IsNot.not(200)).body(isEmptyOrNullString());
    }

    @Test
    public void givenLoggedInUser_whenChangingPassword_thenCorrect() {
        final RequestSpecification request = RestAssured.given().auth().form("test@test.com", "test", formConfig);

        final Map<String, String> params = new HashMap<String, String>();
        params.put("oldpassword", "test");
        params.put("password", "newtest");

        final Response response = request.with().params(params).post(URL);

        assertEquals(200, response.statusCode());
        assertTrue(response.body().asString().contains("Password updated successfully"));
    }

    @Test
    public void givenWrongOldPassword_whenChangingPassword_thenBadRequest() {
        final RequestSpecification request = RestAssured.given().auth().form("test@test.com", "test", formConfig);

        final Map<String, String> params = new HashMap<String, String>();
        params.put("oldpassword", "abc");
        params.put("password", "newtest");

        final Response response = request.with().params(params).post(URL);

        assertEquals(400, response.statusCode());
        assertTrue(response.body().asString().contains("Invalid Old Password"));
    }

    @Test
    public void givenNotAuthenticatedUser_whenChangingPassword_thenRedirect() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("oldpassword", "abc");
        params.put("password", "xyz");

        final Response response = RestAssured.with().params(params).post(URL);

        assertEquals(302, response.statusCode());
        assertFalse(response.body().asString().contains("Password updated successfully"));
    }

}
