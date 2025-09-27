package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Sign-Up API
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody Map<String, Object> signUpRequest) {
        // Extract and validate inputs
        String email = (String) signUpRequest.get("email");
        String password = (String) signUpRequest.get("password");
        String confirmPassword = (String) signUpRequest.get("confirmPassword");
        String dobString = (String) signUpRequest.get("dob");
        String empName = (String) signUpRequest.get("empName");
        String empId = (String) signUpRequest.get("empId");
        String location = (String) signUpRequest.get("location");

        if (email == null || password == null || confirmPassword == null || dobString == null ||
            empName == null || empId == null || location == null ||
            email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("All fields are required!");
        }

        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match!");
        }
        
        // Convert dobString to Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Expected format
        Date dob1;
        try {
            dob1 = dateFormat.parse(dobString); // Parse the String to Date
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format! Use 'yyyy-MM-dd'.");
        }


        // Save user
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setDob(dob1);
        user.setEmpName(empName);
        user.setEmpId(empId);
        user.setLocation(location);

        String responseMessage = userService.saveUser(user);
        if ("User saved successfully!".equals(responseMessage)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseMessage);
        }
    }

    // Sign-In API
    @PostMapping("/signin")
    public ResponseEntity<Object> signIn(@RequestBody Map<String, String> loginRequest) {
        // Extract and validate inputs
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email and password are required!");
        }

        // Authenticate user
        User user = userService.authenticateUser(email, password);
        if (user != null) {
            // Generate JWT Token
            String jwtToken = JwtUtil.generateToken(email);

            // Response with token
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "message", "Login successful!",
                "token", "Bearer " + jwtToken
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password!");
        }

    }
    
    
    
    @GetMapping("/details/{email}")
    public ResponseEntity<Object> getUserDetailsByEmail(@PathVariable String email) {
        // Validate email
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required!");
        }

        // Fetch user details
        User user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for email: " + email);
        }
    }
    
    
    
}