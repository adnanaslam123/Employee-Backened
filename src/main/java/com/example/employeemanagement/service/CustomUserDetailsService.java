package com.example.employeemanagement.service;

import com.example.employeemanagement.dao.EmployeeDao;
import com.example.employeemanagement.dto.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private EmployeeDao dao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = dao.findByEmail(email);
        if (employee == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(employee.getEmail(), employee.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(employee.getRole())));


    }
}

