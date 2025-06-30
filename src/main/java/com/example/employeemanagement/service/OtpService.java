package com.example.employeemanagement.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new HashMap<>();
    private final Map<String, LocalDateTime> expiryMap = new HashMap<>();

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStorage.put(email, otp);
        expiryMap.put(email, LocalDateTime.now().plusMinutes(5));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("mohammadaslamadnan@gmail.com");
        message.setSubject("OTP for Password Reset");
        message.setText("Your OTP is: " + otp + ". It expires in 5 minutes.");
        mailSender.send(message);
    }

    public boolean validateOtp(String email, String otp) {
        return otpStorage.containsKey(email) &&
               otpStorage.get(email).equals(otp) &&
               expiryMap.get(email).isAfter(LocalDateTime.now());
    }

    public void removeOtp(String email) {
        otpStorage.remove(email);
        expiryMap.remove(email);
    }
}

