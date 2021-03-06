package com.poly.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.model.OrderDetails;

import java.util.List;

@Repository
public interface OrderDetailRepository extends CrudRepository<OrderDetails, Integer> {
    @Query("SELECT od FROM OrderDetails od WHERE od.orders.Id = :id")
    public List<OrderDetails> getByOrderId(@Param("id") Integer id);


}
