package com.example.demo.model;

import com.example.demo.repository.AppRoleRepository;
import com.example.demo.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    AppUserRepository userRepository;

    @Autowired
    AppRoleRepository roleRepository;

    @Autowired
    public UserService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Long countByEmail(String email) {
        return userRepository.countByEmail(email);
    }

    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(AppUser user) {

        Set<AppRole> roles = new HashSet<>(Collections.singletonList(roleRepository.findByRole("USER")));
        user.setRoles(roles);
        user.setEnabled(true);
        userRepository.save(user);
    }
}
