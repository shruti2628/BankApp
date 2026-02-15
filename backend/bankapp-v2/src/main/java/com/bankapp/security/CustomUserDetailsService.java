package com.bankapp.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bankapp.entities.User;
import com.bankapp.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String dbPass = user.getPassword();
        
        System.out.println("🔍 --- STRING COMPARISON DEBUG ---");
        System.out.println("🔍 DB Password: [" + dbPass + "]");
        System.out.println("🔍 DB Pass Length: " + (dbPass != null ? dbPass.length() : "NULL"));
        System.out.println("🔍 DB Role: [" + user.getRole().name() + "]");
        System.out.println("🔍 -------------------------------");
        
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
        		.password(dbPass)
                .authorities("ROLE_" + user.getRole().name())
                .build();
    }
////            System.out.println("DEBUG: Stored Hash from DB: " + user.getPassword());
////            boolean isMatch = passwordEncoder.matches("password", user.getPassword());
////            System.out.println("DEBUG: Does it match the word 'password'?: " + isMatch);

}
