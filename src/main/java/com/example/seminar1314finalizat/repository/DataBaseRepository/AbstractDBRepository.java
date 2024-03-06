package com.example.seminar1314finalizat.repository.DataBaseRepository;


import com.example.seminar1314finalizat.domain.Entity;
import com.example.seminar1314finalizat.repository.Repository;
import com.example.seminar1314finalizat.validators.Validator;

import java.util.Optional;

public abstract class AbstractDBRepository <ID, E extends Entity<ID>> implements Repository<ID, E>
{
    protected Validator validator;
    protected DB_Access data;
    protected String table;

    public AbstractDBRepository(Validator validator, DB_Access data, String table) {
        this.validator = validator;
        this.data = data;
        this.table = table;
    }
    @Override
    public  abstract Optional<E> findOne(ID id);

    @Override
    public abstract Iterable<E> findAll();

}
