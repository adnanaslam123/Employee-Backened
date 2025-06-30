package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.Employee;
import com.example.employeemanagement.exception.CustomizeException;
import com.example.employeemanagement.repo.EmployeeRepo;
import com.example.employeemanagement.service.EmployeeService;
import com.example.employeemanagement.service.OtpService;
import com.example.employeemanagement.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private BCryptPasswordEncoder encoder;
    
    @Autowired
    private OtpService otpService;
    
    @Autowired
    private EmployeeRepo repo;

    @Autowired
    private PasswordEncoder passwordEncoder; 
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Employee emp = employeeService.findByEmail(email);

        if (emp == null || !encoder.matches(password, emp.getPassword())) {
            throw new CustomizeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(emp.getEmail(), emp.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("email", emp.getEmail());
        response.put("role", emp.getRole());
        response.put("name", emp.getName());

        return response;
    }
    
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        if (repo.findByEmail(email) == null) {
            return ResponseEntity.badRequest().body("Email not registered");
        }
        otpService.sendOtp(email);
        return ResponseEntity.ok("OTP sent");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String otp = payload.get("otp");
        if (otpService.validateOtp(email, otp)) {
            otpService.removeOtp(email); // Clean up
            return ResponseEntity.ok("OTP verified");
        }
        return ResponseEntity.badRequest().body("Invalid or expired OTP");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String newPassword = payload.get("password");

        Employee emp = repo.findByEmail(email);
        if (emp == null) return ResponseEntity.badRequest().body("Email not found");

        emp.setPassword(passwordEncoder.encode(newPassword));
        repo.save(emp);
        return ResponseEntity.ok("Password updated successfully");
    }

}

