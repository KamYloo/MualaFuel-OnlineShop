package com.example.MualaFuel_Backend.dao;

import java.util.Optional;

public interface Dao<T, U>{
    T save(T t);
    Optional<T> findById(U u);
    void delete(U u);
}
