package com.example.service;

import com.example.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ICustomerService extends IGenerateService<Customer>{
    List<Customer> findCustomerByName(String name);
}
