package com.elasticSearch.elasticSearchCrud.service;


import com.elasticSearch.elasticSearchCrud.entity.Employee;
import com.elasticSearch.elasticSearchCrud.repository.ElasticSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private ElasticSearchRepository elasticSearchRepository;
    public String createEmployee(Employee employee) {
        if(elasticSearchRepository.createEmployee(employee)){
            return "inserted";
        }
        return "not inserted";
    }
    public List<Employee> getAllEmployees(){
        return elasticSearchRepository.getAllEmployees();
    }
    public String  deleteEmployee(String id){
        if(elasticSearchRepository.deleteEmployee(id)){
            return "data deleted";
        }
        return "data not deleted";
    }
    public String updateEmployee(String id ,Employee employee) throws IOException {
        if(elasticSearchRepository.updateEmployee(id,employee)){
            return "updated";
        }
        return "not updated";
    }

}
