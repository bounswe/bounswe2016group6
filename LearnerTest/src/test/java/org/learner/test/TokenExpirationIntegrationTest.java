package org.learner.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.learner.persistence.dao.UserRepository;
import org.learner.persistence.dao.VerificationTokenRepository;
import org.learner.persistence.model.User;
import org.learner.persistence.model.VerificationToken;
import org.learner.spring.TestDbConfig;
import org.learner.spring.TestTaskConfig;
import org.learner.task.TokensPurgeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestDbConfig.class, TestTaskConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
public class TokenExpirationIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private TokensPurgeTask tokensPurgeTask;

    @PersistenceContext
    private EntityManager entityManager;

    private Long token_id;
    private Long user_id;

    //

    @Before
    public void givenUserWithExpiredToken() {
        User user = new User();
        user.setEmail(UUID.randomUUID().toString() + "@example.com");
        user.setPassword(UUID.randomUUID().toString());
        user.setFirstName("First");
        user.setLastName("Last");

        entityManager.persist(user);
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationToken.setExpiryDate(Date.from(Instant.now().minus(2, ChronoUnit.DAYS)));

        entityManager.persist(verificationToken);

        /*
            flush managed entities to the database to populate identifier field
         */
        entityManager.flush();

        /*
            remove managed entities from the persistence context
            so that subsequent SQL queries hit the database
         */
        entityManager.clear();

        token_id = verificationToken.getId();
        user_id = user.getId();
    }

    @Test
    public void whenContextLoad_thenCorrect() {
        assertNotNull(user_id);
        assertNotNull(token_id);
        assertNotNull(userRepository.findOne(user_id));

        VerificationToken verificationToken = tokenRepository.findOne(token_id);
        assertNotNull(verificationToken);

        assertTrue(tokenRepository.findAllByExpiryDateLessThan(Date.from(Instant.now())).anyMatch((token) -> token.equals(verificationToken)));
    }

    @After
    public void flushAfter() {
        entityManager.flush();
    }

    @Test
    public void whenRemoveByGeneratedQuery_thenCorrect() {
        tokenRepository.deleteByExpiryDateLessThan(Date.from(Instant.now()));
        assertEquals(0, tokenRepository.count());
    }

    @Test
    public void whenRemoveByJPQLQuery_thenCorrect() {
        tokenRepository.deleteAllExpiredSince(Date.from(Instant.now()));
        assertEquals(0, tokenRepository.count());
    }

    @Test
    public void whenPurgeTokenTask_thenCorrect() {
        tokensPurgeTask.purgeExpired();
        assertNull(tokenRepository.findOne(token_id));
    }
}
