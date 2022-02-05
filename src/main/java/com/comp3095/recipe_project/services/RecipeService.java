//*********************************************************************************
//* Project: Recipe Spring Boot Application
// * Assignment: Assignment 2
// * Author(s): Stephen Davis, Michael Sirna, Aleksandr Kudin, Matthew Campbell
// * Student Number: 101294116, 101278670, 101258693, 101289518
// * Date: December 6, 2021
// * Description: This class is for managing and loading recipes in the program.
//*********************************************************************************
package com.comp3095.recipe_project.services;

import com.comp3095.recipe_project.domain.Ingredient;
import com.comp3095.recipe_project.domain.Recipe;
import com.comp3095.recipe_project.domain.User;
import com.comp3095.recipe_project.repositories.IngredientRepository;
import com.comp3095.recipe_project.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RecipeService {
    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    public void addRecipe(String title,
                          String description,
                          String prepTime,
                          String cookTime,
                          String strIngredients,
                          String instructions,
                          User user){
        // Create array containing ingredients names entered.
        List<String> recipeIngredientsList = List.of(strIngredients.split(","));

        // Create new Recipe object without ingredients.
        Recipe newRecipe = new Recipe(title, description, prepTime, cookTime, instructions, user);

        // Loop through all ingredients names in array.
        for (String ingredient : recipeIngredientsList) {
            // Create Ingredient instance for each ingredient name entered.
            Ingredient savedIngredient = ingredientRepository.save(new Ingredient(ingredient));
            // Get ingredient list state from the recipe object.
            Set<Ingredient> recipeIngredients = newRecipe.getRecipeIngredients();
            // Add ingredient to the recipe ingredient list state.
            recipeIngredients.add(savedIngredient);
            // Insert updated recipe ingredient list state to the recipe object.
            newRecipe.setRecipeIngredients(recipeIngredients);
        }

        // Save newly created recipe to the database.
        recipeRepository.save(newRecipe);
    }

}
