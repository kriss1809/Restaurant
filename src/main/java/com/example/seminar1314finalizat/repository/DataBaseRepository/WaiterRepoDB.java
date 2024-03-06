package com.example.seminar1314finalizat.repository.DataBaseRepository;

import com.example.seminar1314finalizat.domain.Waiter;
import com.example.seminar1314finalizat.exceptions.RepositoryException;
import com.example.seminar1314finalizat.validators.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class WaiterRepoDB extends AbstractDBRepository<Long, Waiter> 
{
    public WaiterRepoDB(Validator validator, DB_Access data, String table) {
        super(validator, data, table);
    }

    private Optional<Waiter> getWaiter(ResultSet r) throws SQLException
    {
        String name = r.getString("name");
        Waiter w = new Waiter(name);
        w.setId(r.getLong("id"));
        return Optional.of(w);
    }


    @Override
    public Optional<Waiter> findOne(Long id)
    {
        if (id==null)
        {
            throw new IllegalArgumentException("Id of entity is null!");
        }

        String findOneStatement = "SELECT * FROM waiters WHERE id = ?";
        try{
            PreparedStatement st = data.createStatement(findOneStatement);
            st.setLong(1, id);
            ResultSet resultSet=st.executeQuery();
            if(resultSet.next())
                return getWaiter(resultSet);
            return Optional.empty();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Optional<Waiter> findByName(String nume)
    {
        if (nume==null)
        {
            throw new IllegalArgumentException("Name of entity is null!");
        }

        String findOneStatement = "SELECT * FROM waiters WHERE name = ?";
        try{
            PreparedStatement st = data.createStatement(findOneStatement);
            st.setString(1, nume);
            ResultSet resultSet=st.executeQuery();
            if(resultSet.next())
                return getWaiter(resultSet);
            return Optional.empty();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Iterable<Waiter> findAll() {
        String findAllStatement="SELECT * FROM waiters";
        Set<Waiter> waiters =new HashSet<>();
        try
        {
            PreparedStatement statement= data.createStatement(findAllStatement);
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next())
            {
                Long id=resultSet.getLong("id");
                waiters.add(getWaiter(resultSet).get());
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return waiters;
    }

}
