package com.example.demo.model;

import com.example.demo.repository.AppUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class SSUserDetailsService implements UserDetailsService {
    private AppUserRepository userRepository;

    public SSUserDetailsService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        AppUser theUser = userRepository.findByUsername(s);
        if (theUser == null)
            throw new UsernameNotFoundException("Unable to find that user");
        return new User(theUser.getUsername(), theUser.getPassword(), myAuthorities(theUser));
    }

    private Collection<? extends GrantedAuthority> myAuthorities(AppUser user) {
        Set<GrantedAuthority> userAuthorities = new HashSet<>();

        for (AppRole role : user.getRoles()) {
            userAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        }

        return userAuthorities;
    }
}