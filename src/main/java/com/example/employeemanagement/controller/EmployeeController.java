package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.Employee;
import com.example.employeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @PostMapping("/register")
    public Employee register(@RequestBody Employee employee) {
        return service.register(employee);
    }

    @GetMapping("/employee/me")
    public Employee myProfile(Principal principal) {
        return service.getMyProfile(principal);
    }

    @DeleteMapping("/employee/delete")
    public ResponseEntity<String> deleteOwnAccount(Principal principal) {
        String message = service.deleteOwnAccount(principal);
        return ResponseEntity.ok(message);
    }

    
    
    
    @PutMapping("/employee/update")
    public Employee updateOwn(@RequestBody Employee updated, Principal principal) {
        return service.updateOwnProfile(updated, principal);
    }
    @GetMapping("/admin/employees")
    public List<Employee> allEmployees() {
        return service.getAllEmployees();
    }
    
    @GetMapping("/admin/employees/{id}")
    public Employee getEmployeeById(@PathVariable int id) {
        return service.findById(id);
    }


    @PutMapping("/admin/update/{id}")
    public Employee updateByAdmin(@PathVariable int id, @RequestBody Employee updated) {
        return service.updateByAdmin(id, updated);
    }
 // EmployeeController.java
    @DeleteMapping("/admin/employees/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id) {
    	System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        String message = service.deleteById(id);
        return ResponseEntity.ok(message);
    }
    
    
    //validate password
    @PostMapping("/employee/validate-password")
    public ResponseEntity<Map<String, Boolean>> validatePassword(@RequestBody Map<String, String> payload, Principal principal) {
        boolean valid = service.validateOldPassword(principal, payload.get("password"));
        return ResponseEntity.ok(Collections.singletonMap("valid", valid));
    }

    
    
    
    

}
