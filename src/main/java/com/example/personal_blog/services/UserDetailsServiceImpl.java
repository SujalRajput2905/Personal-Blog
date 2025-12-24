package com.example.personal_blog.services;

import com.example.personal_blog.model.User;
import com.example.personal_blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) {
            User foundUser = user.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(foundUser.getUsername())
                    .password(foundUser.getPassword())
                    .roles(foundUser.getRole())
                    .build();
        }
        throw new UsernameNotFoundException("No user found with given username" + username);
    }
}
