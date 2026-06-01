package com.biteful.mealplanner.listservice.controllers;

import com.biteful.mealplanner.listservice.config.WithMockCustomUser;
import com.biteful.mealplanner.listservice.domain.dtos.*;
import com.biteful.mealplanner.listservice.repositories.ListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;


@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ListControllerTests {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final ListRepository listRepository;

    @Autowired
    public ListControllerTests(MockMvc mockMvc, ObjectMapper objectMapper, ListRepository listRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;

        this.listRepository = listRepository;
    }

    @Test
    @WithMockCustomUser
    public void testThatListCanBeCreatedAndRecalled() throws Exception {
        ListRequest listRequest = ListTestUtils.getListA();

        MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/lists")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(listRequest))
        )
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn();

        ListResponse listResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ListResponse.class
        );

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/lists"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").value(listResponse.getId().toString())
        )
        .andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value(listResponse.getTitle())
        );
    }

    @Test
    @WithMockCustomUser
    public void testThatListCanBeUpdatedAndRecalled() throws Exception {
        ListRequest listRequest = ListTestUtils.getListA();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/lists")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(listRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        ListResponse listResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ListResponse.class
        );

        ListRequest listRequest1 = ListTestUtils.getListB();

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/lists/" + listResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listRequest1))
        )
        .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/lists"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value(listRequest1.getTitle())
        );

    }

    @Test
    @WithMockCustomUser
    public void testThatListCanDeleted() throws Exception {
        ListRequest listRequest = ListTestUtils.getListA();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/lists")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(listRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        ListResponse listResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ListResponse.class
        );

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/lists/" + listResponse.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/lists"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockCustomUser
    public void testThatItemCanBeAddedToListAndRecalled() throws Exception {
        ListRequest listRequest = ListTestUtils.getListA();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/lists")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(listRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        ListResponse listResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ListResponse.class
        );

        ListItemRequest listItemRequest = ListTestUtils.getItemA();

        result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/lists/" + listResponse.getId() + "/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listItemRequest))
        )
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn();

        ListItemResponse listItemResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ListItemResponse.class
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/lists/" + listResponse.getId() + "/items"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").value(listItemResponse.getId())
        )
        .andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value(listItemRequest.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].unit").value(listItemRequest.getUnit())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].quantity").value(listItemRequest.getQuantity())
        );
    }

    @Test
    @WithMockCustomUser
    public void testThatItemCanBeAddedToListAndUpdated() throws Exception {
        ListRequest listRequest = ListTestUtils.getListA();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/lists")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(listRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        ListResponse listResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ListResponse.class
        );

        ListItemRequest listItemRequest = ListTestUtils.getItemA();

        result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/lists/" + listResponse.getId() + "/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listItemRequest))
        )
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn();

        ListItemResponse listItemResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ListItemResponse.class
        );

        ListItemRequest listItemRequest1 = ListTestUtils.getItemB();

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/lists/" + listResponse.getId() + "/items/" + listItemResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listItemRequest1))
        )
        .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/lists/" + listResponse.getId() + "/items"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").value(listItemResponse.getId())
        )
        .andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value(listItemRequest1.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].unit").value(listItemRequest1.getUnit())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].quantity").value(listItemRequest1.getQuantity())
        );
    }

    @Test
    @WithMockCustomUser
    public void testThatItemCanBeDeleted() throws Exception {
        ListRequest listRequest = ListTestUtils.getListA();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/lists")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(listRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        ListResponse listResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ListResponse.class
        );

        ListItemRequest listItemRequest = ListTestUtils.getItemA();

        result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/lists/" + listResponse.getId() + "/items")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(listItemRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        ListItemResponse listItemResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ListItemResponse.class
        );

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/lists/" + listResponse.getId() + "/items/" + listItemResponse.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/lists/" + listResponse.getId() + "/items"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockCustomUser
    public void testThatItemCanBeChackedAndUnchecked() throws Exception {
        ListRequest listRequest = ListTestUtils.getListA();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/lists")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(listRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        ListResponse listResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ListResponse.class
        );

        ListItemRequest listItemRequest = ListTestUtils.getItemA();

        result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/lists/" + listResponse.getId() + "/items")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(listItemRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        ListItemResponse listItemResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ListItemResponse.class
        );

        UpdateCheckedRequest checkedRequest = ListTestUtils.getChecked();

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/lists/" + listResponse.getId() + "/items/" + listItemResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkedRequest))
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/lists/" + listResponse.getId() + "/items"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
                MockMvcResultMatchers.jsonPath("$[0].checked").value(true)
        );

        UpdateCheckedRequest uncheckedRequest = ListTestUtils.getUnchecked();

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/lists/" + listResponse.getId() + "/items/" + listItemResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(uncheckedRequest))
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/lists/" + listResponse.getId() + "/items"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
                MockMvcResultMatchers.jsonPath("$[0].checked").value(false)
        );
    }

}
