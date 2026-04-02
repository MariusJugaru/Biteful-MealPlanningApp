package com.biteful.mealplanner.recipeservice.services;

import com.biteful.mealplanner.recipeservice.TestDataUtil;
import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.exceptions.runtime.NotAllowed;
import com.biteful.mealplanner.recipeservice.exceptions.runtime.RecipeNotFound;
import com.biteful.mealplanner.recipeservice.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class RecipeServiceImplTests {

    private RecipeService underTest;
    private RecipeRepository recipeRepository;

    @Autowired
    public RecipeServiceImplTests(RecipeService recipeService, RecipeRepository recipeRepository) {
        this.underTest = recipeService;
        this.recipeRepository = recipeRepository;
    }

    @BeforeEach
    void clean() {
        recipeRepository.deleteAll();
    }

    @Test
    public void testThatARecipeCanBeCreatedAndRecalledById() {
        Recipe recipe = TestDataUtil.createPrivateRecipeA();
        Recipe saved = underTest.createRecipe(recipe, UUID.randomUUID(), "USER");

        Recipe result = underTest.getRecipeById(saved.getId());
        assertThat(result).isNotNull();
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(saved);
    }

    @Test
    public void testThatRecipesCanBeCreatedAndRecalledByUserId() {
        Recipe recipeA = TestDataUtil.createPrivateRecipeA();
        Recipe savedA = underTest.createRecipe(recipeA, UUID.randomUUID(), "USER");

        Recipe recipeB = TestDataUtil.createPublicRecipeB();
        Recipe savedB = underTest.createRecipe(recipeB, savedA.getUserId(), "USER");

        Page<Recipe> results = underTest.getRecipesByUser(savedA.getUserId(), PageRequest.of(0, 10));

        assertThat(results).hasSize(2);
        assertThat(results)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt", "updatedAt")
                .containsExactly(savedA, savedB);
    }

    @Test
    public void testThatUserCanRecallTheirOwnRecipe() {
        Recipe recipe = TestDataUtil.createPrivateRecipeA();
        Recipe saved = underTest.createRecipe(recipe, UUID.randomUUID(), "USER");

        Recipe result = underTest.getByIdAndUserId(saved.getId(), saved.getUserId());
        assertThat(result).isNotNull();
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(saved);
    }

    @Test
    public void testThatUserCanRecallPublicRecipe() {
        Recipe recipe = TestDataUtil.createPublicRecipeB();
        Recipe saved = underTest.createRecipe(recipe, UUID.randomUUID(), "USER");

        Recipe result = underTest.getRecipeWithAccess(saved.getId(), TestDataUtil.createUserPrincipalA());
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(saved);
    }

    @Test
    public void testThatUserCantRecallPrivateRecipeTheyDontOwn() {
        Recipe recipe = TestDataUtil.createPrivateRecipeA();
        Recipe saved = underTest.createRecipe(recipe, UUID.fromString("11111111-1111-1111-1111-111111111112"), "USER");

        try {
            Recipe result = underTest.getRecipeWithAccess(saved.getId(), TestDataUtil.createUserPrincipalA());
            assert false;
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo(new NotAllowed().getMessage());
        }
    }

    @Test
    public void testThatAdminCanRecallPrivateRecipes() {
        Recipe recipe = TestDataUtil.createPrivateRecipeA();
        Recipe saved = underTest.createRecipe(recipe, UUID.fromString("11111111-1111-1111-1111-111111111112"), "USER");

        Recipe result = underTest.getRecipeWithAccess(saved.getId(), TestDataUtil.createAdminPrincipalA());
        assertThat(result).isNotNull();
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(saved);
    }

    @Test
    public void testThatCantRecallRecipeThatDoesntExists() {
        try {
            Recipe result = underTest.getRecipeWithAccess("123", TestDataUtil.createAdminPrincipalA());
            assert false;
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo(new RecipeNotFound().getMessage());
        }
    }

}
