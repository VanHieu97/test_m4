package com.example.service;

import java.util.List;

public interface IGenerateService<E> {
    List<E> getAll();
    E findById(Long id);
    void save(E e);
    void delete(Long id);
}
