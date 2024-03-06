package com.example.seminar1314finalizat.repository.DataBaseRepository;

import com.example.seminar1314finalizat.domain.Table;
import com.example.seminar1314finalizat.domain.Table;
import com.example.seminar1314finalizat.validators.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TableRepoDB extends AbstractDBRepository<Long, Table>
{
    public TableRepoDB(Validator validator, DB_Access data, String table) {
        super(validator, data, table);
    }

    private Optional<Table> getTable(ResultSet r) throws SQLException
    {
        Integer nr = r.getInt("number");
        Table t = new Table(nr);
        t.setId(r.getLong("id"));
        return Optional.of(t);
    }


    @Override
    public Optional<Table> findOne(Long id)
    {
        if (id==null)
        {
            throw new IllegalArgumentException("Id of entity is null!");
        }

        String findOneStatement = "SELECT * FROM tables WHERE id = ?";
        try{
            PreparedStatement st = data.createStatement(findOneStatement);
            st.setLong(1, id);
            ResultSet resultSet=st.executeQuery();
            if(resultSet.next())
                return getTable(resultSet);
            return Optional.empty();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Optional<Table> findOneByNr(Integer nr)
    {
        if (nr==null)
        {
            throw new IllegalArgumentException("Nr of table is null!");
        }

        String findOneStatement = "SELECT * FROM tables WHERE  number= ?";
        try{
            PreparedStatement st = data.createStatement(findOneStatement);
            st.setLong(1, nr);
            ResultSet resultSet=st.executeQuery();
            if(resultSet.next())
                return getTable(resultSet);
            return Optional.empty();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Iterable<Table> findAll() {
        String findAllStatement="SELECT * FROM tables";
        Set<Table> tables =new HashSet<>();
        try
        {
            PreparedStatement statement= data.createStatement(findAllStatement);
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next())
            {
                Long id=resultSet.getLong("id");
                tables.add(getTable(resultSet).get());
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return tables;
    }

}