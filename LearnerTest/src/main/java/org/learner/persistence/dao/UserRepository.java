package org.learner.persistence.dao;

import java.util.List;

import org.learner.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    
    @Override
    void delete(User user);

	List<User> findByFirstNameContainingOrLastNameContaining(String q,String q2);

}
