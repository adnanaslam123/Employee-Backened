package com.example.employeemanagement.service;

import com.example.employeemanagement.dao.EmployeeDao;
import com.example.employeemanagement.dto.Employee;
import com.example.employeemanagement.exception.CustomizeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
	 private static final String ADMIN_EMAIL = "admin@email.com";
	
    @Autowired
    private EmployeeDao dao;

    @Autowired
    private BCryptPasswordEncoder encoder;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Employee register(Employee emp) {
    	
    	  if (dao.findByEmail(emp.getEmail()) != null) {
    	        // Step 2: Throw a custom error if email exists
    	        throw new CustomizeException("Email already registered: " + emp.getEmail());
    	    }

    	    // Step 3: Encrypt password before saving
    	    emp.setPassword(encoder.encode(emp.getPassword()));
    	    System.out.println("Encoded password: " + emp.getPassword());
    	    emp.setRole("ROLE_EMPLOYEE");

    	    // Step 4: Save to DB
    	    return dao.save(emp);
    }
    public Employee findByEmail(String email) {
        return dao.findByEmail(email);
    }
    
    public String deleteOwnAccount(Principal principal) {
        Employee emp = getMyProfile(principal);
        String msg=dao.deleteById(emp.getId());
        if(msg!=null) {
        	 return msg;
        }
        else
        	throw new CustomizeException("id not found");
       
    }

    
    

    
    public Employee findById(int id) {
    	Employee employee=dao.findById(id);
    	if(employee!=null) {
    		return employee;
    	}
    	else
    		throw new CustomizeException("id not found");
    }
    public String deleteById(int id) {
    	String msg=dao.deleteById(id);
    	if(msg!=null) {
    		return msg;
    	}
    	else throw new CustomizeException("id not found,So can't delete the employee");
    }

    public Employee getMyProfile(Principal principal) {
        return dao.findByEmail(principal.getName());
    }
    
    public List<Employee> getAllEmployees() {
        List<Employee> all = dao.findAll();
        List<Employee> result = new ArrayList<>();
        for (Employee emp : all) {
            if (!"admin@email.com".equalsIgnoreCase(emp.getEmail())) {
                result.add(emp);
            }
        }
        return result;
    }

    
    

//    public List<Employee> getAllEmployees() {
//        return dao.findAll().stream()
//                .filter(emp -> !emp.getEmail().equalsIgnoreCase("admin@email.com"))
//                .toList();
//    }

    public Employee updateOwnProfile(Employee updated, Principal principal) {
        Employee existing = dao.findByEmail(principal.getName());
        if (existing != null) {
            existing.setName(updated.getName());
            existing.setDepartment(updated.getDepartment());
            existing.setSalary(updated.getSalary());

            // Only update password if a new one is provided
            if (updated.getPassword() != null && !updated.getPassword().trim().isEmpty()) {
                existing.setPassword(encoder.encode(updated.getPassword()));
            }

            return dao.save(existing);
        }
        return null;
    }


    public Employee updateByAdmin(int id, Employee updated) {
        Employee existing = dao.findById(id);
        if (existing != null) {
            existing.setName(updated.getName());
            existing.setPassword(encoder.encode(updated.getPassword()));
            existing.setDepartment(updated.getDepartment());
            existing.setSalary(updated.getSalary());
            existing.setRole(updated.getRole()); 
            return dao.save(existing);
        }
        throw new CustomizeException("Employee not found with ID: " + id);
    }
    
    
    // validation with old password
    public boolean validateOldPassword(Principal principal, String inputPassword) {
        // Step 1: Get the logged-in user's email
        String email = principal.getName();

        // Step 2: Find the employee using email
        Employee employee= dao.findByEmail(email);

        // Step 3: If employee not found, return false
        if (employee==null) {
            return false;
        }

       // Employee employee = optionalEmployee.get();

        // Step 4: Compare entered old password with stored (encoded) password
        boolean isMatch = passwordEncoder.matches(inputPassword, employee.getPassword());

        // Step 5: Return true if passwords match, else false
        return isMatch;
    }
    
    
      
}