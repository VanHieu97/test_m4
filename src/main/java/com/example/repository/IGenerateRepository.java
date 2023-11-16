package com.example.repository;

import java.util.List;

public interface IGenerateRepository<E> {
    List<E> getAll();
    E findById(Long id);
    void save(E e);
    void remove(long id);
}
