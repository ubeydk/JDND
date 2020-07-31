package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private Cart testCart;

    private Item testItem;

    private User testUser;

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);



        testItem = new Item();
        testItem.setId(123456L);
        testItem.setName("testItem");
        testItem.setDescription("testDescription");
        testItem.setPrice(new BigDecimal(3));

        when(itemRepository.findById(123456L)).thenReturn(Optional.of(testItem));

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

        when(userRepository.findByUsername("testUser")).thenReturn(testUser);
    }

    @Test
    public void add_to_cart_happy_path(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(123456L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("testUser");

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void add_to_cart_invalid_user_name(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(123456L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("testUserx");

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void add_to_cart_invalid_item_id(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(12345L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("testUser");

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void remove_from_cart_happy_path(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(123456L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("testUser");

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void remove_from_cart_invalid_user(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(123456L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("testUserx");

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void remove_from_cart_invalid_item(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(12345L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("testUser");

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
