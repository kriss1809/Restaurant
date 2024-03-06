package com.example.seminar1314finalizat.repository;


import com.example.seminar1314finalizat.domain.Entity;

import java.util.Optional;

public interface Repository<ID, E extends Entity<ID>> {
    Optional<E> findOne(ID id);

    Iterable<E> findAll();
}