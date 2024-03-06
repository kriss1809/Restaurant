package com.example.seminar1314finalizat.repository.DataBaseRepository;

import com.example.seminar1314finalizat.domain.*;
import com.example.seminar1314finalizat.exceptions.RepositoryException;
import com.example.seminar1314finalizat.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;


public class OrderRepoDB extends AbstractDBRepository<Long, Order>
{
    public OrderRepoDB(Validator validator, DB_Access data, String table) {
        super(validator, data, table);
    }

    private Optional<Order> getOrder(ResultSet r) throws SQLException
    {
        Timestamp date = r.getTimestamp("date");
        Long waiter_id = r.getLong("waiter_id");
        Waiter waiter = getWaiter(waiter_id);
        Long table_id = r.getLong("table_id");
        Table table = getTable(table_id);
        Map<MenuItem, Integer> items = getOrderItems(r.getLong("id"));
        Order o = new Order(date.toLocalDateTime(), items, waiter, table);
        o.setId(r.getLong("id"));
        return Optional.of(o);
    }

    private Map<MenuItem, Integer> getOrderItems(Long orderId) {
        Map<MenuItem, Integer> items = new HashMap<>();

        String query = "SELECT menu_item_id, quantity FROM order_items WHERE order_id = ?";

        try (PreparedStatement preparedStatement = data.createStatement(query)) {
            preparedStatement.setLong(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long menuItemId = resultSet.getLong("menu_item_id");
                int quantity = resultSet.getInt("quantity");
                MenuItem menuItem = getMenuItem(menuItemId);
                items.put(menuItem, quantity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return items;
    }

    private MenuItem getMenuItem(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id of entity is null!");
        }

        String findOneStatement = "SELECT * FROM menu_items WHERE id = ?";
        try {
            PreparedStatement st = data.createStatement(findOneStatement);
            st.setLong(1, id);
            ResultSet r = st.executeQuery();
            if (r.next()) {
                String name = r.getString("name");
                Float price = r.getFloat("price");
                Short preparation_time_in_minutes = r.getShort("preparation_time_in_minutes");
                MenuItem mi = new MenuItem(name, price, preparation_time_in_minutes);
                mi.setId(r.getLong("id"));
                return mi;  // Adaugă această linie pentru a întoarce obiectul MenuItem creat
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private Waiter getWaiter(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id of entity is null!");
        }

        String findWaiterStatement = "SELECT * FROM waiters WHERE id = ?";
        try {
            PreparedStatement st = data.createStatement(findWaiterStatement);
            st.setLong(1, id);
            ResultSet r = st.executeQuery();
            if (r.next()) {
                String name = r.getString("name");
                Waiter w = new Waiter(name);
                w.setId(r.getLong("id"));
                return w;  // Adaugă această linie pentru a întoarce obiectul Waiter creat
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Table getTable(Long id)
    {
        if (id==null)
        {
            throw new IllegalArgumentException("Id of entity is null!");
        }

        String findWaiterStatement = "SELECT * FROM tables WHERE id = ?";
        try{
            PreparedStatement st = data.createStatement(findWaiterStatement);
            st.setLong(1, id);
            ResultSet r =st.executeQuery();
            if(r.next()) {
                Integer nr = r.getInt("number");
                Table t = new Table(nr);
                t.setId(r.getLong("id"));
                return t;
            }
            return null;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Optional<Order>save(Order entity) {
        String insertSQL = "INSERT INTO orders(date, waiter_id, table_id) VALUES(?, ?, ?)";
        try {
            PreparedStatement statement= data.createStatement(insertSQL);
            statement.setTimestamp(1, Timestamp.valueOf(entity.getDate()));
            Waiter waiter = entity.getWaiter();
            if (waiter != null) {
                statement.setLong(2, waiter.getId());
            } else {
                statement.setObject(2, null); // Setează null în loc de ID
            }
            statement.setLong(3, entity.getTable().getId());

            int response=statement.executeUpdate();
            return response==0?Optional.of(entity):Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    public Optional<Order> findMostRecent()
    {
        String findOneStatement = "SELECT * FROM orders WHERE id = (SELECT MAX(id) FROM orders)";
        try{
            PreparedStatement st = data.createStatement(findOneStatement);
            ResultSet resultSet=st.executeQuery();
            if(resultSet.next())
                return getOrder(resultSet);
            return Optional.empty();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Order> findOne(Long id)
    {
        if (id==null)
        {
            throw new IllegalArgumentException("Id of entity is null!");
        }

        String findOneStatement = "SELECT * FROM orders WHERE id = ?";
        try{
            PreparedStatement st = data.createStatement(findOneStatement);
            st.setLong(1, id);
            ResultSet resultSet=st.executeQuery();
            if(resultSet.next())
                return getOrder(resultSet);
            return Optional.empty();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Iterable<Order> findAll() {
        String findAllStatement="SELECT * FROM orders";
        Set<Order> Orders =new HashSet<>();
        try
        {
            PreparedStatement statement= data.createStatement(findAllStatement);
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next())
            {
                Long id=resultSet.getLong("id");
                Orders.add(getOrder(resultSet).get());
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return Orders;
    }

    public Iterable<Order> findAllWaiterNull() {
        String findAllStatement="SELECT * FROM orders WHERE waiter_id is NULL";
        Set<Order> Orders =new HashSet<>();
        try
        {
            PreparedStatement statement= data.createStatement(findAllStatement);
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next())
            {
                Long id=resultSet.getLong("id");
                Orders.add(getOrder(resultSet).get());
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return Orders;
    }


    public Float getBill(Integer nr)
    {
        if (nr==null)
        {
            throw new IllegalArgumentException("Nr of table is null!");
        }

        String statement = "SELECT " +
                "    SUM(mi.price * oi.quantity) AS total_item_price " +
                "FROM " +
                "    public.order_items oi " +
                "JOIN " +
                "    public.menu_items mi ON oi.menu_item_id = mi.id " +
                "JOIN " +
                "    public.orders o ON oi.order_id = o.id " +
                "JOIN " +
                "    public.tables t ON o.table_id = t.id " +
                "WHERE " +
                "    t.number = ?";
        try{
            PreparedStatement st = data.createStatement(statement);
            st.setInt(1, nr);
            ResultSet resultSet=st.executeQuery();
            if(resultSet.next())
                return resultSet.getFloat("total_item_price");
            return null;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void updateWaiter(Order o, Waiter w)
    {
        if (o == null) {
            throw new RepositoryException("Entity must not be null");
        }
        String updateStatement = "UPDATE orders SET waiter_id = ? WHERE id = ?";
        try {
            PreparedStatement statement = data.createStatement(updateStatement);
            statement.setLong(1,w.getId());
            statement.setLong(2,o.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    public Iterable<Order> raport(LocalDateTime ziua, Waiter w) {
        String findAllStatement = "SELECT * FROM orders WHERE waiter_id = ? AND DATE(date) = ?";
        Set<Order> orders = new HashSet<>();

        try {
            PreparedStatement statement = data.createStatement(findAllStatement);
            statement.setLong(1, w.getId());
            statement.setDate(2, java.sql.Date.valueOf(ziua.toLocalDate()));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                orders.add(getOrder(resultSet).get());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orders;
    }

}
