package com.example.seminar1314finalizat.repository.DataBaseRepository;

import com.example.seminar1314finalizat.domain.MenuItem;
import com.example.seminar1314finalizat.domain.MenuItem;
import com.example.seminar1314finalizat.validators.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MenuItemRepoDB extends AbstractDBRepository<Long, MenuItem>
{
    public MenuItemRepoDB(Validator validator, DB_Access data, String table) {
        super(validator, data, table);
    }

    private Optional<MenuItem> getMenuItem(ResultSet r, Long id) throws SQLException
    {
        String name = r.getString("name");
        Float price = r.getFloat("price");
        Short preparation_time_in_minutes = r.getShort("preparation_time_in_minutes");
        MenuItem mi = new MenuItem(name, price, preparation_time_in_minutes);
        mi.setId(r.getLong("id"));
        return Optional.of(mi);
    }


    @Override
    public Optional<MenuItem> findOne(Long id)
    {
        if (id==null)
        {
            throw new IllegalArgumentException("Id of entity is null!");
        }

        String findOneStatement = "SELECT * FROM menu_items WHERE id = ?";
        try{
            PreparedStatement st = data.createStatement(findOneStatement);
            st.setLong(1, id);
            ResultSet resultSet=st.executeQuery();
            if(resultSet.next())
                return getMenuItem(resultSet,id);
            return Optional.empty();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Iterable<MenuItem> findAll() {
        String findAllStatement="SELECT * FROM menu_items";
        Set<MenuItem> MenuItems =new HashSet<>();
        try
        {
            PreparedStatement statement= data.createStatement(findAllStatement);
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next())
            {
                Long id=resultSet.getLong("id");
                MenuItems.add(getMenuItem(resultSet,id).get());
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return MenuItems;
    }

    public Iterable<MenuItem> top5() {
        String top5Statement = "SELECT menu_items.*, COUNT(order_items.menu_item_id) AS order_count " +
                "FROM public.menu_items " +
                "LEFT JOIN public.order_items ON menu_items.id = order_items.menu_item_id " +
                "GROUP BY menu_items.id " +
                "ORDER BY order_count DESC " +
                "LIMIT 5";

        Set<MenuItem> top5MenuItems = new HashSet<>();

        try {
            PreparedStatement statement = data.createStatement(top5Statement);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                top5MenuItems.add(getMenuItem(resultSet, id).get());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return top5MenuItems;
    }



}
