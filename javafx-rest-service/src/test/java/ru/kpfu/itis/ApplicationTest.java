package ru.kpfu.itis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.kpfu.itis.model.Item;
import ru.kpfu.itis.repo.ItemRepository;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void cleanUpDatabaseBeforeTests() {
        itemRepository.deleteAll();
    }

    @Test
    public void shouldReturnRepositoryLink() throws Exception {
        mockMvc.perform(get("/")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.item").exists());
    }

    @Test
    public void shouldCreateEntity() throws Exception {

        Item item = new Item("Toaster", 50);

        mockMvc
                .perform(post("/item")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("item/")));
    }

    @Test
    public void shouldRetrieveEntity() throws Exception {

        Item item = new Item("Toaster BOSH", 25);

        MvcResult mvcResult = mockMvc
                .perform(post("/item").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isCreated())
                .andReturn();

        String location = mvcResult.getResponse().getHeader("Location");

        Assert.assertNotNull(location);

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Toaster BOSH"))
                .andExpect(jsonPath("$.price").value(25));
    }

    @Test
    public void shouldDeleteEntity() throws Exception {

        Item item = new Item("Dummy Toaster", 1);

        MvcResult mvcResult = mockMvc
                .perform(post("/item").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isCreated())
                .andReturn();

        String location = mvcResult.getResponse().getHeader("Location");

        Assert.assertNotNull(location);

        mockMvc.perform(delete(location)).andExpect(status().isNoContent());

        mockMvc.perform(get(location)).andExpect(status().isNotFound());
    }

    @Test
    public void shouldRetrieveAllEntities() throws Exception {

        List<Item> items = Arrays.asList(
                new Item("Toaster BOSH", 50),
                new Item("Toaster SAMSUNG", 55),
                new Item("Dummy Toaster", 1),
                new Item("Toaster OMEGA", 100)
        );

        for (Item item : items) {

            mockMvc
                    .perform(post("/item").contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(item)))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/item"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.item", hasSize(4)))
                .andExpect(jsonPath("$.page.totalElements").value(4));

    }
}
