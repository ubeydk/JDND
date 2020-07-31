package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private Item testItem;

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        testItem = new Item();
        testItem.setPrice(new BigDecimal(8));
        testItem.setId(8L);
        testItem.setDescription("testDescription");
        testItem.setName("testItem");

        List<Item> itemList = new ArrayList<>();
        itemList.add(testItem);

        when(itemRepository.findByName("testItem")).thenReturn(itemList);
        when(itemRepository.findById(8L)).thenReturn(Optional.of(testItem));
    }

    @Test
    public void get_items_by_name_happy_path(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("testItem");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void get_items_by_name_sad_path(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("testItem2");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_all_items(){
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void get_item_by_id_happy_path(){
        ResponseEntity<Item> response = itemController.getItemById(8L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void get_item_by_id_sad_path(){
        ResponseEntity<Item> response = itemController.getItemById(9L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
