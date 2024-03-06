package com.example.seminar1314finalizat.repository.DataBaseRepository;

import com.example.seminar1314finalizat.domain.*;
import com.example.seminar1314finalizat.exceptions.RepositoryException;
import com.example.seminar1314finalizat.validators.Validator;
import javafx.scene.control.Menu;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OrderItemsRepoDB extends AbstractDBRepository<Tuple<Long, Long>, OrderItems>{

    public OrderItemsRepoDB(Validator validator, DB_Access data, String table) {
        super(validator, data, table);
    }

    private OrderItems getOrderItemsFromStatement(ResultSet resultSet) throws SQLException
    {
        Long id_order = resultSet.getLong("order_id");
        Long id_menu_item = resultSet.getLong("menu_item_id");
        Integer quantity = resultSet.getInt("quantity");

        Order o = getOrder(id_order);
        o.setId(id_order);
        MenuItem mi = getMenuItem(id_menu_item);
        mi.setId(id_menu_item);

        OrderItems oi = new OrderItems(o, mi, quantity);
        return oi;
    }

    private Order getOrder(Long id)
    {
        if (id==null)
        {
            throw new IllegalArgumentException("Id of entity is null!");
        }

        String findOneStatement = "SELECT * FROM orders WHERE id = ?";
        try{
            PreparedStatement st = data.createStatement(findOneStatement);
            st.setLong(1, id);
            ResultSet r=st.executeQuery();
            if(r.next()){
                Timestamp date = r.getTimestamp("date");
                Long waiter_id = r.getLong("waiter_id");
                Waiter waiter = getWaiter(waiter_id);
                Long table_id = r.getLong("table_id");
                Table table = getTable(table_id);
                Map<MenuItem, Integer> items = getOrderItems(id);
                Order o = new Order(date.toLocalDateTime(), items, waiter, table);
                o.setId(r.getLong("id"));
            }
            return null;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
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

    private Waiter getWaiter(Long id)
    {
        if (id==null)
        {
            throw new IllegalArgumentException("Id of entity is null!");
        }

        String findWaiterStatement = "SELECT * FROM waiters WHERE id = ?";
        try{
            PreparedStatement st = data.createStatement(findWaiterStatement);
            st.setLong(1, id);
            ResultSet r =st.executeQuery();
            if(r.next()) {
                String name = r.getString("name");
                Waiter w = new Waiter(name);
                w.setId(r.getLong("id"));
            }
            return null;
        }
        catch (SQLException e)
        {
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
            }
            return null;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private MenuItem getMenuItem(Long id)
    {
        if (id==null)
        {
            throw new IllegalArgumentException("Id of entity is null!");
        }

        String findOneStatement = "SELECT * FROM menu_items WHERE id = ?";
        try{
            PreparedStatement st = data.createStatement(findOneStatement);
            st.setLong(1, id);
            ResultSet r=st.executeQuery();
            if(r.next()){
                String name = r.getString("name");
                Float price = r.getFloat("price");
                Short preparation_time_in_minutes = r.getShort("preparation_time_in_minutes");
                MenuItem mi = new MenuItem(name, price, preparation_time_in_minutes);
                mi.setId(r.getLong("id"));
            }
            return null;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<OrderItems> findOne(Tuple<Long,Long> id) {
        if (id==null)
        {
            throw new IllegalArgumentException("Id of entity is  null!");
        }
        String findOneStatement="SELECT * FROM order_items" +
                " WHERE order_id = ? AND menu_item_id=?";
        try
        {
            PreparedStatement statement=data.createStatement(findOneStatement);
            statement.setLong(1,id.getLeft());
            statement.setLong(2,id.getRight());
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next())
            {
                OrderItems item =getOrderItemsFromStatement(resultSet);
                return Optional.of(item);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<OrderItems>save(OrderItems entity) {
        String insertSQL="INSERT INTO order_items(order_id, menu_item_id, quantity) values(?, ?, ?)";
        try
        {
            PreparedStatement statement= data.createStatement(insertSQL);
            statement.setLong(1, entity.getOrder().getId());
            statement.setLong(2, entity.getMenu_item().getId());
            statement.setInt(3, entity.getQuantity());

            int response=statement.executeUpdate();
            return response==0?Optional.of(entity):Optional.empty();
        }
        catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Iterable<OrderItems> findAll() {
        return null;
    }


}
