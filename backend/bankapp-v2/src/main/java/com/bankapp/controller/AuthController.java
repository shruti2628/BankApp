package com.bankapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.dto.JwtResponse;
import com.bankapp.dto.LoginRequest;
import com.bankapp.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    	System.out.println(request);
    	
    	
    	System.out.println("➡️ Login API hit for user: " + request.getUsername());
    	try {
            System.out.println("➡️ Attempting login for: [" + request.getUsername() + "]");
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(), 
                    request.getPassword()
                )
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();


        
        System.out.println("✅ Authentication success");

        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
        
    	} catch (AuthenticationException e) {
            System.out.println("❌ Authentrication Failed: " + e.getMessage());
            System.out.println(e.getAuthenticationRequest());
            return ResponseEntity.status(401).body("Error: " + e.getMessage());
        }
    	
    	catch (Exception e) {
            System.out.println("❌ Login Failed: " + e.getMessage());
            return ResponseEntity.status(401).body("Error: " + e.getMessage());
        }
    	
    	
    }
}
