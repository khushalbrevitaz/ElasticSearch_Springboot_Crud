package com.elasticSearch.elasticSearchCrud.controller;


import com.elasticSearch.elasticSearchCrud.entity.Employee;
import com.elasticSearch.elasticSearchCrud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("")
    public  String insertEmployee(@RequestBody Employee employee){
        employeeService.createEmployee(employee);
        return "inserted";
    }
    @GetMapping("")
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable String id){
        System.out.println(id+" hello");
        return employeeService.deleteEmployee(id);
    }
    @PutMapping("/{id}")
    public String updateEmployee(@PathVariable String id ,@RequestBody Employee employee) throws IOException {
        return employeeService.updateEmployee(id,employee);
    }
}
