package com.example.employeemanagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employeemanagement.dto.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Integer>{
	 Employee findByEmail(String email);
}
