package com.example.MualaFuel_Backend.mapper;

public interface Mapper<A,B> {
    B mapTo(A a);

    A mapFrom(B b);
}
