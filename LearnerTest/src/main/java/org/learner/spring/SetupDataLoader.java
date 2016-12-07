package org.learner.spring;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.learner.persistence.dao.PrivilegeRepository;
import org.learner.persistence.dao.RoleRepository;
import org.learner.persistence.dao.UserRepository;
import org.learner.persistence.model.Privilege;
import org.learner.persistence.model.Role;
import org.learner.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = true;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial privileges
        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        
        final Privilege teacherPrivilege = createPrivilegeIfNotFound("TEACHER_PRIVILEGE");
        final Privilege studentPrivilege = createPrivilegeIfNotFound("STUDENT_PRIVILEGE");

        // == create initial roles
        final List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege,studentPrivilege));
        
        final List<Privilege> teacherPrivs = Arrays.asList(readPrivilege, writePrivilege,teacherPrivilege);
        createRoleIfNotFound("ROLE_TEACHER", teacherPrivs);
        
        
        final Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        final User user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@test.com");
        user.setRoles(Arrays.asList(adminRole));
        user.setEnabled(true);
        userRepository.save(user);
        
        
        //TEST USER
        final Role userRole = roleRepository.findByName("ROLE_USER");
        final User traduser = new User();
        traduser.setFirstName("Ordinary");
        traduser.setLastName("Student");
        traduser.setPassword(passwordEncoder.encode("test"));
        traduser.setEmail("stu@test.com");
        traduser.setRoles(Arrays.asList(userRole));
        traduser.setEnabled(true);
        userRepository.save(traduser);
        
        //TEST TEACHER
        final Role teacherRole = roleRepository.findByName("ROLE_TEACHER");
        User tuser = new User();
        tuser.setFirstName("Meister");
        tuser.setLastName("Senpai");
        tuser.setPassword(passwordEncoder.encode("test"));
        tuser.setEmail("tea@test.com");
        tuser.setRoles(Arrays.asList(teacherRole));
        tuser.setEnabled(true);
        userRepository.save(tuser);
        
        tuser = new User();
        tuser.setFirstName("Ahmet");
        tuser.setLastName("Zorer");
        tuser.setPassword(passwordEncoder.encode("test"));
        tuser.setEmail("ahmet@learner.com");
        tuser.setRoles(Arrays.asList(teacherRole));
        tuser.setEnabled(true);
        userRepository.save(tuser);
        
        tuser = new User();
        tuser.setFirstName("Erhan");
        tuser.setLastName("Cagirici");
        tuser.setPassword(passwordEncoder.encode("test"));
        tuser.setEmail("erhan@learner.com");
        tuser.setRoles(Arrays.asList(teacherRole));
        tuser.setEnabled(true);
        userRepository.save(tuser);
        
        tuser = new User();
        tuser.setFirstName("Muaz");
        tuser.setLastName("Ekici");
        tuser.setPassword(passwordEncoder.encode("test"));
        tuser.setEmail("muaz@learner.com");
        tuser.setRoles(Arrays.asList(teacherRole));
        tuser.setEnabled(true);
        userRepository.save(tuser);
        
        tuser = new User();
        tuser.setFirstName("Esra");
        tuser.setLastName("Alinca");
        tuser.setPassword(passwordEncoder.encode("test"));
        tuser.setEmail("esra@learner.com");
        tuser.setRoles(Arrays.asList(teacherRole));
        tuser.setEnabled(true);
        userRepository.save(tuser);
        
        
        tuser = new User();
        tuser.setFirstName("Arda");
        tuser.setLastName("Akdemir");
        tuser.setPassword(passwordEncoder.encode("test"));
        tuser.setEmail("arda@learner.com");
        tuser.setRoles(Arrays.asList(teacherRole));
        tuser.setEnabled(true);
        userRepository.save(tuser);
        
        tuser = new User();
        tuser.setFirstName("Gufte");
        tuser.setLastName("Surmeli");
        tuser.setPassword(passwordEncoder.encode("test"));
        tuser.setEmail("gufte@learner.com");
        tuser.setRoles(Arrays.asList(teacherRole));
        tuser.setEnabled(true);
        userRepository.save(tuser);
        
        tuser = new User();
        tuser.setFirstName("Melih");
        tuser.setLastName("Demiroren");
        tuser.setPassword(passwordEncoder.encode("test"));
        tuser.setEmail("melih@learner.com");
        tuser.setRoles(Arrays.asList(teacherRole));
        tuser.setEnabled(true);
        userRepository.save(tuser);
        
        tuser = new User();
        tuser.setFirstName("Ali Can");
        tuser.setLastName("Erkilic");
        tuser.setPassword(passwordEncoder.encode("test"));
        tuser.setEmail("ali@learner.com");
        tuser.setRoles(Arrays.asList(teacherRole));
        tuser.setEnabled(true);
        userRepository.save(tuser);
        
        tuser = new User();
        tuser.setFirstName("Deniz");
        tuser.setLastName("Demirel");
        tuser.setPassword(passwordEncoder.encode("test"));
        tuser.setEmail("user@learner.com");
        tuser.setRoles(Arrays.asList(userRole));
        tuser.setEnabled(true);
        userRepository.save(tuser);
        
        alreadySetup = true;
    }

    @Transactional
    private final Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private final Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

}