package com.example.employeemanagement.dao;

import com.example.employeemanagement.dto.Employee;
import com.example.employeemanagement.repo.EmployeeRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Component
public class EmployeeDao {
    @Autowired
    private EmployeeRepo repo;
    
   

    public Employee save(Employee emp) {
        return repo.save(emp);
    }

    public Employee findByEmail(String email) {
        return repo.findByEmail(email);
    }

    public List<Employee> findAll() {
        return repo.findAll();
    }

    public Employee findById(int id) {
    	Optional<Employee> carDb=repo.findById(id);
		if(carDb.isPresent()) {
			return carDb.get();
		}else
			return null;
	}
    public String deleteById(int id) {
    	if(repo.existsById(id)) {
    		repo.deleteById(id);
    		return "Employee deleted Successfully";
    	}
    	else return null;
    }
    


    
    
    
    }

