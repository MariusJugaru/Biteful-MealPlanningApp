package com.biteful.mealplanner.recipeservice.controllers;

import com.biteful.mealplanner.recipeservice.TestDataUtil;
import com.biteful.mealplanner.recipeservice.config.WithMockCustomUser;
import com.biteful.mealplanner.recipeservice.config.security.UserPrincipal;
import com.biteful.mealplanner.recipeservice.domain.Ingredient;
import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeCreateDto;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeDto;
import com.biteful.mealplanner.recipeservice.repositories.RecipeRepository;
import com.biteful.mealplanner.recipeservice.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class RecipeControllerTests {

    private final MockMvc mockMvc;

    public final ObjectMapper objectMapper;

    public final RecipeService recipeService;

    public final RecipeRepository recipeRepository;

    public String recipeId;

    @Autowired
    public RecipeControllerTests(MockMvc mockMvc,
                                 ObjectMapper objectMapper,
                                 RecipeService recipeService,
                                 RecipeRepository recipeRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.recipeService = recipeService;
        this.recipeRepository = recipeRepository;
    }

    @BeforeEach
    void clean() {
        recipeRepository.deleteAll();
    }

    @Test
    @WithMockCustomUser()
    void testThatRecipeCanBeCreatedAndReturns201() throws Exception {
        RecipeCreateDto recipeCreateDto = TestDataUtil.getRecipeCreateDtoA();

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeCreateDto))
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

    }

    @Test
    @WithMockCustomUser()
    void testThatRecipeCanBeCreatedAndRecalled() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        // Create recipe
        RecipeCreateDto recipeCreateDto = TestDataUtil.getRecipeCreateDtoA();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/recipes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(recipeCreateDto))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        Map<String, String> responseMessage = objectMapper.readValue(response, Map.class);

        // Get recipe
        result = mockMvc.perform(
                        MockMvcRequestBuilders.get(String.format("/api/recipes/me/%s", responseMessage.get("id")))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        response = result.getResponse().getContentAsString();

        RecipeDto recipeDto = objectMapper.readValue(response, RecipeDto.class);

        assertThat(recipeDto).isNotNull();
        assertThat(recipeDto.getId()).isNotBlank();
        assertThat(recipeDto.getTitle()).isEqualTo(recipeCreateDto.getTitle());
        assertThat(recipeDto.getDescription()).isEqualTo(recipeCreateDto.getDescription());
        assertThat(recipeDto.getVisibility()).isEqualTo(recipeCreateDto.getVisibility());
        assertThat(recipeDto.getServings()).isEqualTo(recipeCreateDto.getServings());
        assertThat(recipeDto.getImage()).isEqualTo(recipeCreateDto.getImage());
        assertThat(recipeDto.getIngredients()).hasSize(recipeCreateDto.getIngredients().size());

        for (int i = 0; i < recipeDto.getIngredients().size(); i++) {
            Ingredient expected = recipeCreateDto.getIngredients().get(i);
            Ingredient actual = recipeDto.getIngredients().get(i);

            assertThat(actual.getName()).isEqualTo(expected.getName());
            assertThat(actual.getQuantity()).isEqualTo(expected.getQuantity());
            assertThat(actual.getUnit()).isEqualTo(expected.getUnit());
        }
    }

    @Test
    @WithMockCustomUser(username = "admin1", role = "ADMIN", id = "11111111-1111-1111-1111-111111111113")
    void testThatAdminCanRecallPrivateRecipes() throws Exception {
        Recipe recipeA = TestDataUtil.createPrivateRecipeA();
        UserPrincipal user = TestDataUtil.createUserPrincipalA();
        Recipe saved = recipeService.createRecipe(recipeA, user.getId(), user.getRole());

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("/api/recipes/%s", saved.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    @WithMockCustomUser(username = "user2", id = "11111111-1111-1111-1111-111111111112")
    void testThatUserCantRecallPrivateRecipesThatTheyDontOwn() throws Exception {
        Recipe recipeA = TestDataUtil.createPrivateRecipeA();
        UserPrincipal user = TestDataUtil.createUserPrincipalA();
        Recipe saved = recipeService.createRecipe(recipeA, user.getId(), user.getRole());

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get(String.format("/api/recipes/%s", saved.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();
    }
}
