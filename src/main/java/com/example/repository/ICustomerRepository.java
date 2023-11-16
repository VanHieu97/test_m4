package com.example.repository;

import com.example.model.Customer;

import java.util.List;

public interface ICustomerRepository extends IGenerateRepository<Customer>{
    List<Customer> findCustomerByName(String name);
}
