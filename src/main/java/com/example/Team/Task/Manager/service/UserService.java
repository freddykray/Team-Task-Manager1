package com.example.Team.Task.Manager.service;

import com.example.Team.Task.Manager.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.Team.Task.Manager.repository.UserRepository;
import com.example.Team.Task.Manager.security.UserDetailsImpl;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findUserByUsername(username)
               .orElseThrow(()  -> new UsernameNotFoundException(
               String.format("User '%s' not found", username)
        ));
        return UserDetailsImpl.build(user);
    }




}
