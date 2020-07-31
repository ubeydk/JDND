package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.hibernate.criterion.Order;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private OrderController orderController;

    private Cart testCart;

    private Item testItem;

    private User testUser;

    private UserOrder testOrder;

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        testItem = new Item();
        testItem.setId(123456L);
        testItem.setName("testItem");
        testItem.setDescription("testDescription");
        testItem.setPrice(new BigDecimal(3));

        testCart = new Cart();
        testCart.setId(12345L);
        testCart.setItems(new ArrayList<Item>());
        testCart.getItems().add(testItem);
        testCart.setTotal(new BigDecimal(3));
        testCart.setUser(testUser);

        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword");
        testUser.setId(123456L);
        testUser.setCart(testCart);

        testOrder = new UserOrder();
        testOrder.setItems(new ArrayList<>());
        testOrder.getItems().add(testItem);
        testOrder.setTotal(new BigDecimal(3));
        testOrder.setUser(testUser);
        testOrder.setId(123L);



        when(userRepository.findByUsername("testUser")).thenReturn(testUser);
    }

    @Test
    public void submit_order_happy_path(){
        ResponseEntity<UserOrder> response = orderController.submit("testUser");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void submit_order_invalid_username(){
        ResponseEntity<UserOrder> response = orderController.submit("testUser2");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_orders_for_user_happy_path(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUser");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void get_orders_for_user_invalid_path(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUser2");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
