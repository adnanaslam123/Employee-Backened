package com.example.employeemanagement.service;

import com.example.employeemanagement.dao.EmployeeDao;
import com.example.employeemanagement.dto.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
public class AdminInitializer implements CommandLineRunner {
    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@email.com";
        if (employeeDao.findByEmail(adminEmail) == null) {
            Employee admin = new Employee();
            admin.setName("Default Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(encoder.encode("admin123"));
            admin.setDepartment("Management");
            admin.setRole("ROLE_ADMIN");
            admin.setSalary(0.0);
            

            employeeDao.save(admin);
            System.out.println("✅ Default admin created");
        } else {
            System.out.println("ℹ️ Admin already exists");
        }
    }
}
